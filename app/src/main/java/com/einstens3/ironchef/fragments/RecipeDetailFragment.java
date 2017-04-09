package com.einstens3.ironchef.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.Utilities.RecipeQuery;
import com.einstens3.ironchef.models.Recipe;
import com.parse.ParseException;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private static final String ID = "eqsYIVD78S";

    View view;
    ImageView ivPhoto;
    TextView tvTitle;
    TextView tvDescription;
    TextView tvAuthor;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        bindControls();
        updateControlStates();
        setEventHandlers();
        return this.view;
    }
    private void bindControls() {
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
    }

    private void updateControlStates() {
        new RecipeQuery().queryRecipe(ID, new RecipeQuery.QueryRecipeCallback() {
            @Override
            public void success(Recipe recipe) {
                try {
                    tvTitle.setText(recipe.getName());
                    tvDescription.setText(recipe.getDescription());
                    tvAuthor.setText(recipe.getAuthor().fetchIfNeeded().getUsername());
                    ivPhoto.setImageURI(Uri.fromFile(recipe.getPhoto().getFile()));
                } catch (ParseException e) {
                    Log.e(TAG, "Parse Error", e);
                    Toast.makeText(getContext(), "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error(ParseException e) {
                Toast.makeText(getContext(), "Failed to obtain Recipe for ID: " + ID, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setEventHandlers() {

    }
}
