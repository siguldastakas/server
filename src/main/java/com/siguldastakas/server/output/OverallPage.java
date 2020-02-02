package com.siguldastakas.server.output;

import com.siguldastakas.server.admin.data.Event;
import com.siguldastakas.server.admin.data.EventResults;
import com.siguldastakas.server.admin.data.Series;
import com.siguldastakas.server.admin.data.overall.OverallResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class OverallPage extends Page {

    public static void process(Series series, OverallResults results, Path outputPath) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("prefix", series.name);
        model.put("title", "Kopvērtējums – " + series.name + " – Siguldas Takas");
        model.put("events", series.events);
        model.put("classes", results.classes);
        Page.process(model, "overall.ftl", outputPath, "overall.html");
    }

}
