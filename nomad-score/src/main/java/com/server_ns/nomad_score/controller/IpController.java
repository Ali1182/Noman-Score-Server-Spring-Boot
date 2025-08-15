package com.server_ns.nomad_score.controller;

import com.server_ns.nomad_score.service.IpInfoService;
import com.server_ns.nomad_score.service.VpnDetectionService;
import com.server_ns.nomad_score.model.IpInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class IpController {

    @Autowired
    private IpInfoService ipInfoService;

    @Autowired
    private VpnDetectionService vpnDetectionService;

    @GetMapping("/api/ipinfo")
    public ResponseEntity<?> getIpInfo(HttpServletRequest request,
                                       @RequestParam(required = false) String ip,
                                       @RequestParam(required = false) Double userLat,
                                       @RequestParam(required = false) Double userLon) {
        try {
            // If no IP provided by client, detect from request
            if (ip == null || ip.isEmpty()) {
                ip = vpnDetectionService.getClientIp(request);
                System.out.println("Auto-detected client IP: " + ip);
            }

            // Validate IP
            if (ip == null || ip.isEmpty() || ip.equals("127.0.0.1") || ip.equals("::1")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid or local IP address"));
            }

            // Lookup IP info
            IpInfo ipObject = ipInfoService.lookupIp(ip);
            if (ipObject == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get IP info"));
            }

            // Check VPN usage
            Boolean isUsingVpn = vpnDetectionService.isUsingVpn(request, ipObject, userLat, userLon);

            return ResponseEntity.ok(Map.of(
                    "ipInfo", ipObject,
                    "isUsingVpn", isUsingVpn
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal server error: " + e.getMessage()
            ));
        }
    }
}
