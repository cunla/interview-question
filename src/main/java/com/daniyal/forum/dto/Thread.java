package com.daniyal.forum.dto;

import lombok.Data;

import java.util.List;

@Data
public class Thread {

    private long id;
    private String author;
    private String message;
    private List<Reply> replies;

}
