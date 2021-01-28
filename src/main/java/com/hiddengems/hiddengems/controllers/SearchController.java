package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.GemCategory;
import com.hiddengems.hiddengems.models.GemData;
import com.hiddengems.hiddengems.models.data.GemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("")
public class SearchController {

    @Autowired
    private GemRepository gemRepository;

    @RequestMapping(value = "/search-results" , method=RequestMethod.POST)
    public String displaySearchResults(@RequestParam(value = "category", required=false) List<GemCategory> category,
                                       @RequestParam String searchTerm,
                                       Model model) {
        Iterable<Gem> gems;
        if (category != null) {
            System.out.println(category);
          if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")) {
                gems = GemData.findByCategory(category, gemRepository.findAll());
            } else {
              gems = GemData.findByCategoryAndValue(category, searchTerm, gemRepository.findAll());
          }
        } else if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")) {
            gems = gemRepository.findAll();
            } else {
            gems = GemData.findByValue(searchTerm, gemRepository.findAll());
            }
        if(gems == null) {
            model.addAttribute("no gems", "no gems found");
        }
        model.addAttribute("categories", GemCategory.values());
        model.addAttribute("gems", gems);
        return "search-results";
        }

    }
