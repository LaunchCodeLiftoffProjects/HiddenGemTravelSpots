package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
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
import java.util.ArrayList;
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

            ArrayList<Gem> recentGems = new ArrayList();
            ArrayList<Review> recentReviews = new ArrayList();
            for (UserAccount friend : userAccount.getFriends()) {
                for (Gem gem : friend.getGems()) {
                    //if a friend submitted or edited gem since user's last logout, add to feed
                    if (gem.getLastUpdated().after(userAccount.getLastLogout())) {
                        recentGems.add(gem);
                    }
                }

                for (Review review : friend.getReviews()) {
                    //if a friend submitted or edited review since user's last logout, add to feed
                    if (review.getLastUpdated().after(userAccount.getLastLogout())) {
                        recentReviews.add(review);
                    }
                }
            }

            model.addAttribute("profile", userProfile);
            model.addAttribute("recentGems", recentGems);
            model.addAttribute("recentReviews", recentReviews);
            model.addAttribute("myGems", userAccount.getGems());
            model.addAttribute("myReviews", userAccount.getReviews());
            model.addAttribute("myFriends", userAccount.getFriends());

        }

        return "index";
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
