package com.siguldastakas.server.admin;

import com.siguldastakas.server.admin.data.DataModel;
import com.siguldastakas.server.admin.data.Event;
import com.siguldastakas.server.admin.view.AuthController;
import com.siguldastakas.server.admin.view.EventController;
import com.siguldastakas.server.admin.view.SeriesController;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;
import spark.template.freemarker.FreeMarkerEngine;

import javax.naming.NamingException;
import java.nio.file.Paths;
import java.util.TimeZone;

import static spark.Spark.*;
import static spark.Spark.get;

public class AdminService implements SparkApplication {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    public void init() {
        staticFiles.location("/static/admin");

        try {
            DataModel.Initialize(Paths.get((String) ContextHelper.lookup("dataPath")));

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
            configuration.setClassForTemplateLoading(AdminService.class, "/freemarker/admin");
            FreeMarkerEngine freemarker = new FreeMarkerEngine(configuration);

            get(Path.path(Path.LOGIN), AuthController.form, freemarker);
            post(Path.path(Path.LOGIN), AuthController.login);
            get(Path.path(Path.LOGOUT), AuthController.logout, freemarker);

            before(Path.path(Path.SERIES), AuthController.filter);
            before(Path.path(Path.SERIES, "*"), AuthController.filter);
            get(Path.path(Path.SERIES), SeriesController.list, freemarker);
            get(Path.path(Path.SERIES, ":path"), SeriesController.view, freemarker);
            get(Path.path(Path.SERIES, ":path", ":event"), EventController.view, freemarker);
            post(Path.path(Path.SERIES, ":path", ":event", "upload"), EventController.upload, freemarker);

        } catch (NamingException e) {
            log.error("Failed to setup admin panel!", e);
        }
    }

    public void destroy() {

    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        ContextHelper.setupLocal();

        AdminService service = new AdminService();
        service.init();
    }


}
