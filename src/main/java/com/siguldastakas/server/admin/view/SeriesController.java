package com.siguldastakas.server.admin.view;

import com.siguldastakas.server.admin.Path;
import com.siguldastakas.server.admin.data.DataModel;
import com.siguldastakas.server.admin.data.Series;

public class SeriesController {

    public static ViewRoute list = (model, req, res) -> {
        Series[] series = DataModel.instance().allSeries();
        model.put("series", series);
        model.put("link", Path.path(req, Path.SERIES));
        model.template("series/list.ftl");
    };

    public static ViewRoute view = (model, req, res) -> {
        Series series = DataModel.instance().series(req.params("path"));
        model.put("series", series);
        model.template("series/view.ftl");
    };

}
