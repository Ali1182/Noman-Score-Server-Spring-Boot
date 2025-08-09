package com.server_ns.nomad_score.service;

import com.server_ns.nomad_score.model.IpInfo;
import org.springframework.stereotype.Service;

@Service
public class VpnDetectionService {
    
    public Boolean isUsingVpn(IpInfo ipInfo, Double userLat, Double userLon) {
        try {
            // If no user coordinates provided, can't determine VPN usage
            if (userLat == null || userLon == null) {
                System.out.println("VPN Detection: No user coordinates provided");
                return null;
            }
            
            // Check if IP coordinates are available
            String latStr = ipInfo.getLatitude();
            String lonStr = ipInfo.getLongitude();
            
            if (latStr == null || latStr.isEmpty() || lonStr == null || lonStr.isEmpty()) {
                System.out.println("VPN Detection: No IP coordinates available");
                return null; // Can't determine VPN usage without IP coordinates
            }
            
            // Get IP location from API response
            double ipLat = Double.parseDouble(latStr);
            double ipLon = Double.parseDouble(lonStr);
            
            // Use your original logic: check if coordinate differences > 4 degrees
            double latDiff = Math.abs(ipLat - userLat);
            double lonDiff = Math.abs(ipLon - userLon);
            
            boolean isVpn = latDiff > 4 || lonDiff > 4;
            
            System.out.println("VPN Detection Debug:");
            System.out.println("  User Location: " + userLat + ", " + userLon);
            System.out.println("  IP Location: " + ipLat + ", " + ipLon);
            System.out.println("  Latitude Difference: " + latDiff + " degrees");
            System.out.println("  Longitude Difference: " + lonDiff + " degrees");
            System.out.println("  Threshold: 4 degrees");
            System.out.println("  Is VPN: " + isVpn);
            
            return isVpn;
            
        } catch (NumberFormatException e) {
            System.out.println("VPN Detection: Error parsing coordinates - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("VPN Detection: Unexpected error - " + e.getMessage());
            return null; // Can't determine if error occurs
        }
    }
}