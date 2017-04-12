package com.einstens3.ironchef.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Locale;

@ParseClassName("Like")
public class Like extends ParseObject {
    /**
     * - fromUser: ParseUser
     * - toRecipe: Recipe
     */

    public static final String KEY_FROM_USER = "fromUser";
    public static final String KEY_TO_RECIPE = "toRecipe";

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

    public Like() {
    }

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
