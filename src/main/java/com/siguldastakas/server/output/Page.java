package com.siguldastakas.server.output;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Page {

    protected static void process(Object model, String templateName, Path outputPath, String file) throws IOException {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setClassForTemplateLoading(Page.class, "/freemarker/output");
            cfg.setDefaultEncoding("UTF-8");

            Template template = cfg.getTemplate(templateName);

            Writer out = new OutputStreamWriter(new FileOutputStream(outputPath.resolve(file).toFile()));
            template.process(model, out);

            out.flush();
            out.close();

            copyFile(outputPath, "main.css");
            copyFile(outputPath, "logo-1x.png");
            copyFile(outputPath, "logo-2x.png");
            copyFile(outputPath, "favicon.ico");
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }

    protected static void copyFile(Path outputPath, String name) throws IOException {
        ClassLoader classLoader = Page.class.getClassLoader();
        String fullName = "static/output/" + name;
        File sourceFile = new File(classLoader.getResource(fullName).getFile());
        Path targetPath = outputPath.resolve(name);
        File targetFile = targetPath.toFile();

        if (!targetFile.exists() || targetFile.lastModified() < sourceFile.lastModified()) {
            InputStream in = classLoader.getResourceAsStream(fullName);
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }
    }

}
