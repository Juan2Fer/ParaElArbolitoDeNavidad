package com.students.students.Controller;


import com.students.students.Services.StudentService;
import com.students.students.Students;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students") // <--- Ruta Base del Controlador
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    /*
     * Endpoint para crear un estudiante y matricularlo en un curso por defecto.
     * URL: POST < a href="http://localhost:8081/api/students">...< /a>
     */

    @GetMapping("/{id}") // <-- {id} se mapea al parámetro
    public ResponseEntity<Students> getStudent(@PathVariable Long id) {

        Students student = studentService.getStudentById(id);

        // Retorna el estudiante encontrado con código HTTP 200 (OK)
        return ResponseEntity.ok(student);
    }



    @PostMapping // <--- Mapea la solicitud POST a la ruta base /api/students
    public ResponseEntity< Students> createStudent(@RequestBody Students student) {

        // El servicio maneja tanto el guardado en MySQL como la llamada a PostgreSQL
        Students createdStudent = studentService.createStudentAndEnroll(student);

        // Retorna el estudiante creado con un código HTTP 201 (Created)
        return new ResponseEntity< >(createdStudent, HttpStatus.CREATED);
    }

    // Podrías agregar otros métodos aquí (ej: GET /api/students/{id}, GET /api/students)
}
