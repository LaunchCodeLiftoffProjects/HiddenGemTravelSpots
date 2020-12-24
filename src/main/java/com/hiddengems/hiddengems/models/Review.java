package com.hiddengems.hiddengems.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Review extends AbstractEntity{

    @NotNull
    private boolean thumbsup;

    @ManyToOne
    private Gem gem;

    @ManyToOne
    private User user;

    @Size(max = 500, message = "Review must be no more than 500 characters in length")
    private String reviewText;

    public Review(String reviewText, Gem gem) {
        this.reviewText = reviewText;
        this.gem = gem;
    }

    public Review() {}

    public boolean isThumbsup() {
        return thumbsup;
    }

    public void setThumbsup(boolean thumbsup) {
        this.thumbsup = thumbsup;
    }

    public Gem getGem() { return gem; }

    public void setGem(Gem gem) { this.gem = gem; }

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
