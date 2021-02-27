package com.example.demo.controller.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * CourseParticipantDetails
 */
@Data
public class CourseParticipantDetails {

    @ApiModelProperty(notes = "Course Id", example = "1")
    private String id;

    @ApiModelProperty(notes = "Course title", example = "Course Title")
    private String title;

    @ApiModelProperty(notes = "End date of course", example = "2021-05-01")
    private String startDate;

    @ApiModelProperty(notes = "End date of course", example = "2021-05-05")
    private String endDate;

    @ApiModelProperty(notes = "Total capacity", example = "10")
    private Integer capacity;

    @ApiModelProperty(notes = "Remaining Capacity", example = "10")
    private Integer remaining;

    @ApiModelProperty(notes = "List of participants registered for course")
    private List<RegisterDetails> participants;
}
