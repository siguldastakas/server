package com.siguldastakas.server.admin.iofxml;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Name {

    @JsonProperty("Family")
    public String family;

    @JsonProperty("Given")
    public String given;

}
