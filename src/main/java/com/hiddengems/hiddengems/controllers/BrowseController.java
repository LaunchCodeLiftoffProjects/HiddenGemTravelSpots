package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.data.GemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class BrowseController {

    @Autowired
    private GemRepository gemRepository;

    @GetMapping("gems-index")
    public String index(Model model){
        Iterable<Gem> gems;
        gems = gemRepository.findAll();
        model.addAttribute("title", "All Gems");
        model.addAttribute("gems", gems);
        return "gems-index";
    }
}
