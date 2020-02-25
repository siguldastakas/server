package com.siguldastakas.server.admin.data;

import com.siguldastakas.server.admin.iofxml.Organization;
import com.siguldastakas.server.admin.iofxml.Person;

public class Runner {

    public String name;
    public String club;

    public Person personXml;
    public Organization organizationXml;

    public RunnerResult[] results;
    public RunnerResult overall;

    public int points;

    public String getName() {
        return name;
    }

    public String getClub() {
        return club;
    }

    public RunnerResult[] getResults() {
        return results;
    }

    public RunnerResult getOverall() {
        return overall;
    }

    public int getPoints() {
        return points;
    }
}
