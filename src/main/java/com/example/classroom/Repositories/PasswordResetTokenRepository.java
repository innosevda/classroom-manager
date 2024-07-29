package com.example.classroom.Repositories;

import com.example.classroom.Entities.PasswordResetToken;
import com.example.classroom.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}
