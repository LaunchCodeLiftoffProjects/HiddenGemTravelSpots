package com.hiddengems.hiddengems.models;


import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class UserAccount extends AbstractEntity {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    @CreatedDate
    private Date acctCreationDate;

    @UpdateTimestamp
    private Date lastLogin;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userAccount",cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserAccount> friends;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gem> gems;

    public UserAccount() {}

    public UserAccount(String username, String password, Date acctCreationDate) {
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.acctCreationDate = acctCreationDate;
    }

    public String getUsername() {
        return username;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public Date getAcctCreationDate() {
        return acctCreationDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<UserAccount> getFriends() {
        return friends;
    }

    public void setFriends(List<UserAccount> friends) {
        this.friends = friends;
    }

    public void addFriend(UserAccount friend) {
        this.friends.add(friend);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public List<Gem> getGems() {
        return gems;
    }

    public void setGems(List<Gem> gems) {
        this.gems = gems;
    }

    public void addGem(Gem gem) {
        this.gems.add(gem);
    }
}

