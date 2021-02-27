package com.example.demo.controller.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * RegisterRequest
 */
@Data
public class RegisterRequest {

    @ApiModelProperty(notes = "Course Id", example = "1")
    private String courseId;

    @ApiModelProperty(notes = "Course registration date", example = "2021-05-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @ApiModelProperty(notes = "Person name registering for course", example = "10")
    private String name;

}
