package com.siguldastakas.server.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class TimeDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (map != null) {
            Writer out = env.getOut();
            Object param = map.get("seconds");
            if (param instanceof SimpleNumber) {
                int time = ((SimpleNumber) param).getAsNumber().intValue();
                int hours = time / 3600;
                int minutes = time / 60 - hours * 60;
                int seconds = time % 60;
                out.write(hours > 0
                        ? String.format("%d:%02d:%02d", hours, minutes, seconds)
                        : String.format("%d:%02d", minutes, seconds));
            } else {
                out.write(param.toString());
            }
        }
    }
}
