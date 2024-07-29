package com.example.classroom.Controllers;

import com.example.classroom.Entities.Classroom;
import com.example.classroom.Repositories.ClassroomRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class ClassroomController {
    private ClassroomRepository classroomRepository;

    public static boolean checkLogin(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping("/home")
    public String home(HttpSession session) {
        if (checkLogin(session)) {
            return "home";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/classrooms")
    public String classroom(Model model,
                            HttpSession session) {
        if (checkLogin(session)) {
            List<Classroom> classrooms = classroomRepository.findAll();
            model.addAttribute("classrooms", classrooms);
            return "classrooms";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/addclassrooms")
    public String addClass(Model model,
                           Classroom classroom,
                           HttpSession session) {
        if (checkLogin(session)) {
            model.addAttribute("classroom", classroom);
            return "addclass";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/storeclassrooms")
    public String storeClassrooms(Classroom classroom) {
        classroomRepository.save(classroom);
        return "redirect:/classrooms";
    }


    @GetMapping("/editclassroom/{id}")
    public String editClass(Model model,
                            HttpSession session,
                            @PathVariable("id") Integer id,
                            Classroom classroom) {
        if (checkLogin(session)) {
            classroom = classroomRepository.getById(id);
            model.addAttribute("classroom", classroom);
            return "editclass";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/update")
    public String updateClassroom(Classroom classroom) {
        classroomRepository.save(classroom);
        return "redirect:/classrooms";
    }

    @GetMapping("/deleteclassroom/{id}")
    public String editClassroom(@PathVariable("id") Integer id,
                                Model model,
                                HttpSession session,
                                Classroom classroom) {
        if (checkLogin(session)) {
            classroom = classroomRepository.getById(id);
            model.addAttribute("classroom", classroom);
            return "deleteclass";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/erase")
    public String eraseClassroom(Classroom classroom){
        classroomRepository.delete(classroom);
        return "redirect:/classrooms";
    }

}
