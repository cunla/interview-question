package com.example.demo.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterRepository extends CrudRepository<RegisterEntity, Long> {

    @Query(value = "select * from TBL_REGISTRATION where COURSE_ID= ?1", nativeQuery = true)
    List<RegisterEntity> findByCourseId(long id);

    @Query(value = "select * from TBL_REGISTRATION where COURSE_ID= ?1 AND NAME= ?2", nativeQuery = true)
    RegisterEntity findByName(String courseId, String name);
}