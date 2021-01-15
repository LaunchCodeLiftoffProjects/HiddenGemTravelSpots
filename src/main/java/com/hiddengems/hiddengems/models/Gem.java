package com.hiddengems.hiddengems.models;

//Here are our imports needed for geometry and geography types.

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import javax.persistence.*;
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

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private String description;

    @NotNull
    private Point gemPoint;

    @ManyToOne
    private UserAccount userAccount;

//    private GemCategory category;

    @OneToMany(mappedBy = "gem")
    private final List<Review> reviews = new ArrayList<>();

    @ElementCollection
    private List<GemCategory> categories;

//    @OneToMany
//    @JoinColumn
//    private final List<Photo> photos = new ArrayList<>();


//    public Gem(String description, GemCategory category) {
//        this.description = description;
//        this.category = category;
//    }

    public Gem(String gemName, Double latitude, Double longitude, String description, List <GemCategory> categories) {
        this.gemName = gemName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.categories = categories;
    }

    public Gem() {}


    public String getGemName() {
        return gemName;
    }

    public void setGemName(String gemName) {
        this.gemName = gemName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        recalculate();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        recalculate();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Review> getReviews() {
        return reviews;
    }

//    public GemCategory getCategory(){
//        return category;
//    }

//    public void setCategory(){
//        this.category = category;
//    }


    public Point getGemPoint() {
        return gemPoint;
    }

    public void setGemPoint(Point gemPoint) {
        this.gemPoint = gemPoint;
        this.latitude = gemPoint.getY(); //We have to assign the latitude and longitude to an X and Y coordinate.
        this.longitude = gemPoint.getX();//Otherwise, the point we attempt to create in the method below will not know if latitude or longitude is x or y.
    }

    //This method recalculates the long/lat above into a geography data type.
    // Technically, it is geometry, but the SRID 4326 designates we are placing the point somewhere on the globe instead of a plane.
    //The PrecisionModel() allows us to be accurate up to about 500 meters.
    private void recalculate() {
        GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), 4326);
        gemPoint = geomFactory.createPoint(new Coordinate(longitude, latitude)); // Because we are mapping this essentially in a graph, we plot (x,y) or (long, lat).
    }


    public List<GemCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<GemCategory> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return gemName;
    }

}


