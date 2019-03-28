package com.siguldastakas.server.output;

import com.siguldastakas.server.admin.data.Series;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class IndexPage extends Page {

    public static void process(Series[] series, Path outputPath) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("series", series);
        model.put("title", "Rezultāti – Siguldas Takas");
        process(model, "index.ftl", outputPath, "index.html");
    }

}
