package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.*;
import com.hiddengems.hiddengems.models.data.GuestRepository;
import com.hiddengems.hiddengems.models.data.UserProfileRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class HelloController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private GuestRepository guestRepository;

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

        if (userProfile != null && userAccount != null) {

            ArrayList<Gem> recentGems = new ArrayList();
            ArrayList<Review> recentReviews = new ArrayList();
            if (!userAccount.getFriends().isEmpty()) {
                for (UserAccount friend : userAccount.getFriends()) {

                    if (!friend.getGems().isEmpty()) {
                        recentGems.addAll(friend.getGems());
                        model.addAttribute("recentGems", recentGems);
//                        for (Gem gem : friend.getGems()) {
//                            //if a friend submitted or edited gem since user's last logout, add to feed
//
//                            if (gem.getLastUpdated().after(userAccount.getLastLogout())) {
//                                recentGems.add(gem);
//                           }
//                        }
                    }

                    if(!friend.getReviews().isEmpty()) {
                        recentReviews.addAll(friend.getReviews());
                        model.addAttribute("recentReviews", recentReviews);
//                        for (Review review : friend.getReviews()) {
//                            //if a friend submitted or edited review since user's last logout, add to feed
//                            if (review.getLastUpdated().after(userAccount.getLastLogout())) {
//                                recentReviews.add(review);
//                            }
//                        }
                    }

                }
            }


            model.addAttribute("profile", userProfile);
            model.addAttribute("myGems", userAccount.getGems());
            model.addAttribute("myReviews", userAccount.getReviews());
            model.addAttribute("myFriends", userAccount.getFriends());

        }

        return "index";
    }


    @GetMapping("about")
    public String about (Model model){
        return "about";
    }

    @GetMapping("contact")
    public String displayContactForm (Model model){
        model.addAttribute(new Guest());
        model.addAttribute("title", "Send us a message!");
        return "contact";
    }

    @PostMapping("contact")
    public String processContactForm (Model model, Guest newGuest, Errors errors){
        if (errors.hasErrors()) {
            return "contact";
        }

        guestRepository.save(newGuest);
        return "contact";
    }

    @GetMapping("errors")

    public String errors(Model model) {
        model.addAttribute("message", "Page Not Found");
        return "error";
    }

    @GetMapping("error")
    public String error(Model model) {
        model.addAttribute("message", "Page Not Found");
        return "error";
    }


}
