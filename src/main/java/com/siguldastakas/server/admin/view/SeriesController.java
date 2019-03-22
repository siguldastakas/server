package com.siguldastakas.server.admin.view;

public class SeriesController {

    public static ViewRoute series = (model, req, res) -> {
        model.template("series.ftl");
    };

}
