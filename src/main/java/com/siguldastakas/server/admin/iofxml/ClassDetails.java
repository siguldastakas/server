package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassDetails {

    @JsonProperty("Id")
    public Integer id;

    @JsonProperty("Name")
    public String name;

}
