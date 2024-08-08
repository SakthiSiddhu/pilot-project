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
    public ResponseEntity<Assessment> createAssessment(@RequestBody AssessmentRequest assessmentRequest) {
        Assessment createdAssessment = assessmentService.createAssessment(assessmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssessment);
    }

    @GetMapping("/{setName}")
    public ResponseEntity<QuestionResponse> getQuestionsBySetName(@PathVariable("setName") String setName) {
       QuestionResponse questionResponse = assessmentService.getQuestionsSetName(setName);
        if (questionResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questionResponse);
    }

    @PutMapping("/{setName}/question/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable("setName") String setName,
                                               @PathVariable("questionId") Long questionId,
                                               @RequestBody AnswerRequest answers) {
        assessmentService.updateQuestion(setName, questionId,answers);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/{setName}/question/{questionId}")
//    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable("setName") String setName,
//                                                              @PathVariable("questionId") Long questionId) {
//        assessmentService.deleteQuestion(setName, questionId);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Question deleted successfully");
//        return ResponseEntity.ok(response);
//    }

    @DeleteMapping("/{setName}/questions/{questionId}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable("setName") String setName,
                                                              @PathVariable("questionId") Long questionId) {
        Map<String, String> response = assessmentService.deleteQuestion(setName, questionId);
        return ResponseEntity.ok(response);
    }
}