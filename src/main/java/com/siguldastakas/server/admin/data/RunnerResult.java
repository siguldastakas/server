package com.siguldastakas.server.admin.data;

public class RunnerResult implements Comparable<RunnerResult> {

    public enum Status {
        OK, MP, DSQ, DNS
    }

    public Status status;
    public int time;
    public int timeBehind;
    public int position;

    public Status getStatus() {
        return status;
    }

    public String getTime() {
        return formatTime(time);
    }

    public String getTimeBehind() {
        return formatTime(timeBehind);
    }

    public int getPosition() {
        return position;
    }

    private static String formatTime(int time) {
        if (time == 0) return null;
        int hours = time / 3600;
        int minutes = time / 60 - hours * 60;
        int seconds = time % 60;
        return hours > 0
                ? String.format("%d:%02d:%02d", hours, minutes, seconds)
                : String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public int compareTo(RunnerResult o) {
        if (status.ordinal() < o.status.ordinal()) {
            return -1;
        } else if (status.ordinal() > o.status.ordinal()) {
            return 1;
        } else if (time < o.time) {
            return -1;
        } else if (time > o.time) {
            return 1;
        }
        return 0;
    }

}
