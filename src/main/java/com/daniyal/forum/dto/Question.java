package com.daniyal.forum.dto;

import lombok.Data;

@Data
public class Question {

    private long id;
    private String author;
    private String message;
    private Integer replies;

}
