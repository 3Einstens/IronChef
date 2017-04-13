package com.einstens3.ironchef.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

@ParseClassName("Like")
public class Like extends ParseObject {
    /**
     * - fromUser: ParseUser
     * - toRecipe: Recipe
     */

    // ------------------------------------
    //  Constants
    // ------------------------------------
    public static final String KEY_FROM_USER = "fromUser";
    public static final String KEY_TO_RECIPE = "toRecipe";

    // ------------------------------------
    //  Static inconvenient methods
    // ------------------------------------

    public static Like likeRecipe(Recipe recipe) {
        return likeRecipe(ParseUser.getCurrentUser(), recipe);
    }

    public static Like likeRecipe(ParseUser user, Recipe recipe) {
        Like like = new Like();
        like.setFromUser(user);
        like.setToRecipe(recipe);
        like.saveInBackground(null);
        return like;
    }

    public static void unlikeRecipe(Recipe recipe) {
        unlikeRecipe(ParseUser.getCurrentUser(), recipe);
    }

    public static void unlikeRecipe(ParseUser user, Recipe recipe) {
        ParseQuery
                .getQuery(Like.class)
                .whereEqualTo(KEY_FROM_USER, user)
                .whereEqualTo(KEY_TO_RECIPE, recipe)
                .findInBackground(new FindCallback<Like>() {
                    @Override
                    public void done(List<Like> likes, ParseException e) {
                        for (Like like : likes) {
                            like.deleteInBackground();
                        }
                    }
                });
    }


    public static void likesForRecipe(Recipe recipe) {
        ParseQuery
                .getQuery(Like.class)
                .whereEqualTo(KEY_TO_RECIPE, recipe)
                .findInBackground(new FindCallback<Like>() {
                    @Override
                    public void done(List<Like> likes, ParseException e) {
                        for (Like like : likes) {
                            like.deleteInBackground();
                        }
                    }
                });
    }

    public static int countLikesForRecipe(Recipe recipe) {
        try {
            return ParseQuery
                    .getQuery(Like.class)
                    .whereEqualTo(KEY_TO_RECIPE, recipe).count();
        } catch (ParseException e) {
            Log.e(TAG, "Error in countLikesForRecipe() recipe: " + recipe, e);
            return -1;
        }
    }

    // ------------------------------------
    //  Constructors
    // ------------------------------------

    public Like() {
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

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s{%s: %s, %s: %s}",
                Like.class.getSimpleName(),
                KEY_FROM_USER, getFromUser().getUsername(),
                KEY_TO_RECIPE, getToRecipe().getName());
    }
}
