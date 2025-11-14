package com.courses.courses.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Courses {
    @Id
    private Long id;
    private String courseName;
    private String instructor;
    // ... otros campos
}