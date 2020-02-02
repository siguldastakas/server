package com.siguldastakas.server.admin.data.overall;

public class OverallClass {

    public String name;
    public OverallRunner[] runners;

    public OverallClass(String name, OverallRunner[] runners) {
        this.name = name;
        this.runners = runners;
    }

    public String getName() {
        return name;
    }

    public OverallRunner[] getRunners() {
        return runners;
    }

}
