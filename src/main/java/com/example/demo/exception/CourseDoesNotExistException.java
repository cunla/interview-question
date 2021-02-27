package com.example.demo.exception;

public class CourseDoesNotExistException extends RuntimeException {

    public CourseDoesNotExistException(String message) {
        super(message);
    }
}