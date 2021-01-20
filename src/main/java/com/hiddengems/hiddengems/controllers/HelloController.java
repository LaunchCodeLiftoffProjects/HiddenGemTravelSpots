package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.UserProfile;
import com.hiddengems.hiddengems.models.data.UserProfileRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class HelloController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

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

    public UserProfile getProfileByUser(UserAccount userAccount) {
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserAccount(userAccount));

        if (userProfile.isEmpty()) {
            return null;
        }

        return userProfile.get();
    }

    @RequestMapping("")
    @GetMapping("index")
    public String index(HttpServletRequest request, Model model) {
        UserAccount userAccount = getUserFromSession(request.getSession());

        if (userAccount != null) {
            model.addAttribute("user", userAccount);
        }

        UserProfile userProfile = getProfileByUser(userAccount);

        if (userProfile != null) {
            model.addAttribute("profile", userProfile);

        }

        return "index";
    }

    @GetMapping("errors")
    public String errors(Model model) {
        model.addAttribute("message", "Page Not Found");
        return "../error";
    }

}
