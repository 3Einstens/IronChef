package com.einstens3.ironchef.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.einstens3.ironchef.R;

import java.util.ArrayList;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by knyamagoudar on 4/12/17.
 */

public class RecipeDetailRecipeListFragment extends Fragment {


    View view;
    ArrayList<String> stepList = null;
    ArrayAdapter<String> itemsAdapter;
    public static RecipeDetailRecipeListFragment newInstance(ArrayList<String> stepList) {
        Bundle args = new Bundle();
        args.putStringArrayList("stepList", stepList);
        RecipeDetailRecipeListFragment recipeDetailRecipeListFragment = new RecipeDetailRecipeListFragment();
        recipeDetailRecipeListFragment.setArguments(args);
        return recipeDetailRecipeListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_recipedetail_recipielist, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lvRecipeDetailRecipieList);
        itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stepList);
        listView.setAdapter(itemsAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepList = new ArrayList<>();
        log.d("STEPLIST",getArguments().getStringArrayList("stepList").toString());
        stepList.addAll(getArguments().getStringArrayList("stepList"));

    }
}
