package com.hiddengems.hiddengems.models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class GemData {

    public static ArrayList<Gem> findByCategoryAndValue(List<GemCategory> category, String value, Iterable<Gem> allGems) {
        ArrayList<Gem> results = new ArrayList<>();
        if (value.toLowerCase().equals("all")) {
            return (ArrayList<Gem>) allGems;
        }

        if (category.size() > 0) {
            ArrayList<Gem> categoryResults = findByCategory(category, allGems);
            if (!value.equals("all") && !value.equals("")) {
                results = findByValue(value, categoryResults);
            }
            return results;
        }
        return results;
    }

    public static ArrayList<Gem> findByCategory(List<GemCategory> checkedCategory, Iterable<Gem> allGems) {
        LinkedHashSet<Gem> resultSet = new LinkedHashSet<Gem>();
        //ArrayList<Gem> results = new ArrayList<>();
        for (Gem gem : allGems) {
            List<GemCategory> gemCategories = gem.getCategories();
            for (GemCategory gemCategory : gemCategories) {
                for (GemCategory category : checkedCategory) {
                    if (gemCategory == category) {
                        resultSet.add(gem);
                    }
                }
            }
            }
        ArrayList<Gem> results = new ArrayList<Gem>(resultSet);
        return results;
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
