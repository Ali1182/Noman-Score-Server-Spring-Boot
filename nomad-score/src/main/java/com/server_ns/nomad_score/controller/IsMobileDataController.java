package com.server_ns.nomad_score.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.server_ns.nomad_score.service.IpInfoService;
import com.server_ns.nomad_score.service.IsMobileDataChecker;
import com.server_ns.nomad_score.model.IpInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IsMobileDataController {

    @Autowired
    private IpInfoService ipInfoService;    

    @GetMapping("/isMobileData")
    public ResponseEntity<?> isMobileData(@RequestParam(required = false) String ip) {


        Boolean isMobileData = IsMobileDataChecker.isMobileData(ipInfoService.lookupIp(ip));
        
        return ResponseEntity.ok(isMobileData);
    }
}
