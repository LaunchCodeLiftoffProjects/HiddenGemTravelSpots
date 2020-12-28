package com.hiddengems.hiddengems.models.dto;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.User;

import javax.validation.constraints.NotNull;

public class ReviewFormDTO {

    @NotNull
    private Gem gem;

    @NotNull
    private User user;

    @NotNull
    private Review review;

    public ReviewFormDTO() { }

    public ReviewFormDTO(Review review, Gem gem, User user) {
        this.review = review;
        this.gem = gem;
        this.user = user;
    }

    public Gem getGem() {
        return gem;
    }

    public void setGem(Gem gem) {
        this.gem = gem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
