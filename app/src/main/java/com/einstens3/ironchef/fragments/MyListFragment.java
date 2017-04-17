package com.einstens3.ironchef.fragments;

/**
 * Created by raprasad on 4/9/17.
 */

import android.util.Log;
import android.view.Menu;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.adapters.RecipeRecyclerAdapter;
import com.einstens3.ironchef.models.Recipe;
import com.einstens3.ironchef.services.RecipeService;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends HomeFragment {
    public MyListFragment() {
    }

    @Override
    protected int getLayoutType() {
        return RecipeRecyclerAdapter.MYLIST_RECIPE;
    }

    @Override
    protected void makeNetworkCall(final int pageNo) {
        queryMyListRecipes();
    }

    private void queryMyListRecipes() {
        new RecipeService().queryMyRecipes(new RecipeService.QueryRecipesCallback() {
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.action_search);
    }
}
