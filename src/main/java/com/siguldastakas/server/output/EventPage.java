package com.siguldastakas.server.output;

import com.siguldastakas.server.admin.data.Event;
import com.siguldastakas.server.admin.data.EventResults;
import com.siguldastakas.server.admin.data.Series;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EventPage extends Page {

    public static void process(Series series, Event event, EventResults results, Path outputPath) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("prefix", series.name + " – " + event.number + ". kārta, " + event.date);
        model.put("name", event.name);
        model.put("title", event.number + ". kārta – " + series.name + " – Siguldas Takas");
        model.put("classes", results.classes);
        Page.process(model, "event.ftl", outputPath, event.number + ".html");
    }

}
