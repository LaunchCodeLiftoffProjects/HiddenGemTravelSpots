package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.FileUploadUtil;
import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.GemCategory;
import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.data.GemRepository;
import com.hiddengems.hiddengems.models.data.ReviewRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
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

    public Gem getGemById(Integer gemId) { // helper method for finding a Gem by its ID
        Optional<Gem> gem = gemRepository.findById(gemId);
        return (Gem) gem.orElse(null);
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
        model.addAttribute("title", "Submit new Hidden Gem!");
        model.addAttribute("submitBtn", "Submit Gem");
        model.addAttribute("editing", false);
        return "gems/add";
    }

//    @PostMapping("add")
//    public String processAddGemForm(@ModelAttribute @Valid Gem newGem,
//                                    Errors errors, Model model, HttpServletRequest request, @RequestParam List<GemCategory> categories) throws IOException {
//
//        if (errors.hasErrors()) {
//            return "gems/add";
//        }
//
//        List <GemCategory> categoryObjs = (List<GemCategory>) categories;
//        newGem.setCategories(categoryObjs);
//
//        UserAccount userAccount = getUserFromSession(request.getSession());
//        newGem.setUserAccount(userAccount);
//
//
//        newGem.setUser(userAccount); // TODO: refactor to make sure we keep this line OR the one above not both
//        userAccount.addGem(newGem); // TODO: refactor and test to see if this line is necessary
//
//        gemRepository.save(newGem);
//
//        return "gems/detail";
//    }

    @PostMapping("add")
    public String processAddGemForm(@ModelAttribute @Valid Gem newGem,
                                          Errors errors, Model model, HttpServletRequest request, @RequestParam List<GemCategory> categories, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (errors.hasErrors()) {
            return "gems/add";
        }

        List <GemCategory> categoryObjs = (List<GemCategory>) categories;
        newGem.setCategories(categoryObjs);

        UserAccount userAccount = getUserFromSession(request.getSession());
        newGem.setUserAccount(userAccount);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        newGem.setPhotos(fileName);



        newGem.setUser(userAccount); // TODO: refactor to make sure we keep this line OR the one above not both
        userAccount.addGem(newGem); // TODO: refactor and test to see if this line is necessary

        gemRepository.save(newGem);

        String uploadDir = "gem-photos/" + newGem.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "gems/detail";
    }


    @GetMapping("detail/{gemId}")
    public String displayViewGem(HttpServletRequest request, Model model, @PathVariable int gemId) {

        Optional<Gem> optGem = gemRepository.findById(gemId);
        if (optGem.isPresent()) {
            Gem gem = (Gem) optGem.get();

            boolean canEdit = false;
            if (gem.getUser() == getUserFromSession(request.getSession())) {
                canEdit = true;
            }

            model.addAttribute("canEdit", canEdit);
            model.addAttribute("gem", gem);
            model.addAttribute("categories", gem.getCategories());

            return "gems/detail";
        } else {
            return "redirect:";
        }
    }

    @GetMapping("edit")//localhost:8080/gems/edit?gemId=
    public String displayEditGemForm(@RequestParam Integer gemId, HttpServletRequest request, Model model) {
        Gem gem = getGemById(gemId);
        UserAccount userAccount = getUserFromSession(request.getSession());

        if(gem == null || userAccount == null) { // null check
            model.addAttribute("message", "Problem loading Gem or User Account from database.");
            return "redirect:/error";
        } else if (!gem.getUserAccount().equals(userAccount) || gem.getUserAccount() == null) { // checks if editing user is 'owning' user of Gem
            model.addAttribute("message", "You are not authorized to edit this Gem.  If a correction" +
                    "needs to be made please contact the original submitting user of this Gem or a Hidden Gems administrator.");
            return "redirect:/error";
        }

        model.addAttribute("categories", GemCategory.values()); // all the categories
        model.addAttribute("categoryList", gem.getCategories()); // the selected categories
        model.addAttribute("title", "Edit Gem details for: " + gem.getGemName());
        model.addAttribute("submitBtn", "Save Changes");
        model.addAttribute("editing", true);
        model.addAttribute("gem", gem);
        return "gems/edit";
    }

    @PostMapping("edit")
    public String processEditGemForm(@ModelAttribute @Valid Gem newGem, @RequestParam Integer gemId,
                                     @RequestParam List<GemCategory> categories,
                                     HttpServletRequest request, Model model) {

        Gem gem = getGemById(gemId);
        UserAccount userAccount = getUserFromSession(request.getSession());

        if (gem == null || userAccount == null) { // null check
            model.addAttribute("message", "Problem loading Gem or User Account from database.");
            return "/error";
        } else if (!gem.getUserAccount().equals(userAccount) || gem.getUserAccount() == null) { // checks if editing user is 'owning' user of Gem
            model.addAttribute("message", "You are not authorized to edit this Gem.  If a correction" +
                    "needs to be made please contact the original submitting user of this Gem or a Hidden Gems administrator.");
            return "/error";
        }

        gem.setUserAccount(userAccount);
        gem.setCategories(categories);
        gem.setDescription(newGem.getDescription());
        gem.setGemName(newGem.getGemName());
        gem.setGemPoint(newGem.getGemPoint());
        gem.setLatitude(newGem.getLatitude());
        gem.setLongitude(newGem.getLongitude());

        gemRepository.save(gem);

        return "redirect:/gems/detail/" + gem.getId();
    }

}
