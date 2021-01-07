package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.UserProfile;
import com.hiddengems.hiddengems.models.data.UserProfileRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    private static final String userSessionKey = "user";

    public UserAccount getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) { return null; }

        Optional<UserAccount> user = userRepository.findById(userId);

        if (user.isEmpty()) { return null; }

        return user.get();
    }

    @GetMapping("/profile/settings")
    public String displayUserProfileSettings(HttpServletRequest request, Model model) {
        UserAccount userAccount = getUserFromSession(request.getSession());
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserAccount(userAccount));
        UserProfile profile;

        if (userProfile.isPresent()) {
            profile = userAccount.getUserProfile();
            model.addAttribute("title", "Edit User Profile");
        } else {
            profile = new UserProfile(userAccount);
            model.addAttribute("title", "Create User Profile");
        }

        model.addAttribute("userProfile", profile);
        return "profile/settings";
    }

    @PostMapping("/profile/settings")
    public String processUserProfileSettings(@ModelAttribute @Valid UserProfile userProfileNew, Errors errors, HttpServletRequest request, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("errors", errors);
            model.addAttribute("message", "Errors has errors, fix it.");
            errors.rejectValue("displayName", "displayName.empty", "You must pick a display name");
            return "settings";
        }

        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserAccount(getUserFromSession(request.getSession())));

        UserProfile userProfileOld;
        UserProfile userProfileUpdated;

        if (userProfile.isPresent()) {
            userProfileOld = userProfile.get();
            userProfileUpdated = new UserProfile(userProfileOld.getUserAccount(), userProfileOld.getEmailAddress(),
                                                    userProfileOld.getDisplayName(), userProfileOld.getZipCode(),
                                                    userProfileOld.getBio());
            userProfileRepository.save(userProfileUpdated);

        } else {
            userProfileNew.setUserAccount(getUserFromSession(request.getSession()));
            userProfileRepository.save(userProfileNew);
        }



        userProfileNew.setUserAccount(getUserFromSession(request.getSession()));

        return "redirect:../";
    }
}
