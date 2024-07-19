package com.henningstorck.springbootvite.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloWorldController {
    @GetMapping("/")
    public String helloWorld() {
        return "helloWorld/helloWorld";
    }
}
