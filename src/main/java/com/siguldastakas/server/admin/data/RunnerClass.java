package com.siguldastakas.server.admin.data;

public class RunnerClass implements Comparable<RunnerClass> {

    public String name;
    public String[] courses;
    public Runner[] runners;

    public String getName() {
        return name;
    }

    public String[] getCourses() {
        return courses;
    }

    public Runner[] getRunners() {
        return runners;
    }

    @Override
    public int compareTo(RunnerClass o) {
        String age = name.substring(1);
        String oage = o.name.substring(1);
        int compare = age.compareTo(oage);
        return compare == 0 ? name.compareTo(o.name) : compare;
    }
}
