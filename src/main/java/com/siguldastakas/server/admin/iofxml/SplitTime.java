package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SplitTime {

    @JsonProperty("ControlCode")
    public int controlCode;

    @JsonProperty("Time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer time;

    @JacksonXmlProperty(isAttribute = true)
    public String status;

}
