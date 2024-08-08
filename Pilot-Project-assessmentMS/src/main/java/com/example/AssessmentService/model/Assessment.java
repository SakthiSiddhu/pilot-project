package com.example.AssessmentService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setid")
    private Long setid;

    @Column(name = "set_name",unique = true)
    private String setName;

    private String domain;

    @Column(name = "created_by")
    private String createdBy;

    private String createdDate;

    private String modifiedDate;

    @Enumerated(EnumType.STRING)
    private SetStatus status;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private List<Question> questions;
}
