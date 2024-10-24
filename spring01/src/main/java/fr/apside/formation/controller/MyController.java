package fr.apside.formation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class MyController {


    @GetMapping
    public String greeting(@RequestParam(required = false) String name) {
        return name != null ? "Hello" + " " + name + "!" : "Hello" + "!";
    }
}
