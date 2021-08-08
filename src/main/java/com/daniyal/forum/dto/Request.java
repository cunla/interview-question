package com.daniyal.forum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Request {
    @NotBlank(message = "author cannot be blank")
    private String author;

    @NotBlank(message = "message cannot be blank")
    private String message;
}
