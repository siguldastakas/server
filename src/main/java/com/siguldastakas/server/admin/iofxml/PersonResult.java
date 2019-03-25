package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class PersonResult {

    @JsonProperty("Person")
    public Person person;

    @JsonProperty("Organisation")
    public Organization organization;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Result")
    public Result[] results;

}
