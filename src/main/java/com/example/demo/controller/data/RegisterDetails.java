package com.example.demo.controller.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * RegisterDetails
 */
@Data
public class RegisterDetails {

    @ApiModelProperty(notes = "Participant Name", example = "David")
    private String name;

    @ApiModelProperty(notes = "Registration Date", example = "2021-05-01")
    private String registrationDate;

}
