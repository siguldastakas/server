package com.siguldastakas.server.admin.data.overall;

public class OverallRunner {

    public String name;
    public String club;
    public int position;
    public int points;
    public EventPoints[] eventPoints;

    public OverallRunner(String name, int eventsSize) {
        this.name = name;
        this.eventPoints = new EventPoints[eventsSize];
    }

}
