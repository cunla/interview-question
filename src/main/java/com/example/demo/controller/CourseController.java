package com.example.demo.controller;

import com.example.demo.controller.data.*;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.service.CourseService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * Controller class
 */
@Api(value = "/courses", tags = "Course Details")
@RestController
@RequestMapping(produces = "application/json")
public class CourseController {
    private final CourseService courseService;

    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }

    @ApiOperation(value = "Create Course",
            response = CourseDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/courses")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<CourseDetails> createCourse(
            @RequestBody @Validated CourseRequest courseRequest) {

        CourseDetails courseDetails = courseService.createCourse(courseRequest);

        return new ResponseEntity(courseDetails, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Search by course title",
            response = CourseDetails.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(value = "/courses")
    public ResponseEntity<List<CourseParticipantDetails>> searchCourseByTitle(
            @ApiParam(value = "Course title", required = true)
            @RequestParam(value = "q") String q) {

        List<CourseParticipantDetails> courseDetails = courseService.searchCourseByTitle(q);

        return new ResponseEntity<>(courseDetails, HttpStatus.OK);
    }

    @ApiOperation(value = "Search by course id",
            response = CourseParticipantDetails.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(value = "/courses/{id}")
    public ResponseEntity<List<CourseParticipantDetails>> searchCourseById(
            @PathVariable long id) {

        List<CourseParticipantDetails> courseDetails = courseService.searchCourseById(id);

        return new ResponseEntity<>(courseDetails, HttpStatus.OK);
    }

    @ApiOperation(value = "Register for course",
            response = CourseParticipantDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/courses/{id}/add")
    public ResponseEntity<List<CourseParticipantDetails>> registerParticipant(
            @PathVariable long id,
            @RequestBody @Validated RegisterRequest registerRequest) throws ParseException {

        if (!String.valueOf(id).equals(registerRequest.getCourseId()))
            throw new InvalidRequestException("Path variable and request body does not match");

        List<CourseParticipantDetails> register = courseService.register(id, registerRequest);

        return new ResponseEntity<>(register, HttpStatus.OK);
    }

    @ApiOperation(value = "Register for course",
            response = CourseParticipantDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/courses/{id}/remove")
    public ResponseEntity<List<CourseParticipantDetails>> unregisterParticipant(
            @PathVariable long id,
            @RequestBody @Validated UnRegisterRequest unRegisterRequest) throws ParseException {

        if (!String.valueOf(id).equals(unRegisterRequest.getCourseId()))
            throw new InvalidRequestException("Path variable and request body does not match");

        List<CourseParticipantDetails> unregister = courseService.unregister(id, unRegisterRequest);

        return new ResponseEntity<>(unregister, HttpStatus.OK);
    }
}
