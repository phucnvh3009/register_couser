package com.he190620.mvc.task1_website.config;

import java.io.IOException;

import com.he190620.mvc.task1_website.entity.Users;
import com.he190620.mvc.task1_website.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class GoogleLoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GoogleLoginSuccessHandler.class);

    private final UserService userService;

    public GoogleLoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        try {
            if (!(authentication.getPrincipal() instanceof OidcUser googleUser)) {
                throw new IllegalArgumentException(
                        "Unsupported login provider");
            }

            if (!Boolean.TRUE.equals(googleUser.getEmailVerified())) {
                throw new IllegalArgumentException(
                        "Google email is not verified");
            }

            Users user = userService.loginWithGoogle(
                    googleUser.getSubject(),
                    googleUser.getEmail(),
                    googleUser.getFullName());

            request.getSession(true).setAttribute("userId", user.getId());

            response.sendRedirect(request.getContextPath() + "/home");

        } catch (RuntimeException exception) {
            log.warn(
                    "Google login could not complete: {}",
                    exception.getClass().getSimpleName());

            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(
                    request.getContextPath() + "/login?googleError=true");
        }
    }
}