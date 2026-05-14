package com.halildurmus.hotdeals.legal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/legal")
public class LegalController {

    @GetMapping("/privacy-policy")
    public Map<String, String> getPrivacyPolicy() {
        Map<String, String> response = new HashMap<>();
        response.put("type", "privacy-policy");
        response.put("version", "1.0");
        response.put("lastUpdated", "2026-05-12");
        response.put("content", "Your full privacy policy text goes here...");
        return response;
    }
}
