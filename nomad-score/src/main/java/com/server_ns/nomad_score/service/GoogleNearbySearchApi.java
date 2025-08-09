package com.server_ns.nomad_score.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;




@Service
public class GoogleNearbySearchApi {

    @Value("${GOOGLE_API_KEY}")
    private String googleApiKey;
    
    public String getNearbyHotels(Double latitude, Double longitude, Integer radius) throws Exception {
        
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
        + "?location=" + latitude + "," + longitude
        + "&radius=" + radius
        + "&type=lodging"
        + "&key=" + googleApiKey;
    
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            return response;
        }catch (Exception e) {
            throw new Exception("Failed to get nearby hotels: " + e.getMessage());
        }

    }
}
