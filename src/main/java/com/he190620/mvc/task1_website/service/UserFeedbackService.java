package com.he190620.mvc.task1_website.service;

import com.he190620.mvc.task1_website.dto.FeedbackForm;
import com.he190620.mvc.task1_website.entity.UserFeedback;
import com.he190620.mvc.task1_website.entity.Users;
import com.he190620.mvc.task1_website.repository.UserFeedbackRepository;
import com.he190620.mvc.task1_website.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserFeedbackService {
    private final UserFeedbackRepository userFeedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackEmailService feedbackEmailService;

    public void processFeedback(FeedbackForm feedbackForm, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setName(feedbackForm.getName());
        userFeedback.setEmail(feedbackForm.getEmail());
        userFeedback.setSubject(feedbackForm.getSubject());
        userFeedback.setMessage(feedbackForm.getMessage());
        userFeedback.setUser(user);

        userFeedbackRepository.save(userFeedback);

        feedbackEmailService.sendFeedback(feedbackForm);
    }

    public List<UserFeedback> getAllFeedbacks(Long userId) {
        return userFeedbackRepository.findByUserId(userId);
    }

}
