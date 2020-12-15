package com.hiddengems.hiddengems.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Review extends AbstractEntity{

    @NotNull
    private String thumbsup;

//    @ManyToOne
//    private Gem gem;

    @ManyToOne
    private User user;

    private String reviewText;

    public Review() {}

    public String getThumbsup() {
        return thumbsup;
    }

    public void setThumbsup(String thumbsup) {
        this.thumbsup = thumbsup;
    }

    //    public Gem getGem() {
//        return gem;
//    }
//
//    public void setGem(Gem gem) {
//        this.gem = gem;
//    }
//
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
