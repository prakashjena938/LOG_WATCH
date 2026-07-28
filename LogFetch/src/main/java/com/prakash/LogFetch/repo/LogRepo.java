package com.prakash.LogFetch.repo;

import com.prakash.LogFetch.model.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogRepo extends JpaRepository<AnalysisResult, Integer> {
    Optional<AnalysisResult> findTopByRequestedByOrderByIdDesc(String requestedBy);
}
