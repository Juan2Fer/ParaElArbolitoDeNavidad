package com.students.students;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Students {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private Long courseId;
}
