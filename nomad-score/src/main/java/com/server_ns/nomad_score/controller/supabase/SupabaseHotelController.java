package com.server_ns.nomad_score.controller.supabase;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.server_ns.nomad_score.service.SupabaseService;

@RestController
public class SupabaseHotelController {
    
    @Autowired
    private SupabaseService supabaseService;

    @GetMapping("/supabase/get/hotel")
    public ResponseEntity<?> getHotel(@RequestParam(required = true) String city) {
        String hotels = supabaseService.getHotels(city);
        return ResponseEntity.ok(hotels);
    }
    
}
