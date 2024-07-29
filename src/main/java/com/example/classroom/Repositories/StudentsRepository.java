package com.example.classroom.Repositories;

import com.example.classroom.Entities.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Students, Integer> {

}
