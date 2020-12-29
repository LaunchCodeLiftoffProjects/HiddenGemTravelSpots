package com.hiddengems.hiddengems.models;

import java.util.ArrayList;

public class GemData {

    public static ArrayList<Gem> findByColumnAndValue(String value, Iterable<Gem> allGems) {
        ArrayList<Gem> results = new ArrayList<>();

        if (value.toLowerCase().equals("all")) {
            //return (ArrayList<Gem>) allGems;
            results = findByValue(value, allGems);
            return results;
        }

//        if (column.equals("all")) {
//            results = findByValue(value, allGems);
//            return results;
//        }
        for (Gem gem: allGems) {
            String aValue = getFieldValue(gem, value);
            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                results.add(gem);
            }
        }
        return results;
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
