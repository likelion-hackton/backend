package com.example.backend.lecture.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "Lecture")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Long price;

    @NotBlank
    private String type;

    @NotNull
    private int member_limit;

    @NotNull
    private Instant dateTime;

    @NotBlank
    private String location;
}
