package com.tujuhsembilan.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

    private final PasswordEncoder encoder;
    private final Map<String, Future<Map<String, String>>> asyncResults = new ConcurrentHashMap<>();

    @GetMapping("/start")
    public ResponseEntity<String> startAsyncProcess() {
        String processId = UUID.randomUUID().toString();
        asyncResults.put(processId, generateUuidsAsync());
        return ResponseEntity.ok(processId);
    }

    @GetMapping("/status/{processId}")
    public ResponseEntity<?> checkStatus(@PathVariable String processId) {
        if (asyncResults.containsKey(processId)) {
            Future<Map<String, String>> future = asyncResults.get(processId);
            if (future.isDone()) {
                try {
                    Map<String, String> results = future.get();
                    asyncResults.remove(processId); // Remove completed process
                    return ResponseEntity.ok(results);
                } catch (Exception e) {
                    return ResponseEntity.status(500).body("Error occurred during processing.");
                }
            } else {
                return ResponseEntity.ok("Processing...");
            }
        } else {
            return ResponseEntity.status(404).body("Process not found.");
        }
    }

    @Async
    private Future<Map<String, String>> generateUuidsAsync() {
        Map<String, String> results = new HashMap<>();
        while (results.size() < 1000) {
            var uuid = UUID.randomUUID().toString();
            results.put(uuid, encoder.encode(uuid));
        }
        return new AsyncResult<>(results);
    }
}