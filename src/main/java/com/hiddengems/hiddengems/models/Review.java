package com.hiddengems.hiddengems.models;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Review extends AbstractEntity{

    @NotNull
    private boolean thumbsup;


}
