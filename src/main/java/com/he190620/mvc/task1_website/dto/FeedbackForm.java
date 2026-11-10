package com.he190620.mvc.task1_website.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackForm {
    private String name;
    private String email;
    private String subject;
    private String message;

    public FeedbackForm() {
    }

}
