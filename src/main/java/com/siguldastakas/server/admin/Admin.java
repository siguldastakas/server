package com.siguldastakas.server.admin;

import com.siguldastakas.server.ContextHelper;
import com.siguldastakas.server.Server;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.template.freemarker.FreeMarkerEngine;

import javax.naming.NamingException;

import static spark.Spark.get;
import static spark.Spark.post;

public class Admin {

    private static final Logger log = LoggerFactory.getLogger(Admin.class);

    public static void init() {
        try {
            Path.prefix = ContextHelper.lookup("adminPrefix");

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
            configuration.setClassForTemplateLoading(Server.class, "/freemarker/admin");
            FreeMarkerEngine freemarker = new FreeMarkerEngine(configuration);

            get(Path.path(Path.LOGIN), AuthController.form, freemarker);
            post(Path.path(Path.LOGIN), AuthController.login);
            get(Path.path(Path.LOGOUT), AuthController.logout, freemarker);
        } catch (NamingException e) {
            log.error("Failed to setup admin panel!", e);
        }
    }

}
