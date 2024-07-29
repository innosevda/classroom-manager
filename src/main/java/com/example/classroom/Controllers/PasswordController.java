package com.example.classroom.Controllers;

import com.example.classroom.Entities.PasswordResetToken;
import com.example.classroom.Entities.User;
import com.example.classroom.Repositories.PasswordResetTokenRepository;
import com.example.classroom.Repositories.UserRepository;
import com.example.classroom.Services.EmailService;
import com.example.classroom.Services.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@AllArgsConstructor
public class PasswordController {


    private UserRepository userRepository;

    private PasswordResetTokenService tokenService;

    private EmailService emailService;

    private PasswordResetTokenRepository tokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/forgot-password")
    public String displayForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {

        User user = userRepository.findByUsername(email);
        if (user == null) {
            model.addAttribute("error", "Invalid email address. User does not exist.");
            return "forgot-password";
        }

        PasswordResetToken token = tokenService.createOrUpdateToken(user);
        emailService.sendPasswordResetEmail(user.getUsername(), token.getToken());

        model.addAttribute("message", "Check your email. Password reset email has been sent.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPwd(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "redirect:/forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password";

    }

    @PostMapping("/reset")
    public String reset(@RequestParam("password") String password,
                        @RequestParam("token") String token,
                        User user,
                        Model model) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password";
        }


        user = resetToken.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        tokenService.deleteToken(resetToken);

        model.addAttribute("message", "Password successfully reset.");
        return "redirect:/login";  // Redirect to login page or another appropriate page
    }

}
