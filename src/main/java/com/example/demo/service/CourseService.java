package com.example.demo.service;

import com.example.demo.controller.data.*;
import com.example.demo.exception.CourseDoesNotExistException;
import com.example.demo.exception.InvalidRegistrationException;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.persistence.CourseEntity;
import com.example.demo.persistence.CourseRepository;
import com.example.demo.persistence.RegisterEntity;
import com.example.demo.persistence.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegisterRepository registerRepository;

    public CourseDetails createCourse(CourseRequest courseRequest) {
        CourseEntity courseEntity = populateCourse(courseRequest);

        CourseDetails courseDetails = new CourseDetails();
        courseDetails.setId(String.valueOf(courseEntity.getId()));
        courseDetails.setTitle(courseEntity.getTitle());
        courseDetails.setStartDate(courseEntity.getStartDate());
        courseDetails.setEndDate(String.valueOf(courseEntity.getEndDate()));
        courseDetails.setCapacity(courseEntity.getCapacity());
        courseDetails.setRemaining(courseEntity.getRemainingCapacity());

        return courseDetails;
    }

    private CourseEntity populateCourse(CourseRequest courseRequest) {
        CourseEntity entity = new CourseEntity();
        try {

            entity.setTitle(courseRequest.getTitle());
            entity.setStartDate(String.valueOf(courseRequest.getStartDate()));
            entity.setEndDate(String.valueOf(courseRequest.getEndDate()));
            entity.setCapacity(courseRequest.getCapacity());
            entity.setRemainingCapacity(courseRequest.getRemaining());
            courseRepository.save(entity);
        } catch (Exception e) {
            throw new InvalidRequestException("Course cannot be created");
        }
        return entity;
    }

    public List<CourseParticipantDetails> searchCourseByTitle(String title) {

        List<CourseEntity> coursesById = courseRepository.findByTitle(title);
        List<CourseParticipantDetails> courseParticipantList = new ArrayList<>();

        coursesById.forEach(course -> populateCourseRegistrationDetails(courseParticipantList, course, null));
        return courseParticipantList;
    }


    public List<CourseParticipantDetails> searchCourseById(long id) {
        Optional<CourseEntity> coursesById = courseRepository.findById(id);
        List<CourseParticipantDetails> courseParticipantList = new ArrayList<>();

        if (coursesById.isPresent()) {
            populateCourseRegistrationDetails(courseParticipantList, coursesById.get(), null);
        }
        return courseParticipantList;
    }


    public List<CourseParticipantDetails> register(long id, RegisterRequest registerRequest) throws ParseException {

        RegisterEntity registerByIdName = registerRepository.findByName(registerRequest.getCourseId(), registerRequest.getName());
        Optional<CourseEntity> coursesById = courseRepository.findById(Long.valueOf(registerRequest.getCourseId()));

        if (!coursesById.isPresent()) {
            throw new CourseDoesNotExistException("Course does not exist");
        } else if (registerByIdName != null && registerByIdName.getName().equals(registerRequest.getName())) {
            throw new InvalidRegistrationException("Participant Already registered to Course");
        } else if (!(coursesById.get().getRemainingCapacity() > 0)) {
            throw new InvalidRegistrationException("Course is full");
        } else {
            dateValidation(coursesById, registerRequest.getRegistrationDate(), "Registration date  is 3 or more days before or after course start date");
            RegisterEntity registerEntity = new RegisterEntity();
            registerEntity.setCourseId(Long.valueOf(registerRequest.getCourseId()));
            registerEntity.setRegistrationDate(String.valueOf(registerRequest.getRegistrationDate()));
            registerEntity.setName(registerRequest.getName());
            registerRepository.save(registerEntity);
            coursesById.get().setRemainingCapacity(coursesById.get().getRemainingCapacity() - 1);
            courseRepository.save(coursesById.get());
        }

        List<CourseParticipantDetails> courseParticipantList = new ArrayList<>();
        coursesById.ifPresent(courseEntity -> populateCourseRegistrationDetails(courseParticipantList, courseEntity, null));
        return courseParticipantList;

    }


    public List<CourseParticipantDetails> unregister(long id, UnRegisterRequest unRegisterRequest) throws ParseException {

        RegisterEntity unRegisterByIdName = registerRepository.findByName(unRegisterRequest.getCourseId(), unRegisterRequest.getName());
        Optional<CourseEntity> coursesById = courseRepository.findById(Long.valueOf(unRegisterRequest.getCourseId()));

        if (!coursesById.isPresent()) {
            throw new CourseDoesNotExistException("Course does not exist");
        } else if (unRegisterByIdName == null) {
            throw new CourseDoesNotExistException("User not registered for this course");
        } else {
            dateValidation(coursesById, unRegisterRequest.getCancelDate(), "Cancellation date is 3 or more days before or after course start date");
            RegisterEntity registerEntity = new RegisterEntity();
            registerEntity.setCourseId(Long.valueOf(unRegisterRequest.getCourseId()));
            registerEntity.setRegistrationDate(String.valueOf(unRegisterRequest.getCancelDate()));
            registerEntity.setName(unRegisterByIdName.getName());
            registerRepository.delete(unRegisterByIdName);
            coursesById.get().setRemainingCapacity(coursesById.get().getRemainingCapacity() + 1);
            courseRepository.save(coursesById.get());
        }

        List<CourseParticipantDetails> courseParticipantList = new ArrayList<>();
        coursesById.ifPresent(courseEntity -> populateCourseRegistrationDetails(courseParticipantList, courseEntity, unRegisterRequest.getName()));
        return courseParticipantList;
    }

    private void dateValidation(Optional<CourseEntity> coursesById, LocalDate date, String s) throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBefore = myFormat.parse(coursesById.get().getStartDate());
        Date dateAfter = myFormat.parse(String.valueOf(date));

        long difference = dateAfter.getTime() - dateBefore.getTime();
        float daysBetween = (difference / (1000 * 60 * 60 * 24));
        int round = Math.round(daysBetween);
        if (round == 3 || round == -3) {
            throw new InvalidRegistrationException(s);
        }
    }

    private void populateCourseRegistrationDetails(List<CourseParticipantDetails> courseParticipantList, CourseEntity course, String name) {
        CourseParticipantDetails courseParticipantDetails = new CourseParticipantDetails();
        courseParticipantDetails.setId(String.valueOf(course.getId()));
        courseParticipantDetails.setTitle(course.getTitle());
        courseParticipantDetails.setStartDate(course.getStartDate());
        courseParticipantDetails.setEndDate(course.getEndDate());
        courseParticipantDetails.setCapacity(course.getCapacity());
        courseParticipantDetails.setRemaining(course.getRemainingCapacity());

        List<RegisterDetails> registerDetailList = new ArrayList<>();
        List<RegisterEntity> registerEntityList = registerRepository.findByCourseId(course.getId());
        if (name != null) {

            boolean noneMatch = registerEntityList.stream().noneMatch(p1 -> name.equals(p1.getName()));
            if (noneMatch) {
                registerEntityList.forEach(registerEntity -> {
                    populateParticipants(registerDetailList, registerEntity);
                    courseParticipantDetails.setParticipants(registerDetailList);
                });
            }
        } else {
            registerEntityList.forEach(registerEntity -> {
                populateParticipants(registerDetailList, registerEntity);
                courseParticipantDetails.setParticipants(registerDetailList);
            });
        }
        courseParticipantList.add(courseParticipantDetails);
    }

    private void populateParticipants(List<RegisterDetails> registerDetailList, RegisterEntity registerEntity) {
        RegisterDetails registerDetails = new RegisterDetails();
        registerDetails.setName(registerEntity.getName());
        registerDetails.setRegistrationDate(registerEntity.getRegistrationDate());
        registerDetailList.add(registerDetails);
    }
}
