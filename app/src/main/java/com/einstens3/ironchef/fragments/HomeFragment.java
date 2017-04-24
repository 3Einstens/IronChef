package com.einstens3.ironchef.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.adapters.RecipeRecyclerAdapter;
import com.einstens3.ironchef.models.Recipe;
import com.einstens3.ironchef.services.RecipeService;
import com.einstens3.ironchef.utilities.EndlessRecyclerViewScrollListener;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements FragmentRefresh {
    @BindView(R.id.rvRecipies)
    RecyclerView rvRecipies;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    protected final String TAG = HomeFragment.class.getName();
    protected ArrayList<Recipe> mArrayList;
    protected RecipeRecyclerAdapter mArrayAdapter;
    protected EndlessRecyclerViewScrollListener scrollListener;
    //protected GridLayoutManager layoutManager;
    protected StaggeredGridLayoutManager layoutManager;
    private String mQueryString;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        int layoutType = getLayoutType();

        mArrayAdapter = new RecipeRecyclerAdapter(getActivity(), mArrayList, layoutType);
        rvRecipies.setAdapter(mArrayAdapter);
        //layoutManager = new GridLayoutManager(getContext(),layoutType == RecipeRecyclerAdapter.HOME_RECIPE ? 2 : 1) ;
        layoutManager = new StaggeredGridLayoutManager(layoutType == RecipeRecyclerAdapter.HOME_RECIPE ? 2 : 1, StaggeredGridLayoutManager.VERTICAL);
        rvRecipies.setLayoutManager(layoutManager);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeNetworkCall(0);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code     is needed to append new items to the bottom of the list
                final int pageNo = page;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        makeNetworkCall(pageNo);

                    }
                });
            }
        };
        // Adds the scroll listener to RecyclerView
        rvRecipies.addOnScrollListener(scrollListener);
        makeNetworkCall(0);

        return v;
    }

    protected void makeNetworkCall(final int pageNo) {
        if (mQueryString == null || mQueryString.isEmpty()) {
            queryAllRecipe();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrayList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) mSearchMenuItem.getActionView();


        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mQueryString = null;
                makeNetworkCall(0);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //    new RecipeService().queryAllRecipes(new RecipeService.QueryRecipesCallback()
                mQueryString = query;
                new RecipeService().queryRecipesByKeyword(query, new RecipeService.QueryRecipesCallback() {
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

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected void queryAllRecipe() {
        // search example:
        new RecipeService().queryOriginalRecipes(new RecipeService.QueryRecipesCallback() {
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

    protected int getLayoutType() {
        return RecipeRecyclerAdapter.HOME_RECIPE;
    }

    @Override
    public void update() {
        makeNetworkCall(0);
    }
}


