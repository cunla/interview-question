package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Value("${backbase.my_secret:no yaml}")
    private String secret;

    @GetMapping("/getSecret")
    public String getSecret() {
        return String.format("Your secret is: %s", secret);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "greeting " + name;
    }


}
