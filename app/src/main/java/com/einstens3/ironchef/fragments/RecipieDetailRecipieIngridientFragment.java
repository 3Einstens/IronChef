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
 * Created by knyamagoudar on 4/15/17.
 */

public class RecipieDetailRecipieIngridientFragment extends Fragment{

    View view;
    ArrayList<String> ingridients = null;
    ArrayAdapter<String> itemsAdapter;
    public static RecipieDetailRecipieIngridientFragment newInstance(ArrayList<String> ingridients) {
        Bundle args = new Bundle();
        args.putStringArrayList("ingridients", ingridients);
        RecipieDetailRecipieIngridientFragment recipieDetailRecipieIngridientFragment = new RecipieDetailRecipieIngridientFragment();
        recipieDetailRecipieIngridientFragment.setArguments(args);
        return recipieDetailRecipieIngridientFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_recipedetail_recipielist, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lvRecipeDetailRecipieList);
        itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ingridients);
        listView.setAdapter(itemsAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingridients = new ArrayList<>();
        log.d("INGRIDIENTS",getArguments().getStringArrayList("ingridients").toString());
        ingridients.addAll(getArguments().getStringArrayList("ingridients"));
    }
}
