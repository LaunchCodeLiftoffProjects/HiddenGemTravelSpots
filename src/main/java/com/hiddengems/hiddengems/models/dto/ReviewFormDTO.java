package com.hiddengems.hiddengems.models.dto;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.UserAccount;

import javax.validation.constraints.NotNull;

public class ReviewFormDTO {

    @NotNull
    private Gem gem;

    @NotNull
    private UserAccount userAccount;

    @NotNull
    private Review review;

    public ReviewFormDTO() { }

    public ReviewFormDTO(Review review, Gem gem, UserAccount userAccount) {
        this.review = review;
        this.gem = gem;
        this.userAccount = userAccount;
    }

    public Gem getGem() {
        return gem;
    }

    public void setGem(Gem gem) {
        this.gem = gem;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
