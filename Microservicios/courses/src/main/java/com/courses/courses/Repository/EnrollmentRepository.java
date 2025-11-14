package com.courses.courses.Repository;

import com.courses.courses.Models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Método para la consulta compuesta: evita matrículas duplicadas

    Optional< Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // Método usado para la composición de la API (GET /api/courses/student/{id})
    List< Enrollment> findByStudentId(Long studentId);
}
