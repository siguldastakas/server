package com.siguldastakas.server.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextHelper {

    private static final Logger log = LoggerFactory.getLogger(ContextHelper.class);

    private static final String BASE_CONTEXT_NAME = "java:comp/env/";
    private static InitialContext context;

    public static void setupLocal() {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory");
        System.setProperty("org.osjava.sj.space", BASE_CONTEXT_NAME);
        System.setProperty("org.osjava.sj.delimiter", "/");
        System.setProperty("org.osjava.sj.jndi.shared", "true");
        if (System.getProperty("org.osjava.sj.root") != null) System.setProperty("org.osjava.sj.root", "etc/jndi.properties");
    }

    @SuppressWarnings("unchecked")
    public synchronized static <T> T lookup(String name) throws NamingException {
        if (context == null) context = new InitialContext();
        return (T) context.lookup(BASE_CONTEXT_NAME + name);
    }

}
