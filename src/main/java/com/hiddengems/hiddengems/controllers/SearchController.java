package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.GemCategory;
import com.hiddengems.hiddengems.models.GemData;
import com.hiddengems.hiddengems.models.data.GemRepository;
import com.hiddengems.hiddengems.models.dto.GemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;


@Controller
@RequestMapping("")
public class SearchController {

    @Autowired
    private GemRepository gemRepository;


//    @PostMapping("/search-results")
//    public String displaySearchResults(Model model, @RequestParam String searchTerm){
//        Iterable<Gem> gems;
//        if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")){
//            gems = gemRepository.findAll();
//
//        } else {
//            gems = GemData.findByValue(searchTerm, gemRepository.findAll());
//        }
//
//        model.addAttribute("gems", gems);
//        model.addAttribute("title", "Gems with " + searchTerm);
//        return "search-results";
//    }

    static HashMap<String, String> columnChoices = new HashMap<>();

    public SearchController () {

        columnChoices.put("all", "All");
        columnChoices.put("category", "Category");

    }

    @RequestMapping("search")
    public String search(Model model) {
//        model.addAttribute("columns", columnChoices);
        model.addAttribute("categories", GemCategory.values());
        return "search";
    }

//    @PostMapping("/search-results")
//    public String displaySearchResults(Model model, @RequestParam String searchTerm){
//        Iterable<Gem> gems;
//        if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")){
//            gems = gemRepository.findAll();
//        } else {
//            gems = GemData.findByValue(searchTerm, gemRepository.findAll());
//        }
//
//        model.addAttribute("column", columnChoices);
//       // model.addAttribute("categories", gem.getCategories());
//        model.addAttribute("title", "Gems with " + searchTerm);
//        model.addAttribute("gems", gems);
//        return "search-results";
//    }

    //add an 'else if' to allow for the searchTerm.equals("") AND gemCategory(s) chosen
    @PostMapping("/search-results")
    public String displaySearchResults(@ModelAttribute GemDTO gemCategory , @RequestParam String searchTerm, Model model){
        Iterable<Gem> gems;
        if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")){
            gems = gemRepository.findAll();
        } else {
            gems = GemData.findByColumnAndValue(gemCategory, searchTerm, gemRepository.findAll());
        }

        //model.addAttribute("columns", columnChoices);
        // model.addAttribute("categories", gem.getCategories());
        model.addAttribute("title", columnChoices.get(gemCategory) + ": " + "Gems with " + searchTerm);
        model.addAttribute("gems", gems);
        return "search-results";
    }

}