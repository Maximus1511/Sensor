package ru.kryz.max.RestServer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class TestController {


    @GetMapping("/work")
    public String Hello(){
        return "work is ok";
    }
}
