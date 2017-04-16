package com.einstens3.ironchef.models;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;


@ParseClassName("Recipe")
public class Recipe extends ParseObject {
    // ------------------------------------
    //  Constants
    // ------------------------------------
    public static final String TAG = Recipe.class.getSimpleName();

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CHALLENGES = "challenges";
    public static final String KEY_LIKES = "likes";

    // ------------------------------------
    //  Static convenient methods
    // ------------------------------------

    // ------------------------------------
    //  Constructors
    // ------------------------------------
    public Recipe() {
    }

    // ------------------------------------
    //  Getters/Setters
    // ------------------------------------

    // name: String - Recipe Name  Name could be unique

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

    //  standardName: String - Standard Recipe Name for grouping same foods

    public String getStandardName() {
        return getString("standardName");
    }

    public void setStandardName(String value) {
        put("standardName", value);
    }

    // author: ParseUser - Recipe Author (owner)

    public ParseUser getAuthor() {
        return (ParseUser) getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser value) {
        put(KEY_AUTHOR, value);
    }

    // description: String - Recipe description

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String value) {
        put("description", value);
    }

    // ingredients: List<String> - list of ingredients

    public List<String> getIngredients() {
        return getList("ingredients");
    }

    public void setIngredients(List<String> value) {
        put("ingredients", value);
    }

    // photo: ParseFile - Recipe's main photo

    public ParseFile getPhoto() {
        return getParseFile("photo");
    }

    public void setPhoto(ParseFile value) {
        put("photo", value);
    }

    // categories: List<String> - List of categories that the recipe belongs to

    public List<String> getCategories() {
        return getList("categories");
    }

    public void setCategories(List<String> value) {
        put("categories", value);
    }

    public List<String> getSteps() {
        return getList("steps");
    }

    public void setSteps(List<String> value) {
        put("steps", value);
    }

    public List<String> getIngridients() {
        return getList("ingridients");
    }

    public void setIngridients(List<String> value) {
        put("ingridients", value);
    }


    // serving: long - a quantity of food suitable for how many person.

    public long getServing() {
        return getLong("serving");
    }

    public void setServing(long value) {
        put("serving", value);
    }

    // cookingTime: long - total cooking time

    public long getCookingTime() {
        return getLong("cookingTime");
    }

    public void setCookingTime(long value) {
        put("cookingTime", value);
    }

    // challengeTo: Recipe - null in case this is original recipe
    //                     - reference to original recipe

    public Recipe getChallengeTo() {
        return (Recipe) getParseObject("challengeTo");
    }

    public void setChallengeTo(Recipe value) {
        put("challengeTo", value);
    }

    // NOTE - For Sprint 1, Steps are fixed number of steps

//    class Step extends ParseObject{
//        String text;
//        ParseFile photo;
//
//    }
//    public List<Step> getSteps();

    public String getStep1Text() {
        return getString("step1text");
    }

    public void setStep1Text(String value) {
        put("step1text", value);
    }

    public ParseFile getStep1Photo() {
        return getParseFile("step1photo");
    }

    public void setStep1Photo(ParseFile value) {
        put("step1photo", value);
    }

    public String getStep2Text() {
        return getString("step2text");
    }

    public void setStep2Text(String value) {
        put("step2text", value);
    }

    public ParseFile getStep2Photo() {
        return getParseFile("step2photo");
    }

    public void setStep2Photo(ParseFile value) {
        put("step2photo", value);
    }

    public String getStep3Text() {
        return getString("step3text");
    }

    public void setStep3Text(String value) {
        put("step3text", value);
    }

    public ParseFile getStep3Photo() {
        return getParseFile("step3photo");
    }

    public void setStep3Photo(ParseFile value) {
        put("step3photo", value);
    }

    public boolean isDraft() {
        return getBoolean("draft");
    }

    public void setDraft(boolean value) {
        put("draft", value);
    }


    public boolean isPublic() {
        return getBoolean("public");
    }

    public void setPublic(boolean value) {
        put("public", value);
    }

    // ----------------------------------------------------------------
    // Like related features
    // ----------------------------------------------------------------

    public ParseRelation<ParseUser> getLikesRelation() {
        return getRelation(KEY_LIKES);
    }

    /**
     * Check if currentUser (logged in user) likes this Recipe
     * The callback method's paramet is ParseUser object.
     * If it is null, user does not Like, if it is actual ParserUser object, user likes this Recipe.
     *
     * @param callback
     */
    public void doesCurrentUserLikeRecipe(GetCallback<ParseUser> callback) {
        doesCurrentUserLikeRecipe(ParseUser.getCurrentUser(), callback);
    }

    public void doesCurrentUserLikeRecipe(ParseUser user, GetCallback<ParseUser> callback) {
        getLikesRelation()
                .getQuery()
                .whereEqualTo("objectId", user.getObjectId())
                .getFirstInBackground(callback);
    }

    public void like() {
        addLike(ParseUser.getCurrentUser());
    }

    public void unlike() {
        addLike(ParseUser.getCurrentUser());
    }

    public void countLikes(CountCallback callback) {
        getLikesRelation().getQuery().countInBackground(callback);
    }


    public void addLike(final ParseUser value) {
        // block double likes
        doesCurrentUserLikeRecipe(value, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (object == null) {
                    getLikesRelation().add(value);
                    saveInBackground();
                }
            }
        });
    }

    public void removeLike(final ParseUser value) {
        // block double unlikes
        doesCurrentUserLikeRecipe(value, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (object != null) {
                    getLikesRelation().remove(value);
                    saveInBackground();
                }
            }
        });
    }

    // Query all ParseUsers who like this Recipe.
    public void queryLikes(FindCallback<ParseUser> callback) {
        getLikesRelation().getQuery().findInBackground(callback);
    }

    // ----------------------------------------------------------------
    // Challenge related features
    // ----------------------------------------------------------------

    public ParseRelation<Challenge> getChallengesRelation() {
        return getRelation(KEY_CHALLENGES);
    }

    public void countChallenges(CountCallback callback) {
        getChallengesRelation().getQuery().countInBackground(callback);
    }

    public void addChallenge(Challenge value) {
        getChallengesRelation().add(value);
        saveInBackground();
    }

    public void removeChallenge(Challenge value) {
        getChallengesRelation().remove(value);
        saveInBackground();
    }

    public void getOwnChallenge(GetCallback<Challenge> callback) {
        getChallengesRelation()
                .getQuery()
                .whereEqualTo(Challenge.KEY_USER, ParseUser.getCurrentUser())
                .getFirstInBackground(callback);
    }

    // Query all Challenges which are associated with this Recipe.
    public void queryChallenges(FindCallback<Challenge> callback) {
        getChallengesRelation().getQuery().findInBackground(callback);
    }

    // ----------------------------------------------------------------
    // Other
    // ----------------------------------------------------------------
    public boolean isOwnRecipe() {
        if (getAuthor() == null)
            return false;
        return ParseUser.getCurrentUser().getObjectId().equals(getAuthor().getObjectId());
    }
}