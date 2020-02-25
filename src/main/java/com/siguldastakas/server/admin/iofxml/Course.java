package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {

    @JsonProperty("Id")
    public int id;

    @JsonProperty("Name")
    public String name;

    @JsonProperty("Length")
    public Integer length;

}
