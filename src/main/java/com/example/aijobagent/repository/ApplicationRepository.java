package com.example.aijobagent.repository;

import com.example.aijobagent.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserIdOrderByAppliedAtDesc(Long userId);

    List<Application> findByJobId(Long jobId);

    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);

    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
