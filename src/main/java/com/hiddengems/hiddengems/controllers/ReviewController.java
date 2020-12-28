package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.User;
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

    @GetMapping("/reviews/add")//localhost:8080/reviews/add?gemId=
    public String displayReviewForm(@RequestParam Integer gemId, HttpServletRequest request, Model model) {
        Optional<Gem> gemOpt = gemRepository.findById(gemId);
        User user = getUserFromSession(request.getSession());

        if(gemOpt.isPresent() && user != null) {
            // model.addAttribute("gem", (Gem) gemOpt.get());
            Gem gem = gemOpt.get();
            ReviewFormDTO reviewDTO = new ReviewFormDTO();
            Review review = new Review();
            reviewDTO.setGem(gem);
            reviewDTO.setUser(user);
            reviewDTO.setReview(review);
            model.addAttribute("review", review);
            model.addAttribute("reviewDTO", reviewDTO);
            model.addAttribute("title", "Leave a review for: " + gem.getGemName());
        } else {
            model.addAttribute("message", "No such Gem exists with this Id");
            return "../error";
        }


        model.addAttribute(new Review());
        return "reviews/add";
    }

    @PostMapping("/reviews/add")
    public String processReviewForm(@ModelAttribute @Valid ReviewFormDTO review,
                                    Errors errors, HttpServletRequest request, Model model) {

        User user = getUserFromSession(request.getSession());

        if (errors.hasErrors()) {
            model.addAttribute("errors", errors);
            return "reviews/add";
        } else {

            Gem gem = review.getGem();
            gemRepository.save(gem);

            Review newReview = review.getReview();
            newReview.setUser(user);
            newReview.setGem(gem);
            reviewRepository.save(newReview);
        }

        return "redirect:../";
    }

    @GetMapping("/reviews/edit/{reviewId}")
    public String displayEditReviewForm(Model model, @PathVariable int reviewId, HttpServletRequest request) {

        User user = getUserFromSession(request.getSession());
        Optional<Review> review = reviewRepository.findById(reviewId);




        if (review.isPresent()) {
            Review oldReview = (Review) review.get();

            Optional<Gem> gemOpt = gemRepository.findById(oldReview.getGem().getId());
            if (gemOpt.isEmpty()) {
                return "redirect:../";
            }
            Gem gem = gemOpt.get();

            ReviewFormDTO reviewDTO = new ReviewFormDTO(oldReview, gem, user);
            model.addAttribute("reviewDTO", reviewDTO);
            //model.addAttribute("review", oldReview);
            model.addAttribute("edit", true);
            return "reviews/edit.html";
        } else {
            return "redirect:../";
        }
    }

    @PostMapping("/reviews/edit/{reviewId}")
    public String processEditReviewForm(@PathVariable Integer reviewId, @ModelAttribute @Valid ReviewFormDTO reviewDTO, Error errors,
                                        HttpServletRequest request, Model model) {


        Optional<Review> review = reviewRepository.findById(reviewId);



        if (review.isPresent()) {
            Review newReview = (Review) review.get();
            User user = getUserFromSession(request.getSession());


            if (newReview.getUser().getId() == user.getId()) {
                newReview.setUser(user);
                newReview.setReviewText(reviewDTO.getReview().getReviewText());
                newReview.setThumbsup(reviewDTO.getReview().isThumbsup());
                reviewRepository.save(newReview);
                return "success-test";
            } else {
                model.addAttribute("message", "You are not authorized to edit this Gem Review.");
                return "../error";
            }
        }

        return "redirect:../";
    }

    @PostMapping("delete")
    public String processDeleteReview(@RequestParam int reviewId, HttpServletRequest request, Model model) {
        reviewRepository.deleteById(reviewId);
        //model.addAttribute("message", "This will delete the review eventually. Review ID = " + newReview.getId());
        //model.addAttribute("message", "This will delete the review eventually.");
        // TODO: Change annotations in Review, Gem, User to "mapped by" instead of @JoinColumn (maybe) and then remove Review from the lists in the other classes fields
        // https://stackoverflow.com/questions/36058977/hibernate-many-to-one-delete-only-child
        return "success-test";
    }

    // TODO: Update the Review template with conditional statements to populate the fields with pre-existing review data & flip the Submit button to Save
    // TODO: Add 'Delete' handlers

}
