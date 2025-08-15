package com.server_ns.nomad_score.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LocationService {
    
    private static final String GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode";
    
    private final RestTemplate restTemplate;
    private final String googleApiKey;

    public LocationService(@Value("${GOOGLE_API_KEY}") String googleApiKey) {
        this.googleApiKey = Objects.requireNonNull(googleApiKey, "Google API key must not be null");
        this.restTemplate = new RestTemplate();
    }

    /**
     * Get city and country information from latitude and longitude coordinates
     * @param lat Latitude
     * @param lng Longitude
     * @return Map containing location information (city, country, state, postalCode, formattedAddress)
     * @throws RuntimeException if there's an error fetching or processing the location data
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, String> getCityAndCountry(double lat, double lng) {
        String url = String.format("%s/json?latlng=%.6f,%.6f&key=%s", 
            GEOCODE_API_URL, lat, lng, googleApiKey);

        try {
            // Make the API request with raw Map type to avoid type safety issues
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> apiResponse = response.getBody();

            // Check for valid response
            if (apiResponse == null) {
                throw new RuntimeException("No response from geocoding service");
            }

            String status = (String) apiResponse.get("status");
            if (!"OK".equals(status)) {
                String errorMessage = String.format("Geocoding API error: %s", 
                    status != null ? status : "Unknown error");
                throw new RuntimeException(errorMessage);
            }

            // Extract results with proper type casting
            List<Map<String, Object>> results = (List<Map<String, Object>>) apiResponse.get("results");
            if (results == null || results.isEmpty()) {
                throw new RuntimeException("No location results found");
            }

            // Get the first (most relevant) result
            Map<String, Object> firstResult = results.get(0);
            List<Map<String, Object>> addressComponents = 
                (List<Map<String, Object>>) firstResult.get("address_components");

            if (addressComponents == null || addressComponents.isEmpty()) {
                throw new RuntimeException("No address components found in the response");
            }

            // Initialize response map
            Map<String, String> locationData = new HashMap<>();
            locationData.put("formattedAddress", (String) firstResult.get("formatted_address"));
            locationData.put("latitude", String.valueOf(lat));
            locationData.put("longitude", String.valueOf(lng));

            // Extract address components
            for (Map<String, Object> component : addressComponents) {
                List<String> types = (List<String>) component.get("types");
                String longName = (String) component.get("long_name");
                String shortName = (String) component.get("short_name");

                if (types.contains("locality")) {
                    locationData.put("city", longName);
                } else if (types.contains("country")) {
                    locationData.put("country", longName);
                    locationData.put("countryCode", shortName);
                } else if (types.contains("administrative_area_level_1")) {
                    locationData.put("state", longName);
                    locationData.put("stateCode", shortName);
                } else if (types.contains("postal_code")) {
                    locationData.put("postalCode", longName);
                }
            }

            // Validate required fields
            if (!locationData.containsKey("city") || !locationData.containsKey("country")) {
                throw new RuntimeException("Could not determine city or country from the provided coordinates");
            }

            return locationData;

        } catch (Exception e) {
            throw new RuntimeException("Error processing location data: " + e.getMessage(), e);
        }
    }
}
