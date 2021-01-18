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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequestMapping("list")
public class ListController {

    @Autowired
    private GemRepository gemRepository;

    static HashMap<String, String> columnChoices = new HashMap<>();

    public ListController () {
        columnChoices.put("all", "All");
        columnChoices.put("category", "Category");
    }

    @RequestMapping("")
    public String list(Model model) {
        model.addAttribute("gemRepository", gemRepository);
        model.addAttribute("categories", GemCategory.values());
        return "list";
    }

    @RequestMapping("gems")
    public String listGemsByColumnAndValue(@ModelAttribute @Valid GemDTO gemCategory , @RequestParam String searchTerm, Model model) {
        Iterable<Gem> gems;
        if (gemCategory.toString().toLowerCase().equals("all")){
            gems = gemRepository.findAll();
            model.addAttribute("title", "All Gems");
        } else {
            gems = GemData.findByColumnAndValue(gemCategory, searchTerm, gemRepository.findAll());
            //model.addAttribute("title", "Gems with " + columnChoices.get(gemCategory) + ": " + searchTerm);
        }
        model.addAttribute("gems", gems);
        return "list-gems";
    }

}
