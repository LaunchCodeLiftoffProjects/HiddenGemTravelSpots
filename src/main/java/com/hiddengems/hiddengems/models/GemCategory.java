package com.hiddengems.hiddengems.models;

public enum GemCategory {
    INDOOR( "Indoor activity"),
    OUTDOOR ( "Outdoor activity"),
    SEASONAL ("Seasonal activity"),
    FAMILY ("Family-friendly"),
    WHEELCHAIR ("Wheelchair access"),
    CDC ("Meets CDC Guidelines"),
    PET ("Pet-friendly"),
    FREEPARKING ("Free parking"),
    FOODDRINK ("Food and drinks");
//    INDOOR(1, "Indoor activity"),
//    OUTDOOR (2, "Outdoor activity"),
//    SEASONAL (3,"Seasonal activity"),
//    FAMILY (4,"Family-friendly"),
//    WHEELCHAIR (5,"Wheelchair access"),
//    CDC (6,"Meets CDC Guidelines"),
//    PET (7,"Pet-friendly"),
//    FREEPARKING (8,"Free parking"),
//    FOODDRINK (9,"Food and drinks");

//    @Id
//    private int id;
    private String categoryName;

    GemCategory(String categoryName) {
        this.categoryName = categoryName;
    }

//    public int getId() {
//        return id;
//    }

    public String getCategoryName() {
        return categoryName;
    }

//    @NotNull
//    @ManyToMany(mappedBy="categories")
//    private List<Gem> gems = new ArrayList<>();
}