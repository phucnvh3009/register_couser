package com.he190620.mvc.task1_website.controller;

import java.util.Map;
import com.he190620.mvc.task1_website.dto.LoginRequest;
import com.he190620.mvc.task1_website.dto.RegisterUserRequest;
import com.he190620.mvc.task1_website.dto.UserProfile;
import com.he190620.mvc.task1_website.entity.Users;
import com.he190620.mvc.task1_website.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final String SESSION_USER_ID = "userId";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserProfile> register(@RequestBody RegisterUserRequest request) {
        Users user = userService.register(
                request.name(),
                request.email(),
                request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserProfile.of(user));
    }

    @PostMapping("/login")
    public UserProfile login(@RequestBody LoginRequest request, HttpServletRequest http) {
        Users user = userService.authenticate(request.email(), request.password())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        http.getSession(true);
        http.changeSessionId();
        http.getSession().setAttribute(SESSION_USER_ID, user.getId());
        return UserProfile.of(user);
    }

    @GetMapping("/me")
    public UserProfile me(@SessionAttribute(name = SESSION_USER_ID, required = false) Long userId) {
        ResponseStatusException notLoggedIn = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please log in");
        if (userId == null) {
            throw notLoggedIn;
        }
        return userService.findById(userId).map(UserProfile::of).orElseThrow(() -> notLoggedIn);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest http) {
        HttpSession session = http.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> onError(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason() != null ? e.getReason() : e.getMessage()));
    }
}
