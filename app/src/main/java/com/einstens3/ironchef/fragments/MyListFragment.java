package com.einstens3.ironchef.fragments;

/**
 * Created by raprasad on 4/9/17.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.einstens3.ironchef.activities.HomeActivity;
import com.parse.ParseException;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import static com.einstens3.ironchef.Utilities.RecipeQuery.queryMyRecipes;

public class MyListFragment extends  HomeFragment {




    public MyListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        rvRecipies.setAdapter(mArrayAdapter);
        // gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvRecipies.setLayoutManager(mLayoutManager);
        //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        //rvRecipies.addItemDecoration(itemDecoration);
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


        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
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


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       queryAllRecipe();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrayList = new ArrayList<>();
        mArrayAdapter = new RecipeRecyclerAdapter(getActivity(), mArrayList, RecipeRecyclerAdapter.MYLIST_RECIPE);
    }


    private void queryAllRecipe(){
        new MyListTask().execute();
    /*    new RecipeQuery().queryAllRecipes(new RecipeQuery.QueryRecipesCallback() {
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
        });*/
    }



    private class MyListTask extends AsyncTask<String, Void, List<Recipe>> {


        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator

           swipeContainer.setVisibility(ProgressBar.VISIBLE);
        }

        protected List<Recipe> doInBackground(String... strings) {
            // Some long-running task like downloading an image.
            List<Recipe> recipes = null;
            RecipeQuery q = new RecipeQuery();
            try {
              recipes  = q.queryMyRecipes();
            } catch (ParseException e){
                Log.e(TAG, e.getMessage());
            }

            return recipes;

        }


        protected void onPostExecute(List<Recipe> recipes) {
            // This method is executed in the UIThread
            // with access to the result of the long running task

            // Hide the progress bar
            if (recipes != null) {
                mArrayList = mArrayAdapter.swap(new ArrayList(recipes));
            }
            swipeContainer.setVisibility(ProgressBar.INVISIBLE);
        }
    }

}



