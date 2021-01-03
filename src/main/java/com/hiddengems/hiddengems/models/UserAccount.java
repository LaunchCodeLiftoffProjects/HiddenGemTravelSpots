package com.hiddengems.hiddengems.models;


import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userAccount")
    private UserProfile userProfile;

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
}

