package com.students.students.Services;

import com.students.students.Students;
import lombok.Data;

import java.util.List;

@Data
public class StudentWithCoursesResponse {
    private Long id;
    private String firstName;
    private String lastName;

    // Lista de cursos donde está matriculado
    private List< CourseDTO> courses;

    public StudentWithCoursesResponse(Students student, List< CourseDTO> courses) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.courses = courses;
    }
}
