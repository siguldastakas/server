package com.siguldastakas.server.admin.view;

import com.siguldastakas.server.admin.ContextHelper;
import com.siguldastakas.server.admin.Path;
import com.siguldastakas.server.admin.data.DataModel;
import com.siguldastakas.server.admin.data.Series;
import com.siguldastakas.server.output.IndexPage;
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
        model.template("series/view.ftl");
    };

    public static Route update = (req, res) -> {
        Series[] series = DataModel.instance().allSeries();
        java.nio.file.Path outputPath = Paths.get((String) ContextHelper.lookup("outputPath"));
        IndexPage.process(series, outputPath);
        res.redirect(Path.path(req, Path.SERIES));
        return "Updated!";
    };

}
