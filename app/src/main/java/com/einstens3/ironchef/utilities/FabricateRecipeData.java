package com.einstens3.ironchef.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.einstens3.ironchef.models.Recipe;
import com.parse.ParseFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import static com.einstens3.ironchef.utilities.AssetsUtils.fromAssetFile;


public class FabricateRecipeData {
    private static final String TAG = FabricateRecipe.class.getSimpleName();

    public static void createHardCodedRecipe(final Context context) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = fromAssetFile(context, "data.json");
                    JSONArray jsonRecipes = new JSONArray(json);
                    for (int i = 0; i < jsonRecipes.length(); i++) {
                        JSONObject jsonRecipe = jsonRecipes.getJSONObject(i).getJSONObject("recipe");
                        String label;
                        String image;
                        label = jsonRecipe.getString("label");
                        image = jsonRecipe.getString("image");
                        if (label != null && image != null) {
                            // photo
                            String filename = String.format(Locale.ENGLISH, "%04d.jpg", i);
                            File tmpFile = new File(context.getCacheDir(), filename);
                            HttpUtils.download(new URL(image), tmpFile);
                            ParseFile parseFile = new ParseFile(tmpFile, "image/jpg");
                            parseFile.save();
                            // recipe
                            Recipe recipe = new Recipe();
                            recipe.setName(label);
                            recipe.setServing(4);
                            recipe.setCookingTime(35);
                            //recipe.setAuthor(ParseUser.getCurrentUser());
                            recipe.setCategories(Arrays.asList("Italian"));
                            recipe.setIngredients(Arrays.asList("sugar", "salt"));
                            recipe.setPhoto(parseFile);
                            recipe.save();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
