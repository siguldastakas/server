package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class ResultList {

    @JsonProperty("Event")
    public Event event;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("ClassResult")
    public ClassResult[] classResults;

}
