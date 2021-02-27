package com.example.demo.persistence;

import lombok.*;

import javax.persistence.*;

/**
 * Entity class for Course
 */
@Entity
@Table(name = "TBL_REGISTRATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "COURSE_ID")
    private Long courseId;

    @Column(name = "REGISTRATION_DATE")
    private String registrationDate;

    @Column(name = "NAME")
    private String name;

}
