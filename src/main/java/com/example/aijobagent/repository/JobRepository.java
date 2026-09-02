package com.example.aijobagent.repository;

import com.example.aijobagent.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByTechnologyContainingIgnoreCaseAndLocationContainingIgnoreCase(String technology, String location);

    Page<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndTechnologyContainingIgnoreCase(
            String title, String location, String technology, Pageable pageable);
}
