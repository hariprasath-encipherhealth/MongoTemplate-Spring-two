package com.mongoTemplate.mongoDemo.controller;

import com.mongoTemplate.mongoDemo.collection.Names;
import com.mongoTemplate.mongoDemo.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regex")
public class RegexController {

    @Autowired
    InputService inputService;

    @GetMapping("/generate")
    public List<Names> regex(@RequestParam String input)
    {
        return inputService.regex(input);
    }

}
