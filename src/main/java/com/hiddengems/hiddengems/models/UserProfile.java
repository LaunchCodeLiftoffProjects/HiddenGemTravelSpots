package com.hiddengems.hiddengems.models;




import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class UserProfile extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;


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

    private String avatar;

    public UserProfile() { }

    public UserProfile(UserAccount userAccount) {
        this.userAccount = userAccount;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getAvatar() {
        if(this.avatar == null) {
            this.avatar = "&#128142;";
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
