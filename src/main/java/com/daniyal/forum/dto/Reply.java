package com.daniyal.forum.dto;

import lombok.Data;

@Data
public class Reply {

    private Long questionId;
    private long id;
    private String author;
    private String message;

}
