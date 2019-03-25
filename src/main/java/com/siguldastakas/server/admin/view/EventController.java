package com.siguldastakas.server.admin.view;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.siguldastakas.server.admin.Path;
import com.siguldastakas.server.admin.data.Event;
import com.siguldastakas.server.admin.data.*;
import com.siguldastakas.server.admin.iofxml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

import static spark.Spark.halt;

public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    public static final ViewRoute view = (model, req, res) -> {
        String path = req.params("path");
        String number = req.params("event");
        Event event = event(path, number);
        if (event == null) halt(404);

        model.put("event", event);
        model.put("upload", Path.path(req, Path.SERIES, path, number, "upload"));
        model.template("event/view.ftl");
    };

    public static final ViewRoute upload = (model, req, res) -> {
        String path = req.params("path");
        String number = req.params("event");
        Event event = event(path, number);
        if (event == null) halt(404);

        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
        try {
            ObjectMapper mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ResultList resultList = mapper.readValue(req.raw().getPart("xml").getInputStream(), ResultList.class);

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
            eventResults.classes = classes.stream().sorted().toArray(RunnerClass[]::new);

            model.put("event", event);
            model.put("classes", eventResults.classes);
            model.template("event/preview.ftl");
        } catch (IOException | ServletException e) {
            log.error("Failed to upload results", e);
        }
    };

    private static Event event(String path, String number) {
        try {
            return DataModel.instance().event(path, Integer.parseInt(number));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static RunnerResult.Status status(String string) {
        if ("OK".equals(string)) return RunnerResult.Status.OK;
        if ("MissingPunch".equals(string)) return RunnerResult.Status.MP;
        if ("Disqualified".equals(string)) return RunnerResult.Status.DSQ;
        if ("DidNotStart".equals(string)) return RunnerResult.Status.DNS;
        return null;
    }

}
