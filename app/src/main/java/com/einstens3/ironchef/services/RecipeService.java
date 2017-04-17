package com.einstens3.ironchef.services;

import com.einstens3.ironchef.models.Challenge;
import com.einstens3.ironchef.models.Recipe;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.einstens3.ironchef.models.Recipe.KEY_AUTHOR;
import static com.einstens3.ironchef.models.Recipe.KEY_CATEGORIES;
import static com.einstens3.ironchef.models.Recipe.KEY_CHALLENGE_TO;
import static com.einstens3.ironchef.models.Recipe.KEY_NAME;
import static com.einstens3.ironchef.models.Recipe.KEY_UPDATED_AT;
import static com.parse.ParseQuery.getQuery;

public class RecipeService {

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
        query.orderByDescending(KEY_UPDATED_AT);
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

    /**
     * search query by keyword
     */
    public static void queryRecipesByKeyword(final String regex, final QueryRecipesCallback callback) {
        List<ParseQuery<Recipe>> queries = new ArrayList<>();

        // query in the name field
        ParseQuery<Recipe> queryInNameField = getQuery(Recipe.class);
        queryInNameField.whereMatches(KEY_NAME, ".*" + regex + ".*");
        queries.add(queryInNameField);

        // query in the categories field
        ParseQuery<Recipe> queryInCategoryField = getQuery(Recipe.class);
        queryInCategoryField.whereMatches(KEY_CATEGORIES, ".*" + regex + ".*");
        queries.add(queryInCategoryField);

        // Run OR query
        ParseQuery<Recipe> query = ParseQuery.or(queries);
        query.orderByDescending(KEY_UPDATED_AT);
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

    /**
     * Get all MY recipes
     * - Order by `updatedAt` descending order
     */
    public static void queryMyRecipes(final QueryRecipesCallback callback) {
        List<ParseQuery<Recipe>> queries = new ArrayList<>();

        ParseUser me = ParseUser.getCurrentUser();

        // Recipes current user composed
        ParseQuery<Recipe> authorRecipeQuery = ParseQuery.getQuery(Recipe.class);
        authorRecipeQuery.whereEqualTo(KEY_AUTHOR, me);
        queries.add(authorRecipeQuery);

        // Recipes current user likes
        ParseQuery<Recipe> likedRecipeQuery = ParseQuery.getQuery(Recipe.class);
        likedRecipeQuery.whereContainedIn(Recipe.KEY_LIKES, Arrays.asList(me));
        queries.add(likedRecipeQuery);

        // Recipes current user likes
        ParseQuery<Challenge> challengeQuery = ParseQuery.getQuery(Challenge.class);
        challengeQuery.whereEqualTo(Challenge.KEY_USER, me);
        ParseQuery<Recipe> challengeRecipes = ParseQuery.getQuery(Recipe.class);
        challengeRecipes.whereMatchesQuery(Recipe.KEY_CHALLENGES, challengeQuery);
        queries.add(challengeRecipes);

        // Run OR query
        ParseQuery<Recipe> query = ParseQuery.or(queries);
        query.orderByDescending(KEY_UPDATED_AT);
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

    /**
     * Get recipes that challenge to the specified recipe
     * - Order by `updatedAt` descending order
     */
    public static void queryChallengeRecipes(String recipeID, final QueryRecipesCallback callback) {
        ParseQuery<Recipe> query = getQuery(Recipe.class);
        query.whereEqualTo(KEY_CHALLENGE_TO, Recipe.createWithoutData(Recipe.class, recipeID));
        query.orderByDescending(KEY_UPDATED_AT);
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

    /**
     * Get all Original recipes
     * - Order by `updatedAt` descending order
     */
    public static void queryOriginalRecipes(final QueryRecipesCallback callback) {
        ParseQuery<Recipe> query = getQuery(Recipe.class);
        query.whereDoesNotExist(KEY_CHALLENGE_TO);
        query.orderByDescending(KEY_UPDATED_AT);
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
