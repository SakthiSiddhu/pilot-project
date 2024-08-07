package com.example.AssessmentService.controller;

import com.example.AssessmentService.dto.AssessmentDTO;
import com.example.AssessmentService.model.Assessment;
import com.example.AssessmentService.dto.*;
import com.example.AssessmentService.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping
    public ResponseEntity<List<AssessmentDTO>> getAllAssessments() {
        List<AssessmentDTO> assessments = assessmentService.getAllAssessments();
        return ResponseEntity.ok(assessments);
    }

    @PostMapping
    public ResponseEntity<AssessmentDTO> createAssessment(@RequestBody AssessmentRequest assessmentRequest) {
        AssessmentDTO createdAssessment = assessmentService.createAssessment(assessmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssessment);
    }

    @GetMapping("/{setName}")
    public ResponseEntity<AssessmentResponse> getAssessmentBySetName(@PathVariable("setName") String setName) {
        AssessmentResponse assessmentResponse = assessmentService.getAssessmentBySetName(setName);
        if (assessmentResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assessmentResponse);
    }

    @PutMapping("/{setName}/question/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable("setName") String setName,
                                               @PathVariable("questionId") Long questionId,
                                               @RequestBody com.example.AssessmentService.dto.QuestionRequest questionRequest) {
        assessmentService.updateQuestion(setName, questionId, questionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{setName}/question/{questionId}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable("setName") String setName,
                                                              @PathVariable("questionId") Long questionId) {
        assessmentService.deleteQuestion(setName, questionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Question deleted successfully");
        return ResponseEntity.ok(response);
    }
}