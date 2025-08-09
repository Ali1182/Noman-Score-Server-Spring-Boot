package com.server_ns.nomad_score.controller.supabase;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.server_ns.nomad_score.service.SupabaseService;

@RestController
public class SupabaseSpeedTest {
    
    @Autowired
    private SupabaseService supabaseService;
    
    @PostMapping("/supabase/insert/speedtest")
    public ResponseEntity<?> insertSpeedTest(@RequestBody Map<String, Object> speedTestData) {
        try {
            String result = supabaseService.insertSpeedTest(speedTestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
