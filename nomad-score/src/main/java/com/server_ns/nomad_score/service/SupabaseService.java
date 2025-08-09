package com.server_ns.nomad_score.service;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupabaseService {

    @Value("${supabaseUrl}")
    private String supabaseUrl;

    @Value("${supabaseKey}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCities() {
        String url = supabaseUrl + "/rest/v1/test_cities?order=city_rank.asc";
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );
        return response.getBody();
    }

    public String getHotels(String city) {
        String url = supabaseUrl + "/rest/v1/TestedHotels?city=eq." + city;        
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );
        return response.getBody();
    }

    public String insertSpeedTest(Map<String, Object> speedTestData) throws Exception {
        String url = supabaseUrl + "/rest/v1/UserWifiSpeedTest";
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(Collections.singletonList(speedTestData), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new Exception("Failed to insert speed test: " + e.getMessage());
        }
    }
}