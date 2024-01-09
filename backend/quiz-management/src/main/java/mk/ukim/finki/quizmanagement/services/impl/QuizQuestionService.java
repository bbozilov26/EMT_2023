package mk.ukim.finki.quizmanagement.services.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.quizmanagement.domain.dtos.QuizQuestionCreationDTO;
import mk.ukim.finki.quizmanagement.domain.models.QuizAnswer;
import mk.ukim.finki.quizmanagement.domain.models.exceptions.QuizQuestionNotFoundException;
import mk.ukim.finki.quizmanagement.domain.dtos.QuizAnswerDTO;
import mk.ukim.finki.quizmanagement.domain.dtos.QuizGivenAnswersDTO;
import mk.ukim.finki.quizmanagement.domain.dtos.QuizQuestionDTO;
import mk.ukim.finki.quizmanagement.domain.models.QuizQuestion;
import mk.ukim.finki.quizmanagement.domain.models.QuizQuestionAnswer;
import mk.ukim.finki.quizmanagement.domain.models.ids.QuizQuestionId;
import mk.ukim.finki.quizmanagement.domain.repositories.QuizQuestionRepository;
import mk.ukim.finki.usersmanagement.domain.models.User;
import mk.ukim.finki.usersmanagement.services.impl.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizQuestionAnswerService quizQuestionAnswerService;
    private final UserService userService;

    public List<QuizQuestion> findAll(){
        return quizQuestionRepository.findAll();
    }

    public Optional<QuizQuestion> findById(QuizQuestionId id){
        return Optional.ofNullable(quizQuestionRepository.findById(id).orElseThrow(QuizQuestionNotFoundException::new));
    }

    public QuizQuestion create(QuizQuestionCreationDTO quizQuestionDTO){
        return fillProperties(new QuizQuestion(), quizQuestionDTO);
    }

    public QuizQuestion edit(QuizQuestionId id, QuizQuestionCreationDTO quizQuestionDTO){
        return fillProperties(findById(id).get(), quizQuestionDTO);
    }

    private QuizQuestion fillProperties(QuizQuestion quizQuestion, QuizQuestionCreationDTO quizQuestionDTO){
        quizQuestion.setQuestion(quizQuestionDTO.getQuestion());
        quizQuestion.setReward(quizQuestionDTO.getReward());
        quizQuestion.setTopic(quizQuestionDTO.getTopic());
        quizQuestion.setDifficulty(quizQuestionDTO.getDifficulty());

        quizQuestionRepository.save(quizQuestion);

        List<QuizQuestionAnswer> quizQuestionAnswers = new ArrayList<>();
        quizQuestionDTO.getQuizAnswerDTOs().forEach(quizAnswerDTO ->
                quizQuestionAnswers.add(quizQuestionAnswerService.getOrCreate(quizQuestion, quizAnswerDTO))
        );
        quizQuestion.setQuizQuestionAnswers(quizQuestionAnswers);

        quizQuestion.setCorrectQuizAnswer(quizQuestionAnswerService.getOrCreate(quizQuestion, quizQuestionDTO.getCorrectQuizAnswerDTO()));

        return quizQuestionRepository.save(quizQuestion);
    }

    public void delete(QuizQuestionId id){
        quizQuestionRepository.deleteById(id);
    }

    public User submitQuiz(QuizGivenAnswersDTO quizGivenAnswersDTO){
        Double quizReward = -5.0;

        QuizQuestion quizQuestion = quizQuestionRepository.findById(quizGivenAnswersDTO.getQuestionId()).isPresent() ?
                quizQuestionRepository.findById(quizGivenAnswersDTO.getQuestionId()).get() : null;

        if(quizQuestion != null) {
            String correctAnswerId = quizQuestion.getCorrectQuizAnswer().getQuizAnswer().getId().getId();
            if(quizGivenAnswersDTO.getAnswerId() != null) {
                String givenAnswerId = quizGivenAnswersDTO.getAnswerId().getId();

                if (correctAnswerId.equals(givenAnswerId)) {
                    quizReward += Math.abs(quizReward) + quizQuestion.getReward();
                }
            }
        }

        User user = userService.findById(quizGivenAnswersDTO.getUserId()).get();
        user.setCreditBalance(user.getCreditBalance() + quizReward);
        user.setAnsweredQotD(true);

        return userService.save(user);
    }

    public QuizQuestion getRandom(){
        List<QuizQuestion> quizQuestions = this.findAll();
        long totalQuestions = quizQuestions.size();
        int randomIndex = new Random().nextInt((int) totalQuestions);

        return quizQuestions.get(randomIndex);
    }

    public List<QuizAnswer> getAllAnswersByQuestionId(QuizQuestionId questionId) {
        return quizQuestionAnswerService.getAllAnswersByQuestionId(questionId);
    }
}
