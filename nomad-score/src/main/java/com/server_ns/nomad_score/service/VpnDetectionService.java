package com.server_ns.nomad_score.service;

import com.server_ns.nomad_score.model.IpInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class VpnDetectionService {

    public String getClientIp(HttpServletRequest request) {
        // Try to get IP from X-Forwarded-For header (handles proxies/load balancers)
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs: "clientIP, proxy1, proxy2"
            return forwardedFor.split(",")[0].trim();
        }

        // Fallback to remote address
        return request.getRemoteAddr();
    }

    public Boolean isUsingVpn(HttpServletRequest request, IpInfo ipInfo, Double userLat, Double userLon) {
        try {
            String clientIp = getClientIp(request);
            System.out.println("Client IP: " + clientIp);

            // Filter out local addresses
            if (clientIp == null || clientIp.isEmpty() ||
                clientIp.equals("127.0.0.1") ||
                clientIp.equals("::1")) {
                System.out.println("VPN Detection: Invalid or local IP");
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
                return null;
            }

            // Parse IP location
            double ipLat = Double.parseDouble(latStr);
            double ipLon = Double.parseDouble(lonStr);

            // Calculate differences
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
            return null;
        }
    }
}
