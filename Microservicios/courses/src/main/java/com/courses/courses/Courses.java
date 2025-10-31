package com.courses.courses;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courses {
    @Id
    private Long id;
    private String courseName;
    private String instructor;
    // ... otros campos
}