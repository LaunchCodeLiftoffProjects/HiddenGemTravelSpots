package com.hiddengems.hiddengems.models;

import com.hiddengems.hiddengems.models.dto.GemDTO;

import java.util.ArrayList;

public class GemData {

    public static ArrayList<Gem> findByColumnAndValue(GemDTO category, String value, Iterable<Gem> allGems) {
        ArrayList<Gem> results = new ArrayList<>();

        if (value.toLowerCase().equals("all")) {
            return (ArrayList<Gem>) allGems;
        }

        if (category.toString().equals("all")) {
            results = findByValue(value, allGems);
            return results;
        }
        for (Gem gem: allGems) {
            String aValue = getCategoryValue(gem, category);
            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                results.add(gem);
            }
        }
        return results;
    }

    //TODO: for loop iterates over gemCategories
    public static String getCategoryValue(Gem gem, GemDTO categoryName){
        String theValue = "";
        if (categoryName.toString().equals("categoryName")){
            theValue= gem.getCategoryName();
        }
        return theValue;
    }

    public static String getFieldValue(Gem gem, String fieldName){
        String theValue = "";
        if (fieldName.equals("name")){
            theValue = gem.getGemName();
        }
        return theValue;
    }

    public static ArrayList<Gem> findByValue(String value, Iterable<Gem> allGems) {
        String lower_val = value.toLowerCase();

        ArrayList<Gem> results = new ArrayList<>();

        for (Gem gem : allGems) {

            if (gem.getGemName().toLowerCase().contains(lower_val)) {
                results.add(gem);
            } else if (gem.getDescription().toLowerCase().contains(lower_val)) {
                results.add(gem);
            } else if (gem.toString().toLowerCase().contains(lower_val)) {
                results.add(gem);
            }

        }

        return results;
    }

}
