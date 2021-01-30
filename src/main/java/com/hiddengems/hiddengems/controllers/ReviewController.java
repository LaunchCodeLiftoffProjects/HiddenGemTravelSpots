package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.UserAccount;
import com.hiddengems.hiddengems.models.data.GemRepository;
import com.hiddengems.hiddengems.models.data.ReviewRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import com.hiddengems.hiddengems.models.dto.ReviewFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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

    public Review getReviewById(Integer reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return (Review) review.orElse(null);
    }

    public Gem getGemById(Integer gemId) {
        Optional<Gem> gem = gemRepository.findById(gemId);
        return (Gem) gem.orElse(null);
    }

    @GetMapping("/reviews/add")//localhost:8080/reviews/add?gemId=
    public String displayReviewForm(@RequestParam Integer gemId, HttpServletRequest request, Model model) {

        Gem gem = getGemById(gemId);
        UserAccount userAccount = getUserFromSession(request.getSession());

        if(gem != null && userAccount != null) {
            model.addAttribute("reviewDTO", new ReviewFormDTO(new Review(), gem, userAccount));
            model.addAttribute("title", "Leave a review for: " + gem.getGemName());
            model.addAttribute("submitBtnText", "Submit!");
            return "reviews/add";
        } else {
            model.addAttribute("message", "No such Gem exists with this Id");
            return "../error";
        }
    }

    @PostMapping("/reviews/add")
    public String processReviewForm(@ModelAttribute @Valid ReviewFormDTO review,
                                    Errors errors, HttpServletRequest request, Model model) {

        UserAccount userAccount = getUserFromSession(request.getSession());

        if (!errors.hasErrors()) {
            Gem gem = review.getGem();
            gemRepository.save(gem);

            Review newReview = review.getReview();
            newReview.setUser(userAccount);
            newReview.setGem(gem);
            userAccount.addReview(newReview);
            reviewRepository.save(newReview);

            return "redirect:/gems/detail/" + gem.getId();
        } else {
            model.addAttribute("errors", errors);
            return "reviews/add";
        }
    }

    @GetMapping("/reviews/edit/{reviewId}")
    public String displayEditReviewForm(Model model, @PathVariable int reviewId, HttpServletRequest request) {

        Review review = getReviewById(reviewId);
        Gem gem = getGemById(review.getGem().getId());
        UserAccount userAccount = getUserFromSession(request.getSession());

        if (review != null && review.getUser().getId() == userAccount.getId() && gem != null) {
            ReviewFormDTO reviewDTO = new ReviewFormDTO(review, gem, userAccount);
            model.addAttribute("reviewDTO", reviewDTO);
            model.addAttribute("review", review);
            model.addAttribute("submitBtnText", "Save");
            return "reviews/edit.html";
        } else {
            model.addAttribute("message", "You are not authorized to edit this Gem Review.");
            return "../error";
        }


    }

    @PostMapping("/reviews/edit/{reviewId}")
    public String processEditReviewForm(@ModelAttribute @Valid ReviewFormDTO reviewDTO, Errors errors,
                                        @PathVariable Integer reviewId, HttpServletRequest request, Model model) {

        Review review = getReviewById(reviewId);
        UserAccount userAccount = getUserFromSession(request.getSession());

        if (review != null && review.getUser().getId() == userAccount.getId() && !errors.hasErrors()) {
            review.setUser(userAccount);
            review.setReviewText(reviewDTO.getReview().getReviewText());
            review.setThumbsup(reviewDTO.getReview().isThumbsup());
            reviewRepository.save(review);
            return "redirect:../../";
        } else {
            model.addAttribute("message", "You are not authorized to edit this Gem Review.");
            return "../error";
        }
    }

    @PostMapping("delete/{reviewId}")
    public String processDeleteReview(@PathVariable Integer reviewId, HttpServletRequest request, Model model) {

        Review review = getReviewById(reviewId);
        UserAccount userAccount = getUserFromSession(request.getSession());

        if (review != null && review.getUser().getId() == userAccount.getId()) {
            reviewRepository.deleteById(reviewId);
            return "redirect:../";
        } else {
            model.addAttribute("message", "You are not authorized to delete this Gem Review.");
            return "../error";
        }
    }
}