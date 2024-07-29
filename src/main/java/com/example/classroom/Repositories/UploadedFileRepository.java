package com.example.classroom.Repositories;

import com.example.classroom.Entities.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Integer> {

}
