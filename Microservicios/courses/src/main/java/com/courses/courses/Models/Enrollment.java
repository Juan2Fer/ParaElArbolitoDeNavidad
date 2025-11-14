package com.courses.courses.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
        name = "enrollment",
        // Define la restricción de unicidad:
        uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "courseId"})
)
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del estudiante obtenido de studentsdb (MySQL)
    private Long studentId;

    // Nombre del estudiante (para referencia local)
    private String studentName;

    // ID del curso local (Coursesdb)
    private Long courseId;
}
