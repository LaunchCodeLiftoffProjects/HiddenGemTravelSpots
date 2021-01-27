package com.hiddengems.hiddengems.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("")
    public String index(Model model) {
        return "index"; }

    @GetMapping("errors")
    public String errors(Model model) {
        model.addAttribute("message", "Page Not Found");
        return "../error";
    }

}
