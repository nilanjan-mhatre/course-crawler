package com.frontend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.frontend.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value="select c from Course c where c.title = :name")
    List<Course> findCoursesByName(@Param("name") String name);

    @Query(value="select c from Course c where lower(c.title) = lower(:name) and lower(c.source) = lower(:source)")
    Course getCourseByNameAndSource(@Param("name") String name, @Param("source") String source);
}
