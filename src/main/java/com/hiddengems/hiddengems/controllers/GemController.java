package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.GemCategory;
import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.data.GemRepository;
import com.hiddengems.hiddengems.models.data.ReviewRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("gems")
public class GemController {

    @Autowired
    private GemRepository gemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;


    private static final String userSessionKey = "user";

    public UserAccount getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) {
            return null;
        }

        Optional<UserAccount> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }


    @GetMapping ("index")
    public String index(Model model) {

        model.addAttribute("Gems", "All Gems");
        model.addAttribute("gem", gemRepository.findAll());
        return "gems/index";
    }

    @GetMapping("add")
    public String displayAddGemForm(Model model) {
        model.addAttribute(new Gem());
        model.addAttribute("categories", GemCategory.values());
        return "gems/add";
    }

    @PostMapping("add")
    public String processAddGemForm(@ModelAttribute @Valid Gem newGem,
                                    Errors errors, Model model, @RequestParam List<GemCategory> categories) {

        if (errors.hasErrors()) {
            return "gems/add";
        }
         List <GemCategory> categoryObjs = (List<GemCategory>) categories;
        newGem.setCategories(categoryObjs);
        gemRepository.save(newGem);

        return "gems/detail";
    }

    @GetMapping("detail/{gemId}")
    public String displayViewGem(Model model, @PathVariable int gemId) {

        Optional<Gem> optGem = gemRepository.findById(gemId);
        if (optGem.isPresent()) {
            Gem gem = (Gem) optGem.get();
            model.addAttribute("gem", gem);
            model.addAttribute("categories", gem.getCategories());
            return "gems/detail";
        } else {
            return "redirect:";
        }
    }
}
