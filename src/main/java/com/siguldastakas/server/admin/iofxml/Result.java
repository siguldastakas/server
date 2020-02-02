package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Result {

    @JacksonXmlProperty(isAttribute = true)
    public String raceNumber;

    @JsonProperty("Time")
    public int time;

    @JsonProperty("TimeBehind")
    public int timeBehind;

    @JsonProperty("Position")
    public int position;

    @JsonProperty("Status")
    public String status;

    @JsonProperty("OverallResult")
    public OverallResult overallResult;

    @JsonProperty("Course")
    public Course course;

}
