package com.example.aijobagent.repository;

import com.example.aijobagent.model.SavedJob;
import com.example.aijobagent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByUserOrderBySavedAtDesc(User user);
    Optional<SavedJob> findByUserAndJobId(User user, Long jobId);
    Optional<SavedJob> findByUserAndExternalJobId(User user, String externalJobId);
}
