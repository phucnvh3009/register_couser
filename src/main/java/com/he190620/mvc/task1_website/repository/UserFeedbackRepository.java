package com.he190620.mvc.task1_website.repository;

import com.he190620.mvc.task1_website.entity.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Long> {
    List<UserFeedback> findByUserId(Long userId);
}
