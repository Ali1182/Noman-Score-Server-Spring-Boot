package com.server_ns.nomad_score.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.server_ns.nomad_score.service.GoogleNearbySearchApi;
import com.server_ns.nomad_score.service.LocationService;

@RestController
public class GoogleApiController {

    @Autowired
    private GoogleNearbySearchApi googleNearbySearchApi;

    @Autowired
    private LocationService locationService;

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


    @GetMapping("/location-info")
    public ResponseEntity<?> getLocationInfo(@RequestParam(required = false) Double lat,
                                             @RequestParam(required = false) Double lng) {
        if (lat == null || lng == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Latitude and longitude are required"));
        }

        try {
            Map<String, String> locationData = locationService.getCityAndCountry(lat, lng);
            return ResponseEntity.ok(locationData);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }


    
}
