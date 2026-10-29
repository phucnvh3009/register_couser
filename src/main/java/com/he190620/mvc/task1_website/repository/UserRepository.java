package com.he190620.mvc.task1_website.repository;

import java.util.Optional;
import com.he190620.mvc.task1_website.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findByGoogleSubject(String googleSubject);
}
