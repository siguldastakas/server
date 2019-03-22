package com.siguldastakas.server.admin.view;

import com.siguldastakas.server.admin.data.DataModel;
import com.siguldastakas.server.admin.data.Series;

public class SeriesController {

    public static ViewRoute list = (model, req, res) -> {
        Series[] series = DataModel.instance().allSeries();
        model.put("series", series);
        model.template("series.ftl");
    };

}
