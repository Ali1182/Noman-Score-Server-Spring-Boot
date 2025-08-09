package com.server_ns.nomad_score.controller.supabase;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.server_ns.nomad_score.service.SupabaseService;

@RestController
public class SupabaseCityController {

    @Autowired
    private SupabaseService supabaseService;

    @GetMapping("/supabase/city")
    public ResponseEntity<?> getCity() {
        String cities = supabaseService.getCities();
        return ResponseEntity.ok(cities);
    }
    
}
