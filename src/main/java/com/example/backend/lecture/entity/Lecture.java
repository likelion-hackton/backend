package com.example.backend.lecture.entity;

import com.example.backend.participant.entity.Participant;
import com.example.backend.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Lecture")
@SuperBuilder(toBuilder = true)
@DiscriminatorColumn(name = "lecture_type")
@Getter
@Setter
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

    @NotNull
    private int member_limit;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    // 위도
    @NotNull
    private Double latitude;

    // 경도
    @NotNull
    private Double longitude;

    @NotBlank
    private String address;

    private String detailAddress;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Category category;

    @JsonManagedReference
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participant> participants;

    @JsonManagedReference
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LectureImage> lectureImages;

    public void addImage(LectureImage image) {
        if (lectureImages == null) {
            lectureImages = new ArrayList<>();
        }
        lectureImages.add(image);
        image.setLecture(this);
    }
}
