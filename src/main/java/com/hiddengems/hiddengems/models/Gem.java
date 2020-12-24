package com.hiddengems.hiddengems.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Gem extends AbstractEntity {

    @NotBlank
    @Size(min = 3, max = 100, message = "Gem name must be between 3 and 100 characters in length")
    private String gemName;

//    @NotNull
//    private double latitude;
//
//    @NotNull
//    private double longitude;

    @NotNull
    private String description;

    private GemCategory category;

    @OneToMany(mappedBy = "gem")
    private final List<Review> reviews = new ArrayList<>();

//    @ManyToMany
//    private final List<GemCategory> categories = new ArrayList<>();

//    @OneToMany
//    @JoinColumn
//    private final List<Photo> photos = new ArrayList<>();


    public Gem(String description, GemCategory category) {
        this.description = description;
        this.category = category;
    }

    public Gem() {}

    public String getGemName() {
        return gemName;
    }

    public void setGemName(String gemName) {
        this.gemName = gemName;
    }

//    public double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public GemCategory getCategory(){
        return category;
    }

    public void setCategory(){
        this.category = category;
    }
//
//    public List<GemCategory> getCategories() {
//        return categories;
//    }
    @Override
    public String toString() {
        return gemName;
    }
}
