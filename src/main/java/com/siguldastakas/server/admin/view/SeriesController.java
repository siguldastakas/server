package com.siguldastakas.server.admin.view;

import com.siguldastakas.server.admin.ContextHelper;
import com.siguldastakas.server.admin.Path;
import com.siguldastakas.server.admin.data.DataModel;
import com.siguldastakas.server.admin.data.Series;
import com.siguldastakas.server.output.IndexPage;
import com.siguldastakas.server.output.SeriesPage;
import spark.Route;

import java.nio.file.Paths;

import static spark.Spark.halt;

public class SeriesController {

    public static ViewRoute list = (model, req, res) -> {
        Series[] series = DataModel.instance().allSeries();
        model.put("series", series);
        model.put("link", Path.path(req, Path.SERIES));
        model.put("update", Path.path(req, Path.SERIES, "update"));
        model.template("series/list.ftl");
    };

    public static ViewRoute view = (model, req, res) -> {
        String path = req.params("path");
        Series series = DataModel.instance().series(path);
        if (series == null) halt(404);
        model.put("series", series);
        model.put("link", Path.path(req, Path.SERIES, path));
        model.put("update", Path.path(req, Path.SERIES, path, "update"));
        model.template("series/view.ftl");
    };

    public static Route updateIndex = (req, res) -> {
        Series[] series = DataModel.instance().allSeries();
        java.nio.file.Path outputPath = Paths.get((String) ContextHelper.lookup("outputPath"));
        IndexPage.process(series, outputPath);
        res.redirect(Path.path(req, Path.SERIES));
        return "Updated!";
    };

    public static Route updateSeries = (req, res) -> {
        String path = req.params("path");
        Series series = DataModel.instance().series(path);
        if (series == null) halt(404);

        boolean overall = DataModel.instance().hasOverallResults(path);
        boolean[] results = new boolean[series.events.length];
        for (int e = 0; e < results.length; e++) results[e] = DataModel.instance().hasResults(path, series.events[e].number);

        java.nio.file.Path outputPath = Paths.get((String) ContextHelper.lookup("outputPath"));
        SeriesPage.process(series, overall, results, outputPath.resolve(path));
        res.redirect(Path.path(req, Path.SERIES, path));
        return "Updated!";
    };

}
