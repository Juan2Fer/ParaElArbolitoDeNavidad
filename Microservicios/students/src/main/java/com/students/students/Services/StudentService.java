package com.students.students.Services;


import com.students.students.Repository.StudentRepository;
import com.students.students.Students;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;


    @Transactional // Para asegurar que el delete (compensación) funcione si hay un error.
    public Students createStudentAndEnroll(Students student) {

        // 1. Guardar el estudiante en studentsdb (MySQL)
        Students savedStudent = studentRepository.save(student);

        // 2. Validación y uso del ID del curso dinámico
        if (savedStudent.getCourseId() == null) {
            throw new IllegalArgumentException("El ID del curso es obligatorio para la matrícula.");
        }

        // 3. Preparar la Matrícula (Llamada síncrona al Courses Service)
        EnrollmentDTO enrollment = new EnrollmentDTO(
                savedStudent.getId(),
                savedStudent.getFirstName() + " " + savedStudent.getLastName(),
                savedStudent.getCourseId() // Usando el ID dinámico
        );

        String url = "http://COURSES-SERVICE/api/courses/enroll";

        try {
            // Envía la solicitud al microservicio de cursos (PostgreSQL)
            restTemplate.postForEntity(url, enrollment, String.class);

        } catch (Exception e) {

            // 4. Lógica de COMPENSACIÓN: Si la matrícula falla, eliminamos el estudiante.
            System.err.println("FALLO en la comunicación con COURSES-SERVICE. Revirtiendo la creación del estudiante...");
            studentRepository.delete(savedStudent);

            // Lanzamos una excepción para que el controlador devuelva un 500
            throw new RuntimeException("Error en matrícula. Estudiante no creado.", e);
        }

        return savedStudent;
    }

    public StudentWithCoursesResponse getStudentWithCourses(Long studentId) {

        // 1. Obtener los datos del estudiante de MySQL
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + studentId));

        // 2. Llamar al Courses Service para obtener los cursos matriculados
        String url = "http://COURSES-SERVICE/api/courses/student/" + studentId;

        // Uso de RestTemplate.exchange() para manejar el retorno de List< CourseDTO>
        ResponseEntity<List< CourseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference< List< CourseDTO>>() {}
        );

        List< CourseDTO> courses = response.getBody();

        // 3. Combinar los datos y retornar la respuesta
        return new StudentWithCoursesResponse(student, courses);
    }

    public Students getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + studentId));
    }
}
