package com.example.classroom.Services;

import com.example.classroom.Entities.PasswordResetToken;
import com.example.classroom.Entities.User;
import com.example.classroom.Repositories.PasswordResetTokenRepository;
import com.example.classroom.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetTokenService {

    private PasswordResetTokenRepository tokenRepository;

    private UserRepository userRepository;

    public PasswordResetToken createOrUpdateToken(User user) {
        PasswordResetToken token = tokenRepository.findByUser(user);

        if (token == null) {
            token = new PasswordResetToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
        } else {
            token.setToken(UUID.randomUUID().toString()); // Optionally generate a new token
        }

        token.setExpiryDate(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiry
        tokenRepository.save(token);
        return token;
    }

    public PasswordResetToken getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }

}
