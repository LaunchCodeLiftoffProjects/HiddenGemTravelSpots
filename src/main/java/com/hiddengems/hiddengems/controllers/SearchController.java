package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.GemData;
import com.hiddengems.hiddengems.models.data.GemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("")
public class SearchController {

    @Autowired
    private GemRepository gemRepository;

//    @RequestMapping("/search-results")
//    public String search(Model model){
//        model.addAttribute("searchTerm", gemRepository);
//        return "search-results";
//    }


    @PostMapping("/search-results")
    public String displaySearchResults(Model model, @RequestParam String searchTerm){
        Iterable<Gem> gems;
        if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")){
            gems = gemRepository.findAll();

        } else {
            gems = GemData.findByValue(searchTerm, gemRepository.findAll());
        }

        model.addAttribute("gems", gems);
        model.addAttribute("title", "Gems with " + searchTerm);
        return "search-results";
    }

//    @PostMapping("")
//    public String displaySearchResults(Model model, @RequestParam String searchTerm){
//        Iterable<Gem> gems;
//        if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")) {
//            gems=gemRepository.findAll();
//        } else {
//            //gems=gemRepository.findById(searchTerm);
//            gems = GemData.findByValue(searchTerm, gemRepository.findAll());
//        }
//        model.addAttribute("columns", columnChoices);
//        model.addAttribute("title", "gems with " + columnChoices.get(searchTerm));
//        model.addAttribute("gems", gems);
//
//        return "search-results";
//    }




}
