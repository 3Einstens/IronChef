package com.einstens3.ironchef.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.fragments.RecipeDetailFragment;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    //private String recipeID;
    //private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                try {
////                    FabricateRecipeData.createHardCodedRecipe(RecipeDetailActivity.this);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    Toast.makeText(RecipeDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
////                }
//
////                FabricateRecipe.createHardCodedRecipe(RecipeDetailActivity.this, new FabricateRecipe.CreateRecipeCallback() {
////                    @Override
////                    public void success(Recipe recipe) {
////                        String recipeID = recipe.getObjectId();
////                        Log.e(TAG, "Succeeded to save the Recipe. ID -> " + recipeID);
////                        Toast.makeText(RecipeDetailActivity.this, "Succeeded to save the Recipe. ID -> " + recipeID, Toast.LENGTH_LONG).show();
////                        // fetchRecipe();
////                    }
////
////                    @Override
////                    public void error(ParseException e) {
////                        Log.e(TAG, "Failed to create the Recipe: " + e.getMessage(), e);
////                        Toast.makeText(RecipeDetailActivity.this, "Failed to create the Recipe: " + e.getMessage(), Toast.LENGTH_LONG).show();
////                    }
////                });
//            }
//        });


        // Create RecipeDetailFragment and set Recipe.objectId
        String objectId = getIntent().getStringExtra("objectId");
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(objectId);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
