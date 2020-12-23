package com.hiddengems.hiddengems.models.dto;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.User;

import javax.validation.constraints.NotNull;

public class ReviewFormDTO {

//    @NotNull
//    private Gem gem;

    @NotNull
    private User user;

    @NotNull
    private Review review;

    public ReviewFormDTO() { }

//    public Gem getGem() { return gem; }

    public Review getReview() { return review; }

    public void setReview(Review review) {
        this.review = review;
    }




}
