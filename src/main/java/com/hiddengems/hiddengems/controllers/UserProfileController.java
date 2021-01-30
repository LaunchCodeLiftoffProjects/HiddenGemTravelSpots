package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.UserProfile;
import com.hiddengems.hiddengems.models.data.UserProfileRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
            model.addAttribute("existing", true);
        } else {
            profile = new UserProfile(userAccount);
            model.addAttribute("title", "Create User Profile");
        }

        model.addAttribute("userProfile", profile);
        return "profile/settings";
    }

    @PostMapping("/profile/settings")
    public String processUserProfileSettings(@ModelAttribute UserProfile userProfileNew, HttpServletRequest request, Model model) {

        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserAccount(getUserFromSession(request.getSession())));

        UserProfile profile;
        UserProfile userProfileUpdated;

        if (userProfile.isPresent()) {
            profile = userProfile.get();
            profile.setDisplayName(userProfileNew.getDisplayName());
            profile.setBio(userProfileNew.getBio());
            profile.setEmailAddress(userProfileNew.getEmailAddress());
            profile.setZipCode(userProfileNew.getZipCode());
            userProfileRepository.save(profile);
            return "redirect:../../";
        } else {
            userProfileNew.setUserAccount(getUserFromSession(request.getSession()));
            userProfileRepository.save(userProfileNew);
            return "redirect:/";
        }
    }

    @PostMapping("/delete-user")
    public String processDeleteUserAccount(HttpServletRequest request, Model model) {
        UserAccount userAccount = getUserFromSession(request.getSession());
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserAccount(userAccount));

        if (userAccount != null) {
            userRepository.delete(userAccount);
        }

        if (userProfile.isPresent()) {
            UserProfile profile = userProfile.get();
            userProfileRepository.delete(profile);
        }

        request.getSession().invalidate();

        return "redirect:/";
    }

    @GetMapping("/users/view")//localhost:8080/users/view?id= POST MAPPING // should be get mapping, we are retrieving info
    public String displayPublicProfile(@RequestParam Integer id, HttpServletRequest request, Model model) {
        UserAccount userAccount = getUserFromSession(request.getSession()); //get logged in user
        if (userAccount == null) {
            model.addAttribute("message", "Unable to determine logged in user.");
            return "redirect:/error";
        }

        Optional<UserAccount> pubUserAcctOpt = userRepository.findById(id);//get user to display profile
        if (pubUserAcctOpt.isEmpty()) {
            model.addAttribute("message", "Requested user not found or does not exist");
            return "redirect:/error";
        }
        UserAccount pubUserAcct = pubUserAcctOpt.get();

        boolean following = false;
        if (userAccount.getFriends().contains(pubUserAcct)) { //determine if logged in user is already 'followed'
            following = true;
        }

        UserProfile pubUserProfile = userProfileRepository.findByUserAccount(pubUserAcct);//get public user profile

        model.addAttribute("profile", pubUserProfile);
        model.addAttribute("user", pubUserAcct);
        model.addAttribute("following", following);
        model.addAttribute("myGems", pubUserAcct.getGems());
        model.addAttribute("myReviews", pubUserAcct.getReviews());
        model.addAttribute("myFriends", pubUserAcct.getFriends());

        return "/users/view";
    }


    @PostMapping("/profile/follow")//localhost:8080//profile/follow?id={userId}&action={String = 'add'|'remove'}
    public String processFollowOrUnfollowUser(@RequestParam Integer id, @RequestParam String action,
                                              HttpServletRequest request, Model model) {
        UserAccount userAccount = getUserFromSession(request.getSession());
        Optional<UserAccount> followUser = userRepository.findById(id);

        if(followUser.isEmpty()) {
            model.addAttribute("message", "Unable to follow user.  User not found :-(");
            return "redirect:/error";
        } else {
            Optional<UserProfile> followUserProfile= Optional.ofNullable(userProfileRepository.findByUserAccount(followUser.get()));
            if(followUserProfile.isEmpty()) {
                model.addAttribute("message", "Unable to follow user.  User not found :-(");
                return "redirect:/error";
            } else {
                model.addAttribute("user", followUser.get());
                model.addAttribute("profile", followUserProfile);
                if(action.equals("add")) {
                    userAccount.addFriend(followUser.get());
                } else if(action.equals("remove")) {
                    userAccount.removeFriend(followUser.get());
                }
                userRepository.save(userAccount);
                return "redirect:/users/view?id=" + id.toString();
            }
        }
    }
}
