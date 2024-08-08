package com.example.AssessmentService.repo;

import com.example.AssessmentService.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Assessment findBySetName(String setName);
}