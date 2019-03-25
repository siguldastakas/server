package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class ClassResult {

    @JsonProperty("Class")
    public ClassDetails classDetails;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("PersonResult")
    public PersonResult[] personResults;

}
