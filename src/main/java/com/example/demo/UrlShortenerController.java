package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShortenerController {

    private static final Map<String, String> data = new HashMap<>();

    @GetMapping("/short")
    public String shortenUrl(@RequestParam(name = "url") final String url) {
        String key = Integer.toHexString(url.hashCode());
        data.put(key, url);
        return key;
    }

    @GetMapping("/long")
    public String longUrl(@RequestParam(name = "tiny") final String tiny) {
        return data.get(tiny);
    }
}
