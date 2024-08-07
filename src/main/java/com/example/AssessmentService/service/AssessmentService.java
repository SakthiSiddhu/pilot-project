package com.example.AssessmentService.service;


import com.example.AssessmentService.dto.AssessmentDTO;
import com.example.AssessmentService.dto.AssessmentRequest;
import com.example.AssessmentService.dto.AssessmentResponse;
import com.example.AssessmentService.exception.ResourceNotFoundException;
import com.example.AssessmentService.model.Answer;
import com.example.AssessmentService.model.Assessment;
import com.example.AssessmentService.model.Question;
import com.example.AssessmentService.dto.QuestionRequest;
import com.example.AssessmentService.repo.AnswerRepository;
import com.example.AssessmentService.repo.AssessmentRepository;
import com.example.AssessmentService.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public AssessmentDTO createAssessment(AssessmentRequest assessmentRequest) {
        Assessment assessment = new Assessment();
        assessment.setSetName(assessmentRequest.getSetName());
        assessment.setDomain(assessmentRequest.getDomain());
        assessment.setCreatedBy(assessmentRequest.getCreatedBy());

        List<Question> questions = new ArrayList<>();
        for (QuestionRequest questionRequest : assessmentRequest.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionRequest.getQuestion());
            question.setAssessment(assessment);
            question.setAnswers(new ArrayList<>()); // Initialize answers as empty list
            questions.add(question);
        }

        assessment.setQuestions(questions);
        Assessment savedAssessment = assessmentRepository.save(assessment);

        return mapToAssessmentDTO(savedAssessment);
    }

    public List<AssessmentDTO> getAllAssessments() {
        List<Assessment> assessments = assessmentRepository.findAll();
        return assessments.stream()
                .map(this::mapToAssessmentDTO)
                .collect(Collectors.toList());
    }

    public AssessmentResponse getAssessmentBySetName(String setName) {
        Assessment assessment = assessmentRepository.findBySetName(setName);
        if (assessment == null) {
            return null;
        }
        AssessmentResponse response = new AssessmentResponse();
        response.setAssessment(mapToAssessmentDTO(assessment));
        response.setQuestions(assessment.getQuestions());
        return response;
    }

    @Transactional
    public void updateQuestion(String setName, Long questionId, QuestionRequest questionRequest) {
        Assessment assessment = assessmentRepository.findBySetName(setName);
        if (assessment == null) {
            throw new ResourceNotFoundException("Assessment not found");
        }

        Question questionToUpdate = assessment.getQuestions().stream()
                .filter(q -> q.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        // Update question text if provided
        if (questionRequest.getQuestion() != null) {
            questionToUpdate.setQuestionText(questionRequest.getQuestion());
        }

        // Add answers if provided
        if (questionRequest.getAnswers() != null) {
            List<Answer> answers = questionRequest.getAnswers().stream()
                    .map(answerRequest -> {
                        Answer answer = new Answer();
                        answer.setOption(answerRequest.getOption());
                        answer.setSuggestion(answerRequest.getSuggestion());
                        answer.setQuestion(questionToUpdate);
                        return answer;
                    })
                    .collect(Collectors.toList());
            questionToUpdate.setAnswers(answers);
        }

        questionRepository.save(questionToUpdate);
    }

    @Transactional
    public void deleteQuestion(String setName, Long questionId) {
        Assessment assessment = assessmentRepository.findBySetName(setName);
        if (assessment == null) {
            throw new ResourceNotFoundException("Assessment not found");
        }

        Question questionToDelete = assessment.getQuestions().stream()
                .filter(q -> q.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        assessment.getQuestions().remove(questionToDelete);
        questionRepository.deleteById(questionToDelete.getQuestionId());
        assessmentRepository.save(assessment);
    }

    private AssessmentDTO mapToAssessmentDTO(Assessment assessment) {
        AssessmentDTO assessmentDTO = new AssessmentDTO();
        assessmentDTO.setSetName(assessment.getSetName());
        assessmentDTO.setDomain(assessment.getDomain());
        assessmentDTO.setCreatedBy(assessment.getCreatedBy());
        // Set additional properties like created_date, modified_date, status if available
        return assessmentDTO;
    }
}


