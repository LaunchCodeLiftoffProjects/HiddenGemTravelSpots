package com.hiddengems.hiddengems.controllers;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.UserProfile;
import com.hiddengems.hiddengems.models.data.GemRepository;
import com.hiddengems.hiddengems.models.data.UserProfileRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import com.hiddengems.hiddengems.models.dto.LoginFormDTO;
import com.hiddengems.hiddengems.models.dto.RegisterFormDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private GemRepository gemRepository;

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

    private static void setUserInSession(HttpSession session, UserAccount userAccount) {
        session.setAttribute(userSessionKey, userAccount.getId());
    }

    public UserProfile getProfileByUser(UserAccount userAccount) {
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserAccount(userAccount));

        if (userProfile.isEmpty()) {
            return null;
        }

        return userProfile.get();
    }


    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        model.addAttribute("userProfile", new UserProfile());
        return "register";
    }


    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        UserAccount existingUserAccount = userRepository.findByUsername(registerFormDTO.getUsername());

        if (existingUserAccount != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        Date date = new Date();
        UserAccount newUserAccount = new UserAccount(registerFormDTO.getUsername(), registerFormDTO.getPassword(), date);
        userRepository.save(newUserAccount);
        setUserInSession(request.getSession(), newUserAccount);
        model.addAttribute("user", newUserAccount);

        return "redirect:profile/settings";
    }


    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }


    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }

        UserAccount theUserAccount = userRepository.findByUsername(loginFormDTO.getUsername());

        if (theUserAccount == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword();

        if (!theUserAccount.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }

        setUserInSession(request.getSession(), theUserAccount);
        Date date = new Date();
        theUserAccount.setLastLogin(date);
        userRepository.save(theUserAccount);//updates last login timestamp

        ArrayList<Gem> recentGems = new ArrayList();
        ArrayList<Review> recentReviews = new ArrayList();
        for (UserAccount friend : theUserAccount.getFriends()) {
            for (Gem gem : friend.getGems()) {
                //if a friend submitted or edited gem since user's last logout, add to feed
                if (gem.getLastUpdated().after(theUserAccount.getLastLogout())) {
                    recentGems.add(gem);
                }
            }

            for (Review review : friend.getReviews()) {
                //if a friend submitted or edited review since user's last logout, add to feed
                if (review.getLastUpdated().after(theUserAccount.getLastLogout())) {
                    recentReviews.add(review);
                }
            }
        }
        model.addAttribute("user", theUserAccount);
        model.addAttribute("profile", getProfileByUser(theUserAccount));
        UserAccount userAccount = getUserFromSession(request.getSession());
        model.addAttribute("recentGems", recentGems);
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("myGems", userAccount.getGems());
        model.addAttribute("myReviews", userAccount.getReviews());
        model.addAttribute("myFriends", userAccount.getFriends());
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        UserAccount userAccount = getUserFromSession(request.getSession());
        Date date = new Date();
        userAccount.setLastLogout(date);
        userRepository.save(userAccount);
        request.getSession().invalidate();
        return "redirect:/login";
    }

}
