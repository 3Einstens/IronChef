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
import static com.parse.ParseQuery.getQuery;

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

    /**
     * Like the Recipe
     *
     * @param recipe
     * @return
     */
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

    /**
     * UnLike the Recipe
     *
     * @param recipe
     */
    public static void unlikeRecipe(Recipe recipe) {
        unlikeRecipe(ParseUser.getCurrentUser(), recipe);
    }

    public static void unlikeRecipe(ParseUser user, Recipe recipe) {
        getQuery(Like.class)
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
        getQuery(Like.class)
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

    /**
     * Get List of Likes for the recipe
     *
     * @param recipe
     * @return
     */
    public static List<Like> likes(Recipe recipe) {
        try {
            return
                    getQuery(Like.class)
                            .whereEqualTo(KEY_TO_RECIPE, recipe)
                            .find();
        } catch (ParseException e) {
            Log.e(TAG, "Error in likes() recipe: " + recipe, e);
            return null;
        }
    }

    /**
     * Get number of Likes for the recipe
     *
     * @param recipe
     * @return
     */
    public static int countLikesForRecipe(Recipe recipe) {
        try {
            return
                    getQuery(Like.class)
                            .whereEqualTo(KEY_TO_RECIPE, recipe).count();
        } catch (ParseException e) {
            Log.e(TAG, "Error in countLikesForRecipe() recipe: " + recipe, e);
            return -1;
        }
    }

    /**
     * Does the current user like the recipe?
     */
    public static boolean doesUserLikeRecipe(Recipe recipe) {
        return doesUserLikeRecipe(ParseUser.getCurrentUser(), recipe);
    }

    /**
     * Does the user like the recipe?
     */
    public static boolean doesUserLikeRecipe(ParseUser user, Recipe recipe) {
        try {
            ParseQuery
                    .getQuery(Like.class)
                    .whereEqualTo(KEY_FROM_USER, user)
                    .whereEqualTo(KEY_TO_RECIPE, recipe)
                    .getFirst();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Toggle Like state for current user
     *
     * @param recipe
     * @return state after toggled
     */
    public static boolean toggleLike(Recipe recipe) {
        return toggleLike(ParseUser.getCurrentUser(), recipe);
    }

    /**
     * Toggle Like state
     *
     * @param user
     * @param recipe
     * @return state after toggled
     */
    public static boolean toggleLike(ParseUser user, Recipe recipe) {
        if (doesUserLikeRecipe(user, recipe)) {
            unlikeRecipe(user, recipe);
            return false;
        } else {
            likeRecipe(user, recipe);
            return true;
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
