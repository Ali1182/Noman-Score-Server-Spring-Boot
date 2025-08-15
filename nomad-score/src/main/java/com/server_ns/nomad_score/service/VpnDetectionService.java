package com.server_ns.nomad_score.service;

import com.server_ns.nomad_score.model.IpInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class VpnDetectionService {
    
    /**
     * Extracts the client IP address from the request, handling proxies and load balancers.
     * @return The client IP address or null if not found
     */
    public static String getClientIpAddress() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                    RequestContextHolder.getRequestAttributes())).getRequest();
            
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            
            // In case of multiple IPs (can happen with X-Forwarded-For), take the first one
            if (ipAddress != null && ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0].trim();
            }
            
            return ipAddress;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Validates if the given IP address is valid and not a local/loopback address
     * @param ipAddress The IP address to validate
     * @return true if the IP is valid and not local, false otherwise
     */
    public static boolean isValidIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }
        
        // Check for local/loopback addresses
        return !ipAddress.equals("127.0.0.1") && 
               !ipAddress.equals("0:0:0:0:0:0:0:1") && 
               !ipAddress.equals("::1") &&
               !ipAddress.startsWith("192.168.") &&
               !ipAddress.startsWith("10.") &&
               !ipAddress.startsWith("172.16.") &&
               !ipAddress.startsWith("172.17.") &&
               !ipAddress.startsWith("172.18.") &&
               !ipAddress.startsWith("172.19.") &&
               !ipAddress.startsWith("172.20.") &&
               !ipAddress.startsWith("172.21.") &&
               !ipAddress.startsWith("172.22.") &&
               !ipAddress.startsWith("172.23.") &&
               !ipAddress.startsWith("172.24.") &&
               !ipAddress.startsWith("172.25.") &&
               !ipAddress.startsWith("172.26.") &&
               !ipAddress.startsWith("172.27.") &&
               !ipAddress.startsWith("172.28.") &&
               !ipAddress.startsWith("172.29.") &&
               !ipAddress.startsWith("172.30.") &&
               !ipAddress.startsWith("172.31.");
    }
    
    public Boolean isUsingVpn(IpInfo ipInfo, Double userLat, Double userLon) {
        try {
            // Get client IP and validate it
            String clientIp = getClientIpAddress();
            System.out.println("Client IP: " + clientIp);
            
            if (!isValidIpAddress(clientIp)) {
                System.out.println("VPN Detection: Invalid or local IP address detected");
                return null;
            }
            
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