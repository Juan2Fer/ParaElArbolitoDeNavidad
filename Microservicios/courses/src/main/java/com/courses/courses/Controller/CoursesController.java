package com.courses.courses.Controller;

import com.courses.courses.Models.Enrollment;
import com.courses.courses.Services.CourseService;
import com.students.students.Services.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesController {
    // Inyección del servicio
    private final CourseService courseService;

    // Este endpoint recibe la solicitud del Students-Service
    @PostMapping("/enroll")
    public ResponseEntity< String> enrollStudent(@RequestBody Enrollment enrollment) {

        // ¡USO DE enrollStudent EN EL SERVICIO!
        courseService.enrollStudent(enrollment);

        return ResponseEntity.ok("Estudiante matriculado o ya existe el registro.");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity< List< CourseDTO>> getCoursesByStudentId(@PathVariable Long studentId) {

        List<CourseDTO> courses = courseService.getCoursesByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }
}
