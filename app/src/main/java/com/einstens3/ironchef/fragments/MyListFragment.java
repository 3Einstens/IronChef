package com.einstens3.ironchef.fragments;

/**
 * Created by raprasad on 4/9/17.
 */

import android.support.annotation.Nullable;

import com.parse.ParseException;

import android.os.Bundle;

import android.util.Log;


import com.einstens3.ironchef.services.RecipeQuery;
import com.einstens3.ironchef.adapters.RecipeRecyclerAdapter;
import com.einstens3.ironchef.models.Recipe;

import java.util.ArrayList;
import java.util.List;


public class MyListFragment extends HomeFragment  {


    public MyListFragment() {
    }

    @Override
    protected int getLayoutType() {
        return RecipeRecyclerAdapter.MYLIST_RECIPE;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void makeNetworkCall(final int pageNo) {
        queryMyListRecipes();
    }

    private void queryMyListRecipes() {


        new RecipeQuery().queryMyRecipes(new RecipeQuery.QueryRecipesCallback() {
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



