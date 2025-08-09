package com.server_ns.nomad_score.model;

public class IpInfo {
    private String ip;
    private String city;
    private String country;
    private String isp;
    private String org;
    private String domain;
    private String latitude;
    private String longitude;

    public IpInfo(String ip, String city, String country, String isp, String org, String domain, String latitude, String longitude) {
        this.ip = ip;
        this.city = city;
        this.country = country;
        this.isp = isp;
        this.org = org;
        this.domain = domain;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public IpInfo() {
        this.ip = "";
        this.city = "";
        this.country = "";
        this.isp = "";
        this.org = "";
        this.domain = "";
        this.latitude = "";
        this.longitude = "";
    }

    public String getIp() {
        return ip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIsp() {
        return isp;
    }
    
    public String getOrg(){
        return org;
    }
    
    public String getDomain(){
        return domain;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public String getLongitude() {
        return longitude;
    }
}