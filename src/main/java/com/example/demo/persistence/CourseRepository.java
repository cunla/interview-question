package com.example.demo.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, Long> {

    @Query(value = "select * from TBL_COURSE where title= ?1", nativeQuery = true)
    List<CourseEntity> findByTitle(String title);
}