package com.einstens3.ironchef.models;

/**
 * Created by raprasad on 4/8/17.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by raprasad on 3/24/17.
 */

public class EdamamRecipe implements Parcelable {

    private String name;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImageUrl;
    }


    public static ArrayList fromJSONArray(JSONArray array){
        ArrayList<EdamamRecipe> recipeList = new ArrayList<>();

        for (int i = 0; i < array.length() ; i++){
            try {
                JSONObject j = array.getJSONObject(i);
                if (j != null){
                    EdamamRecipe r = EdamamRecipe.fromJSON(j);
                    if (r != null){
                        recipeList.add(r);
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
                continue;
            }
        }
        return recipeList;
    }


    public static EdamamRecipe fromJSON(JSONObject json) {
        EdamamRecipe r = null;
        if (json != null) {
            try {
                r = new EdamamRecipe();
                JSONObject o = json.optJSONObject("recipe");
                if (o!= null) {
                    r.name = o.getString("label");
                    r.profileImageUrl = o.getString("image");
                }


            } catch (JSONException e) {
                Log.d("DEBUG", e.getMessage());
            }
        }
        return r;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(profileImageUrl);
    }

    public EdamamRecipe(Parcel source){

        name=source.readString();
        profileImageUrl=source.readString();
    }

    public static final Parcelable.Creator<EdamamRecipe> CREATOR = new Parcelable.Creator<EdamamRecipe>() {
        @Override
        public EdamamRecipe[] newArray(int size) {
            return new EdamamRecipe[size];
        }

        @Override
        public EdamamRecipe createFromParcel(Parcel source) {
            return new EdamamRecipe(source);
        }
    };

    public EdamamRecipe(){}

    @Override
    public int describeContents() {
        return 0;
    }

}






