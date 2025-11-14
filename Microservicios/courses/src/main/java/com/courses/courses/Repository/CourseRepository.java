package com.courses.courses.Repository;

import com.courses.courses.Models.Courses;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository< Courses, Long> {
}