package com.einstens3.ironchef.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Locale;

@ParseClassName("Challenge")
public class Challenge extends ParseObject {
    // ------------------------------------
    //  Constants
    // ------------------------------------
    public static final String TAG = Challenge.class.getSimpleName();

    // access keys
    public static final String KEY_USER = "user";
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

    public static Challenge acceptChallenge(ParseUser user, final Recipe recipe) {
        final Challenge challenge = new Challenge();
        challenge.setUser(user);
        challenge.setState(STATE_ACCEPTED);
        challenge.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                recipe.addChallenge(challenge);
            }
        });
        return challenge;
    }

    // ------------------------------------
    //  Constructors
    // ------------------------------------

    public Challenge() {
    }

    // ------------------------------------
    //  Getters/Setters
    // ------------------------------------

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser value) {
        put(KEY_USER, value);
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
        return String.format(Locale.ENGLISH, "%s{%s: %s, %s: %s, %s: %s}",
                Challenge.class.getSimpleName(),
                KEY_USER, getUser() == null ? "null" : getUser().getObjectId(),
                KEY_MY_RECIPE, getMyRecipe() == null ? "null" : getMyRecipe().getObjectId(),
                KEY_STATE, getStateString());
    }
}
