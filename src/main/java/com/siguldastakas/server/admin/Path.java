package com.siguldastakas.server.admin;

import spark.Request;

public class Path {

    public static String path(String path) {
        return path((String) null, path);
    }

    public static String path(Request request, String path) {
        return path(request.contextPath(), path);
    }

    public static String path(Request request) {
        return path(request, null);
    }

    public static String path(String context, String path) {
        StringBuilder sb = new StringBuilder();
        if (context != null) sb.append(context);
        if (path != null) sb.append(path);
        return sb.toString();
    }

    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";

    public static final String SERIES = "/series";

}
