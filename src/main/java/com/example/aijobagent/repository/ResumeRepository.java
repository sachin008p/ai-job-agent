package com.example.aijobagent.repository;

import com.example.aijobagent.model.Resume;
import com.example.aijobagent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByUserId(Long userId);

    Optional<Resume> findByUser(User user);
}
