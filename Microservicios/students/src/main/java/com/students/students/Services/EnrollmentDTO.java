package com.students.students.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long studentId;
    private String studentName; // Para fines de demostración
    private Long courseId;      // El curso en el que se matricula
}
