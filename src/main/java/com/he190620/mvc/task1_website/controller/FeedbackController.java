package com.he190620.mvc.task1_website.controller;

import com.he190620.mvc.task1_website.dto.FeedbackForm;
import com.he190620.mvc.task1_website.service.UserFeedbackService;
import com.he190620.mvc.task1_website.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final UserService userService;
    private final UserFeedbackService userFeedbackService;

    @GetMapping
    public String showFeedbackForm(Model model, HttpSession session) {
        FeedbackForm feedbackForm = new FeedbackForm();
        Long userId = (Long) session.getAttribute("userId");
        if(userId != null) {
            userService.findById(userId).ifPresent(user -> {
                feedbackForm.setName(user.getName());
                feedbackForm.setEmail(user.getEmail());
            });
        }

        model.addAttribute("feedback", feedbackForm);

        return "feedback";
    }

    @GetMapping("/list")
    public String showFeedbackList(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Long  userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("message", "You need to be logged in to view list feedback.");

            return "redirect:/login";
        }

        model.addAttribute("feedbacks", userFeedbackService.getAllFeedbacks(userId));

        return "feedback-list";
    }

    @PostMapping("/send")
    public String sendFeedback(@ModelAttribute("feedback") FeedbackForm feedback,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in before sending feedback.");

            return "redirect:/login";
        }

        try {
            userFeedbackService.processFeedback(feedback, userId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Your feedback has been sent successfully. Please check your email.");

            return "redirect:/feedback";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed: " + e.getMessage());
            redirectAttributes.addFlashAttribute("feedback", feedback);

            return "redirect:/feedback";
        }
    }
}
