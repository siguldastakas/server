package com.siguldastakas.server.output;

import com.siguldastakas.server.admin.data.Series;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SeriesPage extends Page {

    public static void process(Series series, boolean overall, boolean[] results, Path outputPath) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("name", series.name);
        model.put("title", series.name + " – Siguldas Takas");
        model.put("events", series.events);
        model.put("overall", overall);
        model.put("results", results);
        Page.process(model, "series.ftl", outputPath, "index.html");
    }

}
