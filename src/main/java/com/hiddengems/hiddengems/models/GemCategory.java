package com.hiddengems.hiddengems.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public enum GemCategory {
    INDOOR("Indoor activity", 1),
    OUTDOOR ("Outdoor activity", 2),
    SEASONAL ("Seasonal activity", 3),
    FAMILY ("Family-friendly", 4),
    WHEELCHAIR ("Wheelchair access", 5),
    CDC ("Meets CDC Guidelines",6),
    PET ("Pet-friendly",7),
    FREEPARKING ("Free parking",8),
    FOODDRINK ("Food and drinks",9);

    private final String categoryName;

    @Id
    private final int id;

    GemCategory(String categoryName, int id) {
        this.categoryName = categoryName;
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public int getId() {
        return id;
    }
}