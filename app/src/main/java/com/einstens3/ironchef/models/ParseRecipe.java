package com.einstens3.ironchef.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;


@ParseClassName("Recipe")
public class ParseRecipe extends ParseObject {
    /**
     * Fields
     * - Name: String            <<-- unique Recipe name if user wants
     * - Standard Name: String   <<-- Standard name to grouping receipe
     * - createdAt: Date         <<-- ParseObject has createdAt and updatedAt
     * - updatedAt: Date         <<-- ParseObject has createdAt and updatedAt
     * - Author: ParseUser
     * - Description: String
     * - Ingredients: List<String>
     * - Directions: List<Direction>
     * - Photo: ParseFile
     * - Category - Set<String>
     * - Equipment - Set<String>
     * - Serving: Int
     * - Cooking time: Int (min)
     * - ChallengeTo:  Recipe
     */

    public ParseRecipe() {
    }


    // name: String - Recipe Name  Name could be unique

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    //  standardName: String - Standard Recipe Name for grouping same foods

    public String getStandardName() {
        return getString("standardName");
    }

    public void setStandardName(String standardName) {
        put("standardName", standardName);
    }

    // author: ParseUser - Recipe Author (owner)

    public ParseUser getAuthor() {
        return (ParseUser) getParseUser("author");
    }

    public void setAuthor(ParseUser author) {
        put("author", author);
    }

    // description: String - Recipe description

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    // ingredients: List<String> - list of ingredients

    public List<String> getIngredients() {
        return getList("ingredients");
    }

    public void setIngredients(List<String> ingredients) {
        put("ingredients", ingredients);
    }

    // photo: ParseFile - Recipe's main photo

    public ParseFile getPhoto() {
        return getParseFile("photo");
    }

    public void setPhoto(ParseFile photo) {
        put("photo", photo);
    }

    // categories: List<String> - List of categories that the recipe belongs to

    public List<String> getCategories() {
        return getList("categories");
    }

    public void setCategories(List<String> categories) {
        put("categories", categories);
    }


    // serving: long - a quantity of food suitable for how many person.

    public long getServing() {
        return getLong("serving");
    }

    public void setServing(long serving) {
        put("serving", serving);
    }

    // cookingTime: long - total cooking time

    public long getCookingTime() {
        return getLong("cookingTime");
    }

    public void setCookingTime(long cookingTime) {
        put("cookingTime", cookingTime);
    }

    // challengeTo: Recipe - null in case this is original recipe
    //                     - reference to original recipe

    public ParseRecipe getChallengeTo() {
        return (ParseRecipe) getParseObject("challengeTo");
    }

    public void setChallengeTo(ParseRecipe challengeTo) {
        put("challengeTo", challengeTo);
    }


    // stepPhotos: List<ParseFile> - List of step photo. Num of stepPhotos and stepTexts should be same.

    public List<ParseFile> getStepPhotos() {
        return getList("stepPhotos");
    }

    public void setStepPhotos(List<ParseFile> stepPhotos) {
        put("stepPhotos", stepPhotos);
    }

    // stepTexts: List<String> - List of step text. Num of stepPhotos and stepTexts should be same.

    public List<String> getStepTexts() {
        return getList("stepTexts");
    }

    public void setStepTexts(List<String> stepTexts) {
        put("stepTexts", stepTexts);
    }
}
