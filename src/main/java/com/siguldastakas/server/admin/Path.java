package com.siguldastakas.server.admin;

import spark.Request;

public class Path {

    public static String path(String path) {
        return path((String) null, path);
    }

    public static String path(String... path) {
        return path(null, path);
    }

    public static String path(Request request) {
        return path(request, (String[]) null);
    }

    public static String path(Request request, String... path) {
        StringBuilder sb = new StringBuilder();
        if (request != null) {
            String context = request.contextPath();
            if (context != null) sb.append(context);
        }
        if (path != null) for (String item : path) if (item != null) sb.append('/').append(item);
        return sb.toString();
    }

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    public static final String SERIES = "series";

}
