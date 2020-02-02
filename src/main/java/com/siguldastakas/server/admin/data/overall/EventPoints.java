package com.siguldastakas.server.admin.data.overall;

public class EventPoints {

    public int points;
    public boolean used;

    public EventPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public boolean isUsed() {
        return used;
    }
}
