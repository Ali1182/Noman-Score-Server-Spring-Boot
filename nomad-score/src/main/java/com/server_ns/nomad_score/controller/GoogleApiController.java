package com.server_ns.nomad_score.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.server_ns.nomad_score.service.GoogleNearbySearchApi;

@RestController
public class GoogleApiController {

    @Autowired
    private GoogleNearbySearchApi googleNearbySearchApi;

    @GetMapping("/google/get/hotel/nearby")
    public ResponseEntity<?> getHotelNearby(@RequestParam(required = false ) Double latitude,
                                            @RequestParam(required = false) Double longitude,
                                            @RequestParam(required = false) Integer radius ) {
        try{
            String response = googleNearbySearchApi.getNearbyHotels(latitude, longitude, radius);

            return ResponseEntity.ok(response);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }

    }


    
}
