package com.einstens3.ironchef.adapters;

/**
 * Created by raprasad on 4/8/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.activities.RecipeDetailActivity;
import com.einstens3.ironchef.models.Recipe;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by raprasad on 4/2/17.
 */


/**
 * Created by raprasad on 3/24/17.
 */

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mRecipe;
    public static final String TAG = RecipeRecyclerAdapter.class.getName();


    public class BasicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivRecipe;
        @BindView(R.id.tvDescription)
        TextView tvRecipeDescription;
        private int mPosition;

        public BasicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            // NOTE: handle click event with item view
            //       make ivRecipe not clickable to cascade click even to parent view.
            ivRecipe.setClickable(false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRecipeDetailUI(v.getContext(), mPosition);
                }
            });
        }

        public void renderRecipe(Recipe r) {
            ivRecipe.setImageResource(0);
            if (r != null) {
                tvRecipeDescription.setText(r.getName());
                try {
                    ivRecipe.setImageURI(Uri.fromFile(r.getPhoto().getFile()));
                } catch (ParseException e){
                    Log.d(TAG, "parse exception: " + e.getMessage());
                }
              /*  Glide.with(ivRecipe.getContext())
                        .load(r.getProfileImage()).into(ivRecipe);*/
            }
        }
    }


    public RecipeRecyclerAdapter(Context context, ArrayList<Recipe> recipes) {
        this.mRecipe = recipes;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.mRecipe != null ? this.mRecipe.size() : 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipie_cv, parent, false);
        return new BasicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (holder instanceof BasicViewHolder) {
                ((BasicViewHolder) holder).renderRecipe(mRecipe.get(position));
                ((BasicViewHolder) holder).mPosition = position;
            }
        }
    }

    public ArrayList<Recipe> add(ArrayList<Recipe> recipe) {
        mRecipe.addAll(recipe);
        notifyDataSetChanged();
        return mRecipe;
    }

    public ArrayList<Recipe> addRecipe(Recipe recipe) {
        mRecipe.add(0, recipe);
        notifyDataSetChanged();
        return mRecipe;
    }

    public ArrayList<Recipe> swap(ArrayList<Recipe> recipes) {
        mRecipe.clear();
        mRecipe.addAll(recipes);
        notifyDataSetChanged();
        return mRecipe;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    public int getItemViewType(int position) {
        //Recipe r = mRecipe.get(position);
        return 0;
    }

    /**
     * Show Recipe Detail UI with Recipe.objectID.
     */
    private void showRecipeDetailUI(Context context, int index) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra("objectId", mRecipe.get(index).getObjectId());
        context.startActivity(intent);
    }
}

