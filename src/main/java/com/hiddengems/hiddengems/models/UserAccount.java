package com.hiddengems.hiddengems.models;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class UserAccount extends AbstractEntity {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    @NotBlank
    @Email(message = "Please enter a valid email address format (username@example.com)")
    private String emailAddress;

    @NotBlank
    @Size(min = 2, max = 25, message = "Display name must be between 2 and 25 characters in length")
    private String displayName;

    @Digits(integer = 5, fraction = 0, message = "Please enter a valid 5-digit US Zip Code or leave blank")
    private Integer zipCode;

    @Size(min = 0, max = 500, message = "We get it - you contain multitudes - but there is a 500 character limit")
    private String bio;

    @ManyToMany
    private List<UserAccount> friends;

    public UserAccount() {}

    public UserAccount(String username, String password) {
        this.username = username;
        this.pwHash = encoder.encode(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

