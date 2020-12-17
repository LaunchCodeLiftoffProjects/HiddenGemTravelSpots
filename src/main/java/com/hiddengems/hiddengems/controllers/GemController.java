package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.data.GemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
    @RequestMapping("gems")
    public class GemController {

        @Autowired
        private GemRepository gemRepository;


        @GetMapping ("index")
        public String index(Model model) {

            model.addAttribute("Gems", "All Gems");
            model.addAttribute("gem", gemRepository.findAll());
            return "gems/index";
        }

        @GetMapping("add")
        public String displayAddGemForm(Model model) {
            model.addAttribute(new Gem());
            return "gems/add";
        }

        @PostMapping("add")
        public String processAddGemForm(@ModelAttribute @Valid Gem newGem,
                                        Errors errors, Model model) {

            if (errors.hasErrors()) {
                return "gems/index";
            }
            gemRepository.save(newGem);
            return "redirect:../";
        }

        @GetMapping("view/{gemId}")
        public String displayViewGem(Model model, @PathVariable int gemId) {

            Optional<Gem> optGem = gemRepository.findById(gemId);
            if (optGem.isPresent()) {
                Gem gem = (Gem) optGem.get();
                model.addAttribute("gem", gem);
                return "gems/view";
            } else {
                return "redirect:../";
            }
        }
    }
