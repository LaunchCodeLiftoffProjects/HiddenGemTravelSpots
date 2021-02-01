package com.hiddengems.hiddengems.models;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Guest extends AbstractEntity{

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Please enter a valid email address format (username@example.com)")
    private String emailAddress;


    @Size(min = 0, max = 500, message = "There is a 500 character limit")
    private String message;


    public Guest(@NotBlank String name, @NotBlank @Email(message = "Please enter a valid email address format (username@example.com)") String emailAddress, @Size(min = 0, max = 500, message = "There is a 500 character limit") String message) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.message = message;
    }

    public Guest(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
