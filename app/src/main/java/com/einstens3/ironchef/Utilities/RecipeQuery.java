package com.einstens3.ironchef.Utilities;

import com.einstens3.ironchef.models.Recipe;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class RecipeQuery {

    public interface QueryRecipeCallback {
        void success(Recipe recipe);

        void error(ParseException e);
    }

    public void queryRecipe(String recipeID, final QueryRecipeCallback callback) {
        if (recipeID == null)
            throw new IllegalArgumentException("recipeID is null");

        ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
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

    public interface QueryRecipesCallback {
        void success(List<Recipe> recipes);

        void error(ParseException e);
    }

    public void queryAllRecipes(final QueryRecipesCallback callback) {
        ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
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
