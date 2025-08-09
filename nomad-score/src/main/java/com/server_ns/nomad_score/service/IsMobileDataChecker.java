package com.server_ns.nomad_score.service;

import java.util.Arrays;
import java.util.List;
import com.server_ns.nomad_score.model.IpInfo;
import org.springframework.stereotype.Service;

@Service
public class IsMobileDataChecker {

    private static final List<String> MOBILE_DOMAINS = Arrays.asList(
        // USA
        "verizon", "att.com", "t-mobile.com", "uscellular.com", "cspire.com", "gci.com",
        "bluegrasscellular.com", "metropcs.com", "cricketwireless.com", "boostmobile.com",
        "straighttalk.com", "mintmobile.com", "googlefi.com", 

        // Europe
        "vodafone.com", "orange.com", "telekom.de", "telefonica.com", "bt.com", "tele2.com",
        "three.co.uk", "ee.co.uk", "sfr.fr", "telia.se", "tim.it", "telenor.com",
        "bouyguestelecom.fr", "kpn.com", "swisscom.ch", "proximus.be",

        // Asia
        "chinamobileltd.com", "chinaunicom.com", "chinatelecom-h.com", "nttdocomo.co.jp",
        "softbank.jp", "kddi.com", "sktelecom.com", "bharti.com", "jio.com", "vodafoneidea.com",
        "celcom.com.my", "maxis.com.my", "digi.com.my", "singtel.com", "starhub.com",
        "telkomsel.com", "indosatooredoo.com",

        // Africa
        "mtn.com", "airtel.com", "vodacom.co.za", "orange.com", "telkom.co.za", "safaricom.co.ke",
        "9mobile.com.ng", "globacom.com", "tigo.com",

        // South America
        "claro.com", "movistar.com", "vivo.com.br", "tim.com.br", "personal.com.ar",
        "entel.cl", "tigo.com",

        // Central America & Caribbean
        "digicelgroup.com", "cwc.com", "hon.du", "tegsahonduras.com", "plusmovil.cr",
        "cootel.cu", "libertylatinamerica.com", "caribcell.com", "flowcaribbean.com",
        "octelmobile.com", "telecayman.ky", "bmobiledotcom.com",

        // Canada
        "rogers.com", "bell.ca", "telus.com", "freedommobile.ca", "sasktel.com", "videotron.com",
        "eastlink.ca", "publicmobile.ca", "fido.ca", "koodomobile.com", "virginmobile.ca",
        "chatrwireless.com", "pcmobile.ca", "luckymobile.ca",

        // Oceania
        "telstra.com.au", "optus.com.au", "vodafone.com.au", "tpgi.com.au", "spark.co.nz",
        "2degrees.nz", "vocus.co.nz", "aapt.com.au", "amaysim.com.au", "boost.com.au",
        "dodo.com.au", "koganmobile.com.au"
    );

    private static final List<String> MOBILE_KEYWORDS = Arrays.asList(
        "verizon", "att", "t-mobile", "tmobile", "uscellular", "cspire", "gci",
        "metropcs", "cricket", "boostmobile", "mintmobile", "googlefi",
        "vodafone", "telekom", "telefonica", "orange", "telia", "telenor", "tim",
        "jio", "airtel", "mtn", "vodacom", "safaricom", "telstra", "optus",
        "bell", "rogers", "freedommobile", "koodo", "fido", "luckymobile"
    );

    public static boolean isMobileData(IpInfo data) {
        String org = safeLower(data.getOrg());
        String isp = safeLower(data.getIsp());
        String domain = safeLower(data.getDomain());

        boolean isDomainMatch = MOBILE_DOMAINS.stream()
            .anyMatch(dn -> org.contains(dn) || isp.contains(dn) || domain.contains(dn));

        boolean isKeywordMatch = MOBILE_KEYWORDS.stream()
            .anyMatch(kw -> org.contains(kw) || isp.contains(kw) || domain.contains(kw));

        return isDomainMatch || isKeywordMatch;
    }

    private static String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }
}
