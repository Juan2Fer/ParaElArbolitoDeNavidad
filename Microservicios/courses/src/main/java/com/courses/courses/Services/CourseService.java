package com.courses.courses.Services;

import com.courses.courses.Models.Courses;
import com.courses.courses.Models.Enrollment;
import com.courses.courses.Repository.CourseRepository;
import com.courses.courses.Repository.EnrollmentRepository;
import com.students.students.Services.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    // Inyección de Repositorios (PostgreSQL)
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private Enrollment newEnrollment;

    // ------------------------------------------------------------------
    // 1. Lógica para Registrar Matrícula (Recibida del Students Service)
    // ------------------------------------------------------------------

    public Enrollment enrollStudent(Enrollment newEnrollment) {

        // **Validación para evitar duplicados** (Solución a registros duplicados)
        Optional< Enrollment> existingEnrollment = enrollmentRepository.findByStudentIdAndCourseId(
                newEnrollment.getStudentId(),
                newEnrollment.getCourseId()
        );

        if (existingEnrollment.isPresent()) {
            System.out.println("ADVERTENCIA: Intento de matrícula duplicado para Estudiante " + newEnrollment.getStudentId() + " en Curso " + newEnrollment.getCourseId());
            return existingEnrollment.get();
        }

        // Si no existe, guarda el nuevo registro en la tabla 'enrollment'
        return enrollmentRepository.save(newEnrollment);
    }

    // ------------------------------------------------------------------
    // 2. Lógica para Consulta Compuesta (Llamada del Students Service)
    // ------------------------------------------------------------------

    public List<CourseDTO> getCoursesByStudentId(Long studentId) {

        // 1. Buscar las matrículas (relaciones) del estudiante en la tabla Enrollment
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        // 2. Extraer los IDs de los cursos matriculados
        List< Long> courseIds = enrollments.stream()
                .map(Enrollment::getCourseId)
                .collect(Collectors.toList());

        // 3. Buscar los detalles de los cursos en la tabla Courses
        // Se usa findAllById para buscar todos los cursos cuyos IDs están en la lista
        List< Courses> courses = courseRepository.findAllById(courseIds);

        // 4. Mapear las entidades Course a los DTOs de respuesta (CourseDTO)
        return courses.stream()
                .map(course -> new CourseDTO(course.getId(), course.getCourseName(), course.getInstructor()))
                .collect(Collectors.toList());
    }
}
