package com.einstens3.ironchef.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.Utilities.FabricateRecipeData;
import com.einstens3.ironchef.models.Recipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

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
                try {
                    FabricateRecipeData.createHardCodedRecipe(RecipeDetailActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RecipeDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

//                FabricateRecipe.createHardCodedRecipe(RecipeDetailActivity.this, new FabricateRecipe.CreateRecipeCallback(){
//                    @Override
//                    public void success(Recipe recipe) {
//                        recipeID = recipe.getObjectId();
//                        Log.e(TAG, "Succeeded to save the Recipe. ID -> " + recipeID);
//                        Toast.makeText(RecipeDetailActivity.this, "Succeeded to save the Recipe. ID -> " + recipeID, Toast.LENGTH_LONG).show();
//                        fetchRecipe();
//                    }
//
//                    @Override
//                    public void error(ParseException e) {
//                        Log.e(TAG, "Failed to fetch the Recipe: ID" + recipeID, e);
//                        Toast.makeText(RecipeDetailActivity.this, "Failed to fetch the Recipe: ID" + recipeID, Toast.LENGTH_LONG).show();
//                    }
//                });
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
