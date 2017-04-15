package com.einstens3.ironchef.Utilities;

import com.einstens3.ironchef.models.Challenge;
import com.einstens3.ironchef.models.Like;
import com.einstens3.ironchef.models.Recipe;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.parse.ParseQuery.getQuery;

public class RecipeQuery {

    public interface QueryRecipeCallback {
        void success(Recipe recipe);
        void error(ParseException e);
    }

    public interface QueryRecipesCallback {
        void success(List<Recipe> recipes);
        void error(ParseException e);
    }

    /**
     * Get Recipe by the recipeID
     */
    public static void queryRecipe(String recipeID, final QueryRecipeCallback callback) {
        ParseQuery<Recipe> query = getQuery(Recipe.class);
        query.orderByDescending("updatedAt");
        query.getInBackground(recipeID, new GetCallback<Recipe>() {
            @Override
            public void done(Recipe object, ParseException e) {
                if (e == null) {
                    callback.success(object);
                } else {
                    callback.error(e);
                }
            }
        });
    }

    /**
     * Get ALL recipes
     * - Order by `updatedAt` descending order
     */
    public static void queryAllRecipes(final QueryRecipesCallback callback) {
        ParseQuery<Recipe> query = getQuery(Recipe.class);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> objects, ParseException e) {
                if (e == null) {
                    callback.success(objects);
                } else {
                    callback.error(e);
                }
            }
        });
    }

    // should call from background thread
    public static List<Recipe> queryMyRecipes() throws ParseException {
        ParseUser me = ParseUser.getCurrentUser();

        Set<Recipe> recipes = new HashSet<>();

        // Recipes current user composed
        ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.whereEqualTo("author", me);
        recipes.addAll(query.find());

        // Recipes current user likes
        ParseQuery<Like> likeQuery = getQuery(Like.class);
        likeQuery.include(Like.KEY_TO_RECIPE);
        likeQuery.whereEqualTo(Like.KEY_FROM_USER, me);
        List<Like> likes = likeQuery.find();
        for (Like like : likes) {
            if (like.getToRecipe() != null)
                recipes.add(like.getToRecipe());
        }

        // Recipes current user challenges to
        ParseQuery<Challenge> challengeQuery = ParseQuery.getQuery(Challenge.class);
        challengeQuery.include(Challenge.KEY_TO_RECIPE);
        challengeQuery.whereEqualTo(Challenge.KEY_FROM_USER, me);
        List<Challenge> challenges = challengeQuery.find();
        for (Challenge challenge : challenges) {
            if (challenge.getToRecipe() != null)
                recipes.add(challenge.getToRecipe());
        }

        return new ArrayList<>(recipes);
    }

    /**
     * TODO: NOT functioning, Need to fix!!!!
     *
     * Get all MY recipes
     * - Order by `updatedAt` descending order
     */
    public static void queryMyRecipes(final QueryRecipesCallback callback) {
        List<ParseQuery<Recipe>> queries = new ArrayList<>();

        ParseUser me = ParseUser.getCurrentUser();

        // Recipes current user composed
        ParseQuery<Recipe> authorRecipeQuery = ParseQuery.getQuery(Recipe.class);
        authorRecipeQuery.whereEqualTo("author", me);
        queries.add(authorRecipeQuery);

        // Recipes current user likes
        ParseQuery<Like> likeQuery = getQuery(Like.class);
        likeQuery.whereEqualTo(Like.KEY_FROM_USER, me);
        ParseQuery<Recipe> likedRecipeQuery = ParseQuery.getQuery(Recipe.class);
        likedRecipeQuery.whereMatchesKeyInQuery("objectId", Like.KEY_TO_RECIPE, likeQuery);
        queries.add(likedRecipeQuery);

        // Recipes current user likes
        ParseQuery<Challenge> challengeQuery = ParseQuery.getQuery(Challenge.class);
        challengeQuery.whereEqualTo(Challenge.KEY_FROM_USER, me);
        ParseQuery<Recipe> challengeRecipes = ParseQuery.getQuery(Recipe.class);
        challengeRecipes.whereMatchesKeyInQuery("objectId", Challenge.KEY_TO_RECIPE, challengeQuery);
        queries.add(challengeRecipes);

        // Run OR query
        ParseQuery<Recipe> query = ParseQuery.or(queries);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> objects, ParseException e) {
                if (e == null) {
                    callback.success(objects);
                } else {
                    callback.error(e);
                }
            }
        });
    }
}
