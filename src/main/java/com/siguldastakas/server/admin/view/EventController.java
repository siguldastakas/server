package com.siguldastakas.server.admin.view;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.siguldastakas.server.admin.ContextHelper;
import com.siguldastakas.server.admin.Path;
import com.siguldastakas.server.admin.data.Event;
import com.siguldastakas.server.admin.data.*;
import com.siguldastakas.server.admin.data.overall.OverallResults;
import com.siguldastakas.server.admin.data.overall.OverallResultsBuilder;
import com.siguldastakas.server.admin.iofxml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import javax.naming.NamingException;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static spark.Spark.halt;

public class EventController {

    private interface EventViewRoute extends ViewRoute {
        @Override
        default void handle(Model model, Request req, Response res) {
            Object[] array = seriesAndEvent(req);
            handle((Series) array[0], (Event) array[1], model, req, res);
        }

        void handle(Series series, Event event, Model model, Request req, Response res);
    }

    private interface EventRoute extends Route {
        @Override
        default Object handle(Request req, Response res) throws Exception {
            Object[] array = seriesAndEvent(req);
            return handle((Series) array[0], (Event) array[1], req, res);
        }

        Object handle(Series series, Event event, Request req, Response res) throws Exception;
    }

    private static Object[] seriesAndEvent(Request req) {
        String path = req.params("path");
        String number = req.params("event");
        int eventNumber = Integer.parseInt(number);

        Series series = DataModel.instance().series(path);
        if (series == null) halt(404);

        Event event = series.event(eventNumber);
        if (event == null) halt(404);

        return new Object[] { series, event };
    }

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private static final java.nio.file.Path uploadPath;
    private static final MultipartConfigElement multipartConfigElement;
    static {
        String path;
        try {
            path = ContextHelper.lookup("uploadPath");
        } catch (NamingException e) {
            log.error("Configuration uploadPath is not set.");
            path = "/tmp";
        }
        uploadPath = Paths.get(path);
        multipartConfigElement = new MultipartConfigElement(
                uploadPath.toAbsolutePath().toString(),
                2_097_152,
                2_097_152,
                0);
    }

    private static final String SESSION_FILE = "file";
    private static final String SESSION_SERIES = "series";
    private static final String SESSION_EVENT = "event";
    private static final String SESSION_RESULTS = "results";

    public static final EventViewRoute view = (series, event, model, req, res) -> {
        model.put("event", event);
        model.put("saved", DataModel.instance().hasResults(series.path, event.number));
        model.put("upload", Path.path(req, Path.SERIES, series.path, String.valueOf(event.number), "upload"));
        model.template("event/view.ftl");
    };

    public static final EventViewRoute upload = (series, event, model, req, res) -> {
        Session session = req.session();
        String file = session.attribute("file");
        if (file != null) {
            File oldFile = uploadPath.resolve(file).toFile();
            if (oldFile.exists()) oldFile.delete();
        }

        req.attribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

        try {
            ObjectMapper mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Part part = req.raw().getPart("xml");
            file = String.valueOf(System.currentTimeMillis());
            part.write(file);

            session.attribute(SESSION_FILE, file);
            session.attribute(SESSION_SERIES, series.path);
            session.attribute(SESSION_EVENT, event.number);

            ResultList resultList = mapper.readValue(part.getInputStream(), ResultList.class);

            List<RunnerClass> classes = new ArrayList<>(resultList.classResults.length);

            for (ClassResult cls : resultList.classResults) {
                if (cls.personResults.length == 0) continue;

                RunnerClass runnerClass = new RunnerClass();
                runnerClass.name = cls.classDetails.name;

                PersonResult top = cls.personResults[0];
                int races = top.results.length;

                Course[] courses;
                Map<Integer, Integer> courseIndexes = null;

                if (races > 1) {
                    courses = new Course[races];
                    for (int i = 0; i < races; i++) courses[i] = top.results[i].course;
                    Arrays.sort(courses, Comparator.comparing(o -> o.name));

                    runnerClass.courses = new String[races];
                    courseIndexes = new HashMap<>();
                    for (int i = 0; i < races; i++) {
                        runnerClass.courses[i] = courses[i].name;
                        courseIndexes.put(courses[i].id, i);
                    }
                }

                Runner[] runners = new Runner[cls.personResults.length];
                for (int i = 0; i < runners.length; i++) {
                    PersonResult personResult = cls.personResults[i];

                    Runner runner = new Runner();
                    runner.name = personResult.person.name.given + " " + personResult.person.name.family;
                    runner.club = personResult.organization.name;

                    OverallResult overallResult = personResult.results[races - 1].overallResult;

                    runner.overall = new RunnerResult();
                    runner.overall.status = status(overallResult.status);
                    if (runner.overall.status == RunnerResult.Status.OK) {
                        runner.overall.time = overallResult.time;
                        runner.overall.timeBehind = overallResult.timeBehind;
                        runner.overall.position = overallResult.position;
                    }

                    if (races > 1) {
                        runner.results = new RunnerResult[races];
                        for (Result result : personResult.results) {
                            int r = courseIndexes.get(result.course.id);
                            runner.results[r] = new RunnerResult();
                            runner.results[r].status = status(result.status);
                            if (runner.results[r].status == RunnerResult.Status.OK) runner.results[r].time = result.time;
                        }
                    }

                    runners[i] = runner;
                }

                Arrays.sort(runners, Comparator.comparing(o -> o.overall));
                int best = runners[0].overall.time;
                for (Runner runner : runners) {
                    if (runner.overall.status == RunnerResult.Status.OK) {
                        runner.points = 1000 * best / runner.overall.time;
                    }
                }

                if (races > 1) {
                    for (int i = 0; i < races; i++) {
                        final int ri = i;
                        Runner[] sorted = Arrays.stream(runners).sorted(Comparator.comparing(o -> o.results[ri])).toArray(Runner[]::new);
                        int rank1 = 1;
                        int best1 = sorted[0].results[i].time;
                        for (Runner runner : sorted) {
                            if (runner.results[i].status == RunnerResult.Status.OK) {
                                runner.results[i].timeBehind = runner.results[i].time - best1;
                                runner.results[i].position = rank1++;
                            }
                        }
                    }
                }

                runnerClass.runners = runners;
                classes.add(runnerClass);
            }

            EventResults eventResults = new EventResults();
            eventResults.classes = classes.stream().sorted(Comparator.comparing(o -> series.indexOfClass(o.name))).toArray(RunnerClass[]::new);

            session.attribute(SESSION_RESULTS, eventResults);

            model.put("event", event);
            model.put("classes", eventResults.classes);
            model.put("save", Path.path(req, Path.SERIES, series.path, String.valueOf(event.number), "save"));
            model.put("file", file);
            model.template("event/preview.ftl");
        } catch (IOException | ServletException e) {
            log.error("Failed to upload results", e);
        }
    };

    public static EventRoute save = (series, event, req, res) -> {
        Session session = req.session();
        String file = req.queryParams("file");

        if (!series.path.equals(session.attribute(SESSION_SERIES))
                || event.number != (int) session.attribute(SESSION_EVENT)
                || file == null || !file.equals(session.attribute(SESSION_FILE))) {
            halt(500);
        }

        EventResults results = session.attribute(SESSION_RESULTS);
        DataModel.instance().save(series.path, event.number, results, uploadPath.resolve(file));

        EventResults[] eventResults = new EventResults[series.events.length];
        for (int e = 0; e < series.events.length; e++) {
            Event otherEvent = series.events[e];
            eventResults[e] = otherEvent.number == event.number
                    ? results
                    : DataModel.instance().results(series.path, otherEvent.number);
        }
        OverallResults overallResults = OverallResultsBuilder.build(series, eventResults);
        DataModel.instance().save(series.path, overallResults);

        res.redirect(Path.path(req, Path.SERIES, series.path, String.valueOf(event.number)));
        return "Saved!";
    };

    private static RunnerResult.Status status(String string) {
        if ("OK".equals(string)) return RunnerResult.Status.OK;
        if ("MissingPunch".equals(string)) return RunnerResult.Status.MP;
        if ("Disqualified".equals(string)) return RunnerResult.Status.DSQ;
        if ("DidNotStart".equals(string)) return RunnerResult.Status.DNS;
        return null;
    }

}
