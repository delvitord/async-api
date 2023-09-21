package com.tujuhsembilan.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

    private final PasswordEncoder encoder;
    private final Map<String, CompletableFuture<Map<String, String>>> asyncResults = new ConcurrentHashMap<>();

    // endpoint buat mulai tugas asinkronnya.
    @GetMapping("/start")
    public ResponseEntity<String> startAsyncProcess() {
        String processId = UUID.randomUUID().toString();
        CompletableFuture<Map<String, String>> future = new CompletableFuture<>(); // buat CompletableFuture baru.
        asyncResults.put(processId, future); // masukin CompletableFuture dalam map.
        String pollingUrl = "/sample/poll/" + processId; // URL untuk buat cek status.
        startAsyncTask(processId, future); // manggil fungsi async-nya.
        return ResponseEntity.ok(pollingUrl);
    }

    // cek status tugas
    @GetMapping("/poll/{processId}")
    public ResponseEntity<?> pollStatus(@PathVariable String processId) {
        if (asyncResults.containsKey(processId)) {
            CompletableFuture<Map<String, String>> future = asyncResults.get(processId);
            if (future == null) {
                return ResponseEntity.ok("Sedang Diproses...");
            } else if (future.isDone()) {
                try {
                    Map<String, String> results = future.get();
                    asyncResults.remove(processId);
                    return ResponseEntity.ok(results);
                } catch (Exception e) {
                    return ResponseEntity.status(500).body("Terjadi Kesalahan Selama Proses.");
                }
            } else {
                return ResponseEntity.ok("Sedang Diproses...");
            }
        } else {
            return ResponseEntity.status(404).body("Proses Tidak Ditemukan.");
        }
    }

    // buat fungsi async-nya.
    @Async
    private void startAsyncTask(String processId, CompletableFuture<Map<String, String>> future) {
        CompletableFuture.runAsync(() -> {
            Map<String, String> results = generateUuidsAsync();
            future.complete(results); // CompletableFuture dengan results dari tugas.
        });
    }

    // metode buat generate UUID secara asinkron.
    private Map<String, String> generateUuidsAsync() {
        Map<String, String> results = new HashMap<>();
        try {
            while (results.size() < 10000) {
                var uuid = UUID.randomUUID().toString();
                results.put(uuid, encoder.encode(uuid));
            }
        } catch (Exception e) {

        }
        return results;
    }
}