package com.siguldastakas.server.admin.view;

import com.siguldastakas.server.admin.Path;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface ViewRoute extends TemplateViewRoute {

    class Model {

        private Map<String, Object> data = new HashMap<>();
        private String template;

        public void put(String key, Object value) {
            data.put(key, value);
        }

        public void template(String template) {
            this.template = template;
        }

    }

    @Override
    default ModelAndView handle(Request req, Response res) throws Exception {

        Model model = new Model();

        Map<String, Object> user = new HashMap<>();
        user.put("email", req.session().attribute("email"));
        user.put("name", req.session().attribute("name"));
        model.put("user", user);

        model.put("context", Path.path(req));
        model.put("home", Path.path(req, Path.SERIES));
        model.put("logout", Path.path(req, Path.LOGOUT));

        handle(model, req, res);

        return new ModelAndView(model.data, model.template);
    }

    void handle(Model model, Request req, Response res);
}
