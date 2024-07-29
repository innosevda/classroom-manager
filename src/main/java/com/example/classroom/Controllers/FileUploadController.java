package com.example.classroom.Controllers;

import com.example.classroom.Entities.UploadedFile;
import com.example.classroom.Repositories.UploadedFileRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.classroom.Controllers.ClassroomController.checkLogin;

@Controller
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @GetMapping("/files")
    public String openFileUpload(Model model, HttpSession session) {
        if (checkLogin(session)) {
        List<UploadedFile> files = uploadedFileRepository.findAll();
        model.addAttribute("files", files);
        return "file-upload";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("sectionName") String sectionName,
                                   @RequestParam("notes") String notes,
                                   Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            model.addAttribute("files", uploadedFileRepository.findAll());
            return "redirect:/files";
        }

        try {

            // Save the file to the specified directory
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = file.getOriginalFilename();
            file.transferTo(new File(uploadDir + fileName));

            // Save file information to the database
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFileName(fileName);
            uploadedFile.setSectionName(sectionName);
            uploadedFile.setNotes(notes);
            uploadedFileRepository.save(uploadedFile);

            model.addAttribute("message", "File uploaded successfully: " + fileName);
            model.addAttribute("files", uploadedFileRepository.findAll());
            return "redirect:/files";
        } catch (IOException e) {
            model.addAttribute("message", "File upload failed: " + e.getMessage());
            return "redirect:/files";
        }

    }
}
