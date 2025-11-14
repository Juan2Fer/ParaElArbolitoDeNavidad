package com.students.students.Repository;

import com.students.students.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Students, Long> {
    // Spring Data JPA proveerá automáticamente métodos como save(), findById(), findAll(), etc.
}
