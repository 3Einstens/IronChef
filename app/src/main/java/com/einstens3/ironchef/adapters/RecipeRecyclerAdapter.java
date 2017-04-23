package com.einstens3.ironchef.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.einstens3.ironchef.R;
import com.einstens3.ironchef.activities.ActivityNavigation;
import com.einstens3.ironchef.activities.RecipeDetailActivity;
import com.einstens3.ironchef.models.Challenge;
import com.einstens3.ironchef.models.Recipe;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by raprasad on 4/8/17.
 */
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mRecipe;
    public static final String TAG = RecipeRecyclerAdapter.class.getName();
    public static final int HOME_RECIPE = 0;
    public static final int MYLIST_RECIPE = 1;
    private int mRecipeType;


    public class BasicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;
        @BindView(R.id.tvDescription)
        TextView tvRecipeDescription;
        private int mPosition;

        public BasicViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);

            // NOTE: handle click event with item view
            //       make ivPhoto not clickable to cascade click even to parent view.
            ivPhoto.setClickable(false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRecipeDetailUI(v.getContext(), mPosition, view);
                }
            });
        }

        /**
         * Show Recipe Detail UI with Recipe.objectID.
         */
        private void showRecipeDetailUI(Context context, int index, View view) {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("objectId", mRecipe.get(index).getObjectId());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, view, "recipeDetail");
            context.startActivity(intent,options.toBundle());
        }
    }


    public class HomeViewHolder extends BasicViewHolder {

        @BindView(R.id.tvLike)
        TextView tvLike;
        @BindView(R.id.image_action_like)
        ImageView ivLike;

        public HomeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public class MyListViewHolder extends BasicViewHolder {
        @BindView(R.id.tvBanner)
        TextView tvBanner;

        public MyListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public RecipeRecyclerAdapter(Context context, ArrayList<Recipe> recipes, int recipeType) {
        this.mRecipe = recipes;
        this.mContext = context;
        mRecipeType = recipeType;
    }


    @Override
    public int getItemCount() {
        return this.mRecipe != null ? this.mRecipe.size() : 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if (mRecipeType == HOME_RECIPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipie_cv, parent, false);
            viewHolder = new HomeViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mylist_cv, parent, false);
            viewHolder = new MyListViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            final Recipe r = mRecipe.get(position);
            ((BasicViewHolder) holder).mPosition = position;
            if (holder instanceof BasicViewHolder) {
                BasicViewHolder basicViewHolder = (BasicViewHolder) holder;
                if (r!=null) {
                    basicViewHolder.tvRecipeDescription.setText(r.getName());
                }

                try {

                    if (r.getPhoto() != null) {
                        Glide.with(mContext).load(Uri.fromFile(r.getPhoto().getFile())).into(basicViewHolder.ivPhoto);
                    }
                } catch (ParseException e) {
                    Log.d(TAG, "parse exception: " + e.getMessage());
                }
            }
            if (holder instanceof HomeViewHolder) {
                final HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
                homeViewHolder.tvLike.setText(Integer.toString(0));
                // call count likes asynchronously to improve performance.
                r.countLikes(new CountCallback() {
                    @Override
                    public void done(int count, ParseException e) {
                        if (e == null) {
                            homeViewHolder.tvLike.setText(Integer.toString(count));
                            if (count > 0){
                                int color =  ContextCompat.getColor(mContext, R.color.red);
                                homeViewHolder.ivLike.setColorFilter(color);
                            } else {
                                int color =  ContextCompat.getColor(mContext, R.color.gray);
                                homeViewHolder.ivLike.setColorFilter(color);
                            }
                        }
                        else
                            homeViewHolder.tvLike.setText(0);
                    }
                });
                // Does current logged in user like the recipe?
                r.doesCurrentUserLikeRecipe(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (object != null)
                            homeViewHolder.ivLike.setImageResource(R.drawable.filled_heart);
                        else
                            homeViewHolder.ivLike.setImageResource(R.drawable.heart);
                    }
                });

            } else {
                final MyListViewHolder myListViewHolder = (MyListViewHolder) holder;
                if (r.isOwnRecipe()) {
                    myListViewHolder.tvBanner.setText(mContext.getResources().getString(R.string.created));
                } else {
                    r.getOwnChallenge(new GetCallback<Challenge>() {
                        @Override
                        public void done(final Challenge challenge, ParseException e) {
                            if (e == null && challenge != null) {
                                if (challenge.getState() == Challenge.STATE_ACCEPTED) {
                                    myListViewHolder.tvBanner.setText(mContext.getResources().getString(R.string.accepted));
                                    myListViewHolder.tvBanner.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(mContext instanceof ActivityNavigation){
                                                ((ActivityNavigation)mContext).showComposeUIForChallenge(r.getObjectId(), challenge.getObjectId());
                                            }
                                        }
                                    });
                                } else if (challenge.getState() == Challenge.STATE_COMPLETED) {
                                    myListViewHolder.tvBanner.setText(mContext.getResources().getString(R.string.completed));
                                } else {
                                    myListViewHolder.tvBanner.setVisibility(View.GONE);
                                }
                            } else { //it is showing up here because the user liked this
                                myListViewHolder.tvBanner.setText(mContext.getResources().getString(R.string.liked));
                            }
                        }
                    });
                }
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


}
