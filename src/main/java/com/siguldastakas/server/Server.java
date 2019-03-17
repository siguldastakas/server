package com.siguldastakas.server;

import com.siguldastakas.server.admin.AuthController;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;
import spark.template.freemarker.FreeMarkerEngine;

import javax.naming.NamingException;
import java.util.TimeZone;

import static spark.Spark.*;

public class Server implements SparkApplication {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public void init() {
        try {
            String prefix = ContextHelper.lookup("adminPrefix");

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
            configuration.setClassForTemplateLoading(Server.class, "/freemarker/admin");
            FreeMarkerEngine freemarker = new FreeMarkerEngine(configuration);

            get(prefix + "/login", AuthController.form, freemarker);
            post(prefix + "/login", AuthController.login);
        } catch (NamingException e) {
            log.error("Failed to setup admin panel!", e);
        }
    }

    public void destroy() {

    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        ContextHelper.setupLocal();

        Server service = new Server();
        service.init();
    }


}
