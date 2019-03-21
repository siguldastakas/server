package com.siguldastakas.server;

import com.siguldastakas.server.admin.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;

import java.util.TimeZone;

public class Server implements SparkApplication {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public void init() {
        Admin.init();
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
