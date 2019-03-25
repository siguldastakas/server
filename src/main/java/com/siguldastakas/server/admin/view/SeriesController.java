package com.siguldastakas.server.admin.view;

import com.siguldastakas.server.admin.Path;
import com.siguldastakas.server.admin.data.DataModel;
import com.siguldastakas.server.admin.data.Series;
import static spark.Spark.halt;

public class SeriesController {

    public static ViewRoute list = (model, req, res) -> {
        Series[] series = DataModel.instance().allSeries();
        model.put("series", series);
        model.put("link", Path.path(req, Path.SERIES));
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

}
