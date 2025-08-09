package com.server_ns.nomad_score.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.server_ns.nomad_score.model.IpInfo;

@Service
public class IpInfoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IpInfo lookupIp(String ip) {
        try {
            String url = "https://ipwho.is/" + (ip != null ? ip : "");
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null) {
                return null;
            }
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            // Safely get values with fallbacks for missing fields
            String ipAddress = getFieldSafely(jsonNode, "ip", "");
            String city = getFieldSafely(jsonNode, "city", "");
            String country = getFieldSafely(jsonNode, "country", "");
            String latitude = getFieldSafely(jsonNode, "latitude", "");
            String longitude = getFieldSafely(jsonNode, "longitude", "");

            // Safely access nested fields in the connection object
            JsonNode connection = jsonNode.get("connection");
            String isp = "";
            String org = "";
            String domain = "";

            if (connection != null && !connection.isNull()) {
                isp = getFieldSafely(connection, "isp", "");
                org = getFieldSafely(connection, "org", "");
                domain = getFieldSafely(connection, "domain", "");
            }

            IpInfo ipInfo = new IpInfo(ipAddress, city, country, isp, org, domain, latitude, longitude);
            
            return ipInfo;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String getFieldSafely(JsonNode node, String fieldName, String defaultValue) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asText() : defaultValue;
    }
}