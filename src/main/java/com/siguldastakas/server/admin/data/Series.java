package com.siguldastakas.server.admin.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Series {

    public String path;
    public String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate date;
    public Event[] events;
    public SeriesClass[] classes;

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Event[] getEvents() {
        return events;
    }

    public int indexOfClass(String name) {
        for (int i = 0; i < classes.length; i++) if (classes[i].name.equals(name)) return i;
        return Integer.MAX_VALUE;
    }

    public Event event(int number) {
        for (Event event : events) if (event.number == number) return event;
        return null;
    }

}
