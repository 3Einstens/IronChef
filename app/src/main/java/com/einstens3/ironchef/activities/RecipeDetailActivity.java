package com.einstens3.ironchef.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.Utilities.AssetsUtils;
import com.einstens3.ironchef.models.Recipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private String recipeID;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                createHardCodedRecipe();
            }
        });
    }

    // -- create hard-coded recipe
    private void createHardCodedRecipe(){
        // Original Data:
        // Name: Thai Green Curry Chicken
        // URL: http://allrecipes.com/recipe/141833/thai-green-curry-chicken/?internalSource=hub%20recipe&referringContentType=search%20results&clickId=cardslot%201


        ParseFile photo;
        try {
            File file = AssetsUtils.copyFileFromAssetToChacheDir(RecipeDetailActivity.this,
                    "thai_green_curry_chicken_recipe.jpg");
            photo = new ParseFile(file, "image/jpg");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(RecipeDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //  Save Recipe
        final Recipe recipe = new Recipe();
        recipe.setDraft(false);
        recipe.setPublic(false);
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
        recipe.setStep1Text("Toss chicken first in 1 tablespoon dark soy sauce, then in the flour, coating pieces evenly. Heat the oil in a large skillet over medium high heat. Place chicken in the skillet, cook and stir chicken until browned, about 5 minutes. Remove chicken.");
        recipe.setStep2Text("Reduce heat to medium and stir in curry paste. Cook for 1 minute until fragrant, then stir in green onions, garlic, and ginger; cook an additional 2 minutes. Return chicken to the skillet, stirring to coat with the curry mixture. Stir the coconut milk, fish sauce, 1 tablespoon soy sauce, and sugar into the chicken-curry mixture. Allow to simmer over medium heat for 20 minutes until the chicken is tender. Serve garnished with cilantro leaves.");
        recipe.setStep3Text("");
        if(photo!=null)
            recipe.setPhoto(photo);
//        if(step1Photo!=null)
//            recipe.setPhoto(step1Photo);
//        if(step2Photo!=null)
//            recipe.setPhoto(step2Photo);
//        if(step3Photo!=null)
//            recipe.setPhoto(step3Photo);
        recipe.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    recipeID = recipe.getObjectId();
                    Log.e(TAG, "Succeeded to save the Recipe. ID -> " + recipeID);
                    Toast.makeText(RecipeDetailActivity.this, "Succeeded to save the Recipe. ID -> " + recipeID, Toast.LENGTH_LONG).show();

                    fetchRecipe();
                }
                else {
                    Log.e(TAG, "Failed to save the Recipe:" + e, e);
                    Toast.makeText(RecipeDetailActivity.this, "Failed to save the Recipe:" + e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void fetchRecipe(){
        if(recipeID == null){
            Toast.makeText(RecipeDetailActivity.this, "Recipe ID is null.", Toast.LENGTH_LONG).show();
            return;
        }

        ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.getInBackground(recipeID, new GetCallback<Recipe>() {
            @Override
            public void done(Recipe object, ParseException e) {
                if(e == null){
                    recipe = object;
                    Log.e(TAG, recipe.toString());
                }else{
                    Log.e(TAG, "Failed to fetch the Recipe: ID" + recipeID, e);
                    Toast.makeText(RecipeDetailActivity.this, "Failed to fetch the Recipe: ID" + recipeID, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
