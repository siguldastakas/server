package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OverallResult {

    @JsonProperty("Time")
    public int time;

    @JsonProperty("TimeBehind")
    public int timeBehind;

    @JsonProperty("Position")
    public int position;

    @JsonProperty("Status")
    public String status;

}
