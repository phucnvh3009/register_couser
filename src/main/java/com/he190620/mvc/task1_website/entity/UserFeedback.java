package com.he190620.mvc.task1_website.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="user_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedback {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, length=250)
    private String email;

    @Column(nullable=false, length=250)
    private String subject;

    @Column(nullable=false, length=500)
    private String message;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private Users user;

    @Column(name="created_at",nullable=false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
