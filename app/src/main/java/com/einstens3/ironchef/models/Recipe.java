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

    // access keys (ParseObject)
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_UPDATED_AT = "updatedAt";

    // access keys (Recipe)
    public static final String KEY_NAME = "name";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_PHOTO2 = "photo2";
    public static final String KEY_PHOTO3 = "photo3";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_SERVING = "serving";
    public static final String KEY_COOKING_TIME = "cookingTime";
    public static final String KEY_CHALLENGE_TO = "challengeTo";
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
        return getString(KEY_NAME);
    }

    public void setName(String value) {
        put(KEY_NAME, value);
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
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String value) {
        put(KEY_DESCRIPTION, value);
    }

    // ingredients: List<String> - list of ingredients

    public List<String> getIngredients() {
        return getList(KEY_INGREDIENTS);
    }

    public void setIngredients(List<String> value) {
        put(KEY_INGREDIENTS, value);
    }

    // photo: ParseFile - Recipe's main photo

    public ParseFile getPhoto() {
        return getParseFile(KEY_PHOTO);
    }

    public void setPhoto(ParseFile value) {
        put(KEY_PHOTO, value);
    }

    public ParseFile getPhoto2() {
        return getParseFile(KEY_PHOTO2);
    }
    public void setPhoto2(ParseFile value){
        put(KEY_PHOTO2, value);
    }

    public ParseFile getPhoto3() {
        return getParseFile(KEY_PHOTO3);
    }
    public void setPhoto3(ParseFile value){
        put(KEY_PHOTO3, value);
    }




    // categories: List<String> - List of categories that the recipe belongs to

    public List<String> getCategories() {
        return getList(KEY_CATEGORIES);
    }

    public void setCategories(List<String> value) {
        put(KEY_CATEGORIES, value);
    }

    // steps: List<String> - List of steps (directions)

    public List<String> getSteps() {
        return getList(KEY_STEPS);
    }

    public void setSteps(List<String> value) {
        put(KEY_STEPS, value);
    }

    // serving: long - a quantity of food suitable for how many person.

    public long getServing() {
        return getLong(KEY_SERVING);
    }

    public void setServing(long value) {
        put(KEY_SERVING, value);
    }

    // cookingTime: long - total cooking time

    public long getCookingTime() {
        return getLong(KEY_COOKING_TIME);
    }

    public void setCookingTime(long value) {
        put(KEY_COOKING_TIME, value);
    }

    // challengeTo: Recipe - null in case this is original recipe
    //                     - reference to original recipe

    public Recipe getChallengeTo() {
        return (Recipe) getParseObject(KEY_CHALLENGE_TO);
    }

    public void setChallengeTo(Recipe value) {
        put(KEY_CHALLENGE_TO, value);
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
                .whereEqualTo(KEY_OBJECT_ID, user.getObjectId())
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