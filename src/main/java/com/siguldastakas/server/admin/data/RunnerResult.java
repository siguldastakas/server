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

    public int getTime() {
        return time;
    }

    public int getTimeBehind() {
        return timeBehind;
    }

    public int getPosition() {
        return position;
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
