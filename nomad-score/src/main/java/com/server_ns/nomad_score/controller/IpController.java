package com.server_ns.nomad_score.controller;

import com.server_ns.nomad_score.service.IpInfoService;
import com.server_ns.nomad_score.service.VpnDetectionService;
import com.server_ns.nomad_score.model.IpInfo;
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
    public ResponseEntity<?> getIpInfo(@RequestParam(required = false) String ip,
                                     @RequestParam(required = false) Double userLat,
                                     @RequestParam(required = false) Double userLon) {
        try {
            IpInfo ipObject = ipInfoService.lookupIp(ip);
            
            if (ipObject == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Failed to get IP info\"}");
            }
            
            Boolean isUsingVpn = vpnDetectionService.isUsingVpn(ipObject, userLat, userLon);
            
            // Create response object
            return ResponseEntity.ok(Map.of(
                "ipInfo", ipObject,
                "isUsingVpn", isUsingVpn
            ));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}