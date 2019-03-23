package com.siguldastakas.server.admin.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Series {

    public String path;
    public String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate date;
    public Event[] events;

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Event[] getEvents() {
        return events;
    }

}
