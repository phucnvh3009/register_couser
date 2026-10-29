package com.he190620.mvc.task1_website.service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import com.he190620.mvc.task1_website.entity.Users;
import com.he190620.mvc.task1_website.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users register(String name, String email, String password) {
        if (name == null || name.isBlank() || email == null || !email.contains("@")
                || password == null || password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Please enter your name, a valid email and a password of at least 6 characters");
        }
        Users user = new Users(
                name.trim(),
                email.trim().toLowerCase(),
                passwordEncoder.encode(password));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
    }

    public Optional<Users> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Users> authenticate(String email, String password) {
        if (password == null) {
            return Optional.empty();
        }
        return findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Transactional
    public Users loginWithGoogle(String subject, String email, String name) {

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Missing Google account ID");
        }

        Optional<Users> existingGoogleUser =
                userRepository.findByGoogleSubject(subject);

        if (existingGoogleUser.isPresent()) {
            return existingGoogleUser.get();
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Google did not provide an email");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException(
                    "This email already belongs to an existing account");
        }

        String displayName = name == null || name.isBlank()
                ? normalizedEmail
                : name.trim();

        String randomPasswordHash =
                passwordEncoder.encode(UUID.randomUUID().toString());

        Users user = new Users(
                displayName,
                normalizedEmail,
                randomPasswordHash);

        user.setGoogleSubject(subject);

        return userRepository.saveAndFlush(user);
    }
}
