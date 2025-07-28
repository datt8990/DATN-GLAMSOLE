package com.be.server.core.permitall.thanhtoan.model.Respones;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListSP {

    @JsonProperty("id")
    private String id;
    @JsonProperty("quantity")
    private Integer quantity;

}
