package com.example.classroom.Controllers;

import com.example.classroom.Entities.User;
import com.example.classroom.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class UserController {

    UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/teachers")
    public String teacher(Model model,
                          HttpSession session,
                          User user) {
        if (ClassroomController.checkLogin(session)) {
            model.addAttribute(user);
            return "teacher";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/edit-teacher/{id}")
    public String editTeacher(Model model, HttpSession session, @PathVariable("id") Integer id) {
        if (ClassroomController.checkLogin(session)) {
            User user = userRepository.getById(id);
            model.addAttribute("teacher", user);
            return "edit-teacher";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/update-teacher")
    public String updateTeacher(@ModelAttribute("teacher") User user, HttpSession session) {
        User existingUser = userRepository.getById(user.getId());

        // Update only the non-password fields
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setUsername(user.getUsername());
        User teacher = userRepository.save(existingUser);

        //updating data on the session
        session.removeAttribute("user");
        session.setAttribute("user", teacher);

        return "redirect:/teachers";
    }


    @GetMapping("/delete-teacher/{id}")
    public String deleteTeacher(Model model,
                                HttpSession session,
                                User user,
                                @PathVariable("id") Integer id) {
        user = userRepository.getById(id);
        model.addAttribute("teacher", user);
        return "delete-teacher";
    }

    @PostMapping("/erase-teacher")
    public String eraseTeacher(User user) {
        userRepository.delete(user);
        return "redirect:/";
    }


    @GetMapping("/edit-password")
    public String changePassword(Model model){
        return "change-password";
    }


    @PostMapping("/change")
    public String change(@RequestParam("password") String password,
                         @RequestParam("userId") Integer userId,
                         HttpSession session) {
        // Fetch the user from the database to ensure we have the most recent data
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            session.setAttribute("passwordError", "User not found.");
            return "change-password";
        }

        if (bCryptPasswordEncoder.matches(password, existingUser.getPassword())) {
            session.setAttribute("passwordError", "Use a different password.");
            return "change-password"; // Make sure this is the correct view name
        } else {
            session.removeAttribute("passwordError");
            existingUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(existingUser);
            return "redirect:/teachers";
        }
    }
}