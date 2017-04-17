package com.einstens3.ironchef.utilities;

import android.content.Context;
import android.widget.Toast;

import com.einstens3.ironchef.models.Recipe;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FabricateRecipe {

    public interface CreateRecipeCallback {
        void success(Recipe recipe);

        void error(ParseException e);
    }

    // -- create hard-coded recipe
    public static void createHardCodedRecipe(Context context, final CreateRecipeCallback callback) {
        // Original Data:
        // Name: Thai Green Curry Chicken
        // URL: http://allrecipes.com/recipe/141833/thai-green-curry-chicken/?internalSource=hub%20recipe&referringContentType=search%20results&clickId=cardslot%201

        ParseFile photo;
        try {
            File file = AssetsUtils.copyFileFromAssetToChacheDir(context, "thai_green_curry_chicken_recipe.jpg");
            photo = new ParseFile(file, "image/jpg");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //  Save Recipe
        final Recipe recipe = new Recipe();
        recipe.setAuthor(ParseUser.getCurrentUser());
        recipe.setName("Thai Green Curry Chicken");
        recipe.setDescription("In this quick and easy recipe that never fails, the chicken stays moist and tender. Serve over jasmine rice for a satisfying meal.");
        recipe.setCookingTime(60);
        recipe.setServing(4);
        recipe.setIngredients(Arrays.asList("1 pound skinless, boneless chicken breast halves - cut into 1 inch cubes",
                "1 tablespoon dark soy sauce",
                "1 tablespoon all-purpose flour",
                "2 tablespoons cooking oil",
                "2 tablespoons green curry paste",
                "2 green onions with tops, chopped",
                "3 cloves garlic, peeled and chopped",
                "1 teaspoon fresh ginger, peeled and finely chopped",
                "2 cups coconut milk",
                "1 tablespoon fish sauce",
                "1 tablespoon dark soy sauce",
                "2 tablespoons white sugar",
                "1/2 cup cilantro leaves, for garnish"));
        recipe.setCategories(Arrays.asList("Thai"));
        if (photo != null)
            recipe.setPhoto(photo);
        recipe.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    callback.success(recipe);
                } else {
                    callback.error(e);
                }
            }
        });
    }
}
