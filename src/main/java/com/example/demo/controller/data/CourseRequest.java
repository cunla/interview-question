package com.example.demo.controller.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
 * CourseRequest
 */
@Data
public class CourseRequest {

    @ApiModelProperty(notes = "Course title", example = "Course Title")
    private String title;

    @ApiModelProperty(notes = "End date of course", example = "2021-05-01")
    private LocalDate startDate;

    @ApiModelProperty(notes = "End date of course", example = "2021-05-05")
    private LocalDate endDate;

    @ApiModelProperty(notes = "Total capacity", example = "10")
    private Integer capacity;

    @ApiModelProperty(notes = "Remaining Capacity", example = "10")
    private Integer remaining;

}
