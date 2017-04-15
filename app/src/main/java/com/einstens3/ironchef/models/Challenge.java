package com.einstens3.ironchef.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Locale;

import static com.parse.ParseQuery.getQuery;

@ParseClassName("Challenge")
public class Challenge extends ParseObject {
    // ------------------------------------
    //  Constants
    // ------------------------------------
    public static final String TAG = Challenge.class.getSimpleName();

    // access keys
    public static final String KEY_FROM_USER = "fromUser";
    public static final String KEY_TO_RECIPE = "toRecipe";
    public static final String KEY_MY_RECIPE = "myRecipe";
    public static final String KEY_STATE = "state";

    // states
    public final static int STATE_ACCEPTED = 0;
    public final static int STATE_COMPLETED = 1;


    // ------------------------------------
    //  Static convenient methods
    // ------------------------------------

    public static Challenge acceptChallenge(Recipe recipe) {
        return acceptChallenge(ParseUser.getCurrentUser(), recipe);
    }

    public static Challenge acceptChallenge(ParseUser user, Recipe recipe) {
        Challenge challenge = new Challenge();
        challenge.setFromUser(user);
        challenge.setToRecipe(recipe);
        challenge.setState(STATE_ACCEPTED);
        challenge.saveInBackground(null);
        return challenge;
    }

    public static int countChallengesForRecipe(Recipe recipe) {
        try {
            return getQuery(Challenge.class)
                    .whereEqualTo(KEY_TO_RECIPE, recipe)
                    .count();
        } catch (ParseException e) {
            Log.e(TAG, "Error in countChallengesForRecipe() recipe: " + recipe, e);
            return -1;
        }
    }
    // ------------------------------------
    //  Constructors
    // ------------------------------------

    public Challenge() {
    }

    // ------------------------------------
    //  Getters/Setters
    // ------------------------------------

    public ParseUser getFromUser() {
        return getParseUser(KEY_FROM_USER);
    }

    public void setFromUser(ParseUser value) {
        put(KEY_FROM_USER, value);
    }

    public Recipe getToRecipe() {
        return (Recipe) getParseObject(KEY_TO_RECIPE);
    }

    public void setToRecipe(Recipe value) {
        put(KEY_TO_RECIPE, value);
    }

    public Recipe getMyRecipe() {
        return (Recipe) getParseObject(KEY_MY_RECIPE);
    }

    public void setMyRecipe(Recipe value) {
        put(KEY_MY_RECIPE, value);
    }

    public String getStateString() {
        return getState() == STATE_ACCEPTED ? "ACCEPTED" : "COMPLETED";
    }

    public int getState() {
        return getInt(KEY_STATE);
    }

    public void setState(int value) {
        put(KEY_STATE, value);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s{%s: %s, %s: %s, %s: %s, %s: %s}",
                Challenge.class.getSimpleName(),
                KEY_FROM_USER, getFromUser() == null ? "null" : getFromUser().getObjectId(),
                KEY_TO_RECIPE, getToRecipe() == null ? "null" : getToRecipe().getObjectId(),
                KEY_MY_RECIPE, getMyRecipe() == null ? "null" : getMyRecipe().getObjectId(),
                KEY_STATE, getStateString());
    }
}
