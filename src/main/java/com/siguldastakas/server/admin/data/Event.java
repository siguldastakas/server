package com.siguldastakas.server.admin.data;

import java.time.LocalDate;

public class Event {

    public int number;
    public LocalDate date;
    public String name;

    public int getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
