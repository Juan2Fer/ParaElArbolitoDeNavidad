package com.students.students;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Students {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    // ... otros campos
}
