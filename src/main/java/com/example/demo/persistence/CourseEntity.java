package com.example.demo.persistence;

import lombok.*;

import javax.persistence.*;

/**
 * Entity class for Course
 */
@Entity
@Table(name = "TBL_COURSE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "REMAINING_CAPACITY")
    private Integer remainingCapacity;

}
