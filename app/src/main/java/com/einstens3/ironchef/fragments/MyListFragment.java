package com.einstens3.ironchef.fragments;

/**
 * Created by raprasad on 4/9/17.
 */

import android.support.v4.app.Fragment;

import com.parse.ParseException;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.Utilities.EndlessRecyclerViewScrollListener;
import com.einstens3.ironchef.Utilities.RecipeQuery;
import com.einstens3.ironchef.adapters.RecipeRecyclerAdapter;
import com.einstens3.ironchef.models.Recipe;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Build.VERSION_CODES.M;

public class MyListFragment extends  HomeFragment {

    public MyListFragment() {
        queryAllRecipe();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void queryAllRecipe(){
        new RecipeQuery().queryAllRecipes(new RecipeQuery.QueryRecipesCallback() {
            @Override
            public void success(List<Recipe> recipes) {
                mArrayList = mArrayAdapter.swap(new ArrayList(recipes));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void error(ParseException e) {
                Log.e("ERROR", "Query Error", e);
                swipeContainer.setRefreshing(false);
            }
        });
    }
}



