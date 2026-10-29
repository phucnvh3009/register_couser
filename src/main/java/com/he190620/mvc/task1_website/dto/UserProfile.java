package com.he190620.mvc.task1_website.dto;

import com.he190620.mvc.task1_website.entity.Users;

public record UserProfile(Long id, String name, String email) {

    public static UserProfile of(Users user) {
        return new UserProfile(user.getId(), user.getName(), user.getEmail());
    }
}
