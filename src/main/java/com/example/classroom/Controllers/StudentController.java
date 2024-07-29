package com.example.classroom.Controllers;

import com.example.classroom.Entities.Classroom;
import com.example.classroom.Entities.Students;
import com.example.classroom.Repositories.ClassroomRepository;
import com.example.classroom.Repositories.StudentsRepository;
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
public class StudentController {
    private StudentsRepository studentsRepository;
    private ClassroomRepository classroomRepository;

    @GetMapping("/students")
    public String students(Model model,
                           HttpSession session) {
        if(ClassroomController.checkLogin(session)) {
            List<Students> studentsList = studentsRepository.findAll();
            model.addAttribute("students", studentsList);
            return "students";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/add-student")
    public String addStudent(HttpSession session,
                              Model model,
                             Students student) {
        if(ClassroomController.checkLogin(session)) {
            model.addAttribute("student", student);
            List<Classroom> classrooms = classroomRepository.findAll();
            model.addAttribute("classrooms", classrooms);
            return "add-student";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/store-student")
    public String storeStudent(Students student){
        studentsRepository.save(student);
        return "redirect:/students";
    }


    @GetMapping("/edit-student/{id}")
    public String editStudents(Model model,
                               Students students,
                               HttpSession session,
                               @PathVariable("id") Integer id) {
        if(ClassroomController.checkLogin(session)) {
            students = studentsRepository.getById(id);
            model.addAttribute("student", students);
            List<Classroom> classrooms = classroomRepository.findAll();
            model.addAttribute("classrooms", classrooms);
            return "edit-student";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/update-student")
    public String updateStudent(Students student){
        studentsRepository.save(student);
        return "redirect:/students";
    }


    @GetMapping("/delete-student/{id}")
    public String deleteStudents(Model model,
                           Students students,
                           HttpSession session,
                           @PathVariable("id") Integer id) {
        if(ClassroomController.checkLogin(session)) {
            students = studentsRepository.getById(id);
            model.addAttribute("student", students);
            List<Classroom> classrooms = classroomRepository.findAll();
            model.addAttribute("classrooms", classrooms);
            return "delete-student";
        } else {
            return "redirect:/students";
        }
    }

    @PostMapping("/erase-student")
    public String deleteStudent(Students student){
        studentsRepository.delete(student);
        return "redirect:/students";
    }
}
