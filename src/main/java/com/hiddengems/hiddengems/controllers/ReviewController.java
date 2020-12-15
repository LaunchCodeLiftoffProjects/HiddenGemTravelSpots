package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.User;
import com.hiddengems.hiddengems.models.data.ReviewRepository;
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
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    @GetMapping("/review")
    public String displayReviewForm(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Review a Gem");
        model.addAttribute(new Review());
        return "review";
    }

    @PostMapping("/review")
    public String processReviewForm(@ModelAttribute @Valid Review newReview,
                                    Errors errors, HttpServletRequest request, Model model) {

        User user = getUserFromSession(request.getSession());

        if (errors.hasErrors()) {
            return "review";
        } else {
            newReview.setUser(user);
            reviewRepository.save(newReview);
        }

        return "redirect:";
    }

}
