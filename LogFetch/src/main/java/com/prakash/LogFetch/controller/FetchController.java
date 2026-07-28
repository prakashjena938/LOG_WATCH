package com.prakash.LogFetch.controller;

import com.prakash.LogFetch.dto.LogRequest;
import com.prakash.LogFetch.model.AnalysisResult;
import com.prakash.LogFetch.repo.LogRepo;
import com.prakash.LogFetch.service.FetchLogs;
import com.prakash.LogFetch.service.LogAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class FetchController {

    private final FetchLogs fetchLogs;
    private final LogAnalysis logAnalysis;
    private final LogRepo logRepo;

    @PostMapping("/analyse")
    public ResponseEntity<?> fetch(@RequestBody LogRequest request, Authentication authentication) {
        try {
            String rawLogs = fetchLogs.fetch(request);
            AnalysisResult result = logAnalysis.analysis(rawLogs, request);
            result.setRequestedBy(authentication.getName());
            logRepo.save(result);
            return ResponseEntity.ok(result);
        } catch (IllegalAccessException x) {
            return ResponseEntity.badRequest().body(x.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("SSH error: " + e.getMessage());
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> show(Authentication authentication) {
        return logRepo.findTopByRequestedByOrderByIdDesc(authentication.getName())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("message", "No analysis found. Call POST /api/log/analyse first.")));
    }
}
