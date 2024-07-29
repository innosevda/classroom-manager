package com.example.classroom.Services;

import com.example.classroom.Entities.PasswordResetToken;
import com.example.classroom.Entities.User;
import com.example.classroom.Repositories.PasswordResetTokenRepository;
import com.example.classroom.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    public void createPasswordResetToken() {
        PasswordResetToken resetToken = new PasswordResetToken();
        tokenRepository.save(resetToken);
    }

    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void updatePassword(User user, String password) {
        user.setPassword(password);
        userRepository.save(user);
    }
}




