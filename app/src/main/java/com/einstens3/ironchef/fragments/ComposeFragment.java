package com.einstens3.ironchef.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.einstens3.ironchef.R;
import com.einstens3.ironchef.activities.ActivityResult;
import com.einstens3.ironchef.activities.RecipeDetailActivity;
import com.einstens3.ironchef.models.Challenge;
import com.einstens3.ironchef.models.Recipe;
import com.einstens3.ironchef.services.ChallengeService;
import com.einstens3.ironchef.services.RecipeService;
import com.einstens3.ironchef.utilities.StringUtils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.einstens3.ironchef.R.id.llStepListCompose;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComposeFragment extends Fragment {

    private static final String TAG = ComposeFragment.class.getSimpleName();

    private static final String ARG_PARAM_CHALLENGE_TO = "challengeTo"; // Recipe.objectId
    private static final String ARG_PARAM_CHALLENGE_ID = "challengeId"; // Challenge.objectId

    private static final String PHOTO_NAME = "photo"; // main photo
    private static final String PHOTO_NAME2 = "photo2";
    private static final String PHOTO_NAME3 = "photo3";
    private static final String STEP_PHOTO_NAME = "step_%d"; // Step photo

    // information for challenge. If normal compose, following two parameters will be `null`.
    String challengeTo;
    String challengeId;

    View view;

    //Button btnCancel;

    ScrollView svCompose;

    LinearLayout llImages;
    ImageView ivUploadPhoto;
    ImageButton ibUploadPhoto;
    ImageView ivUploadPhoto2;
    ImageButton ibUploadPhoto2;
    ImageView ivUploadPhoto3;
    ImageButton ibUploadPhoto3;

    TextInputLayout title_text_input_layout;
    EditText etTitle;
    EditText etDescription;
    EditText etCategories;
    EditText etPrepTime;
    EditText etServing;
    ImageButton ibAddSteps;
    ImageButton ibAddIngridient;

    RelativeLayout rlChallenge;
    ImageView ivChallengePhoto;
    TextView tvChallengeRecipeName;

    public ComposeFragment() {
    }

    public static ComposeFragment newInstance(String challengeTo, String challengeId) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_CHALLENGE_TO, challengeTo);
        args.putString(ARG_PARAM_CHALLENGE_ID, challengeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challengeTo = getArguments().getString(ARG_PARAM_CHALLENGE_TO);
            challengeId = getArguments().getString(ARG_PARAM_CHALLENGE_ID);
            Log.e(TAG, "challengeId -> " + challengeId + ", challengeTo -> " + challengeTo);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_compose, container, false);
        bindControls();
        updateControlStates();
        setEventHandlers();
        return view;
    }


    private void bindControls() {


        //btnCancel = (Button) view.findViewById(R.id.btnCancel);

        svCompose = (ScrollView)view.findViewById(R.id.svCompose);

        llImages = (LinearLayout)view.findViewById(R.id.llImages);
        ivUploadPhoto = (ImageView) view.findViewById(R.id.ivUploadPhoto);
        ibUploadPhoto = (ImageButton) view.findViewById(R.id.ibUploadPhoto);

        ivUploadPhoto2 = (ImageView) view.findViewById(R.id.ivUploadPhoto2);
        ibUploadPhoto2 = (ImageButton) view.findViewById(R.id.ibUploadPhoto2);

        ivUploadPhoto3 = (ImageView) view.findViewById(R.id.ivUploadPhoto3);
        ibUploadPhoto3 = (ImageButton) view.findViewById(R.id.ibUploadPhoto3);

        title_text_input_layout = (TextInputLayout)view.findViewById(R.id.title_text_input_layout) ;
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etCategories = (EditText) view.findViewById(R.id.etCategories);
        etPrepTime = (EditText) view.findViewById(R.id.etPrepTime);
        etServing = (EditText) view.findViewById(R.id.etServing);

        ibAddSteps = (ImageButton) view.findViewById(R.id.ibAddSteps);
        ibAddSteps.setTag(1);

        ibAddIngridient = (ImageButton) view.findViewById(R.id.ibAddIngridients);
        ibAddIngridient.setTag(1);

        rlChallenge = (RelativeLayout)view.findViewById(R.id.rlChallenge);
        ivChallengePhoto= (ImageView)view.findViewById(R.id.ivChallengePhoto);
        tvChallengeRecipeName = (TextView)view.findViewById(R.id.tvChallengeRecipeName);
    }

    // To prevent auto scroll to EditText in ScrollView.
    // http://stackoverflow.com/questions/8100831/stop-scrollview-from-setting-focus-on-edittext
    private void scrollViewHack(){
        svCompose.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        svCompose.setFocusable(true);
        svCompose.setFocusableInTouchMode(true);
        svCompose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }
    private void updateControlStates() {
        ibUploadPhoto.setVisibility(View.VISIBLE);
        ivUploadPhoto.setVisibility(View.INVISIBLE);

        scrollViewHack();

        // update if challenge
        if (challengeId != null) {
            ChallengeService.getChallenge(challengeId, new GetCallback<Challenge>() {
                @Override
                public void done(Challenge challenge, ParseException e) {
                    if (getActivity() != null &&  ((AppCompatActivity) getActivity()).getSupportActionBar() != null ) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Challenge Recipe");
                    }
                }
            });
        }
        if (challengeTo != null) {
            RecipeService.queryRecipe(challengeTo, new RecipeService.QueryRecipeCallback() {
                @Override
                public void success(final Recipe recipe) {

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Challenge Recipe");

                    rlChallenge.setVisibility(View.VISIBLE);
                    title_text_input_layout.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llImages.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.rlChallenge);

                    tvChallengeRecipeName.setText(recipe.getName());
                    etTitle.setText(recipe.getName());
                    etCategories.setText(StringUtils.fromList(recipe.getCategories()));
                    etDescription.setText(recipe.getDescription());
                    etPrepTime.setText(String.valueOf(recipe.getCookingTime()));
                    etServing.setText(String.valueOf(recipe.getServing()));
                    if (recipe.getSteps() != null) {
                        for (String step : recipe.getSteps()) {
                            EditText et = addDynamicEditTexts(llStepListCompose, "Add Step");
                            et.setText(step);
                        }
                    }
                    if (recipe.getIngredients() != null) {
                        for (String ingredient : recipe.getIngredients()) {
                            EditText et = addDynamicEditTexts(R.id.llIngridentsCompose, "Add Step");
                            et.setText(ingredient);
                        }
                    }

                    try {
                        Glide.with(getContext()).load(Uri.fromFile(recipe.getPhoto().getFile())).into(ivChallengePhoto);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ivChallengePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /**
                             * Show Recipe Detail UI with Recipe.objectID.
                             */
                            Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                            intent.putExtra("objectId", recipe.getObjectId());
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) getContext(), ivChallengePhoto, "recipeDetail");
                            getContext().startActivity(intent, options.toBundle());

                        }
                    });


                    etTitle.setEnabled(false);
                    etCategories.setEnabled(false);
                }

                @Override
                public void error(ParseException e) {
                }
            });
        }
    }

    private void setEventHandlers() {
        ibUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(0, PHOTO_NAME);
            }
        });

        ibUploadPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(1, PHOTO_NAME);
            }
        });

        ibUploadPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(2, PHOTO_NAME);
            }
        });

        ibAddSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo Dynamically add new editText
                if (Integer.parseInt(v.getTag().toString()) == 1) {
                    Toast.makeText(getContext(), "ADD", Toast.LENGTH_SHORT).show();
                    v.setTag(2);
                    addDynamicEditTexts(llStepListCompose, "Step(s)");
                    v.setBackgroundResource(R.drawable.cancel);
                } else {
                    LinearLayout linearParent = (LinearLayout) v.getParent().getParent();
                    LinearLayout linearChild = (LinearLayout) v.getParent();
                    linearParent.removeView(linearChild);
                }

            }
        });


        ibAddIngridient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(v.getTag().toString()) == 1) {
                    v.setTag(2);
                    addDynamicEditTexts(R.id.llIngridentsCompose, getActivity().getResources().getString(R.string.ingridient_s));
                    v.setBackgroundResource(R.drawable.cancel);
                } else {
                    LinearLayout linearParent = (LinearLayout) v.getParent().getParent();
                    LinearLayout linearChild = (LinearLayout) v.getParent();
                    linearParent.removeView(linearChild);
                }

            }
        });

//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().supportFinishAfterTransition();
//            }
//        });
    }


    private void publish(){

        {
            if (validateFields(llStepListCompose)) {
                return;
            }

            // Save photo images
            ParseFile photo = getParseFile(PHOTO_NAME, true);
            ParseFile photo2 = getParseFile(PHOTO_NAME2, true);
            ParseFile photo3 = getParseFile(PHOTO_NAME3, true);

            //  Save Recipe
            final Recipe recipe = new Recipe();
            recipe.setAuthor(ParseUser.getCurrentUser());
            recipe.setName(getText(etTitle));
            recipe.setDescription(getText(etDescription));
            recipe.setCookingTime(getLong(etPrepTime));
            recipe.setServing(getLong(etServing));
            recipe.setCategories(getList(etCategories));

            //iterarte over the linear layout and get steps added


            ArrayList<String> steps = getDataFromDynamicEditText(llStepListCompose);
            ArrayList<String> ingridients = getDataFromDynamicEditText(R.id.llIngridentsCompose);

            recipe.setSteps(steps);
            recipe.setIngredients(ingridients);

            Log.d("STEPS", steps.toString());
            Log.d("INGRIDIENTS", ingridients.toString());
            Toast.makeText(getContext(), steps.toString(), Toast.LENGTH_SHORT);

            if (photo != null)
                recipe.setPhoto(photo);

            if (photo2 != null)
                recipe.setPhoto2(photo);

            if (photo3 != null)
                recipe.setPhoto3(photo);

            // challengeTo
            if (challengeTo != null)
                recipe.setChallengeTo(Recipe.createWithoutData(Recipe.class, challengeTo));

            recipe.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i(TAG, "Succeeded to save the Recipe");
                        if (challengeId != null) {
                            ChallengeService.getChallenge(challengeId, new GetCallback<Challenge>() {
                                @Override
                                public void done(Challenge challenge, ParseException e) {
                                    if (challenge != null) {
                                        challenge.setState(Challenge.STATE_COMPLETED);
                                        challenge.setMyRecipe(recipe);
                                        challenge.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null)
                                                    Log.i(TAG, "successfully update challenge");
                                                else
                                                    Log.e(TAG, "Failed to update challenge.");
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {
                        Log.e(TAG, "Failed to save the Recipe:" + e, e);
                    }
                }
            });

            if (getActivity() instanceof ActivityResult) {
                ((ActivityResult) getActivity()).submit();
            } else {
                getActivity().supportFinishAfterTransition();
            }


        }
    }



    public TextInputEditText addDynamicEditTexts(int linearLayoutResourceId, String hint) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(linearLayoutResourceId);


        final float scale = getContext().getResources().getDisplayMetrics().density;

        LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_dyanmic_edittext, null);
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        int marginTop = (int) (10 * scale + 0.5f);
        llparams.setMargins(0, marginTop, 0, 0);
        ll.setLayoutParams(llparams);
        linearLayout.addView(ll);

        TextInputEditText te = (TextInputEditText) ll.findViewById(R.id.dynamic_text_input_edittext);
        te.setHint(hint);
        ImageButton dyn_ib = (ImageButton) ll.findViewById(R.id.ib_dyn);
        dyn_ib.setTag(1);
        dyn_ib.setOnClickListener(new DynamicEditText(linearLayoutResourceId, hint) {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(v.getTag().toString()) == 1) {
                    addDynamicEditTexts(this.resourceId, this.hint);
                    v.setBackgroundResource(R.drawable.cancel);
                    v.setTag(2);
                } else {
                    LinearLayout linearParent = (LinearLayout) v.getParent().getParent();
                    LinearLayout linearChild = (LinearLayout) v.getParent();
                    linearParent.removeView(linearChild);
                }

            }
        });

        return te;
    }

    public ArrayList<String> getDataFromDynamicEditText(int llResourceId) {
        ArrayList<String> list = new ArrayList<>();
        LinearLayout ll = (LinearLayout) view.findViewById(llResourceId);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View llView = ll.getChildAt(i);
            if (llView instanceof LinearLayout) {
                TextInputLayout til = (TextInputLayout) ((LinearLayout) llView).getChildAt(0);
                EditText et = til.getEditText();
                if (et != null && !et.getText().toString().isEmpty())
                    list.add(et.getText().toString());
            }
        }
        return list;
    }

    public boolean validateFields(int linearLayoutResourceId) {
        // edit text

        boolean isValid = false;
        if (etTitle.getText().toString().isEmpty()) {
            etTitle.setError("Title is required");
            isValid = true;
        }

        if (etCategories.getText().toString().isEmpty()) {
            etCategories.setError("Category is required");
            isValid = true;
        }

        if (etDescription.getText().toString().isEmpty()) {
            etDescription.setError("Description is required");
            isValid = true;
        }

        return isValid;

    }

    private ParseFile getParseFile(String name, boolean withSave) {
        ParseFile photo = null;
        File photoFile = getPhotoFile(name);
        if (photoFile.exists()) {
            photo = new ParseFile(photoFile);
            if (withSave)
                photo.saveInBackground();
        }
        return photo;
    }

    private List<String> getList(EditText et) {
        return Arrays.asList(getText(et).split("\\s*,\\s*"));
    }

    private long getLong(EditText et) {
        try {
            return Long.valueOf(getText(et));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String getText(EditText et) {
        return et.getText().toString().trim();
    }

    private void displayImage(Bitmap photo, ImageButton ibUploadPhoto, ImageView ivUploadPhoto) {
        ibUploadPhoto.setVisibility(View.INVISIBLE);
        ivUploadPhoto.setVisibility(View.VISIBLE);
        ivUploadPhoto.setImageBitmap(photo);
    }

    // ----------------------------
    // Camera
    // ----------------------------

    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_MAX = 1099;


    private void onLaunchCamera(int index, String name) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(name)); // set the image file name
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            ComposeFragment.this.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE + index);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // main photo
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Bitmap takenImage = BitmapFactory.decodeFile(getPhotoFile(PHOTO_NAME).getPath());
                displayImage(takenImage, ibUploadPhoto, ivUploadPhoto);
            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE + 1) {
                Bitmap takenImage = BitmapFactory.decodeFile(getPhotoFile(PHOTO_NAME).getPath());
                displayImage(takenImage, ibUploadPhoto2, ivUploadPhoto2);
            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE + 2) {
                Bitmap takenImage = BitmapFactory.decodeFile(getPhotoFile(PHOTO_NAME).getPath());
                displayImage(takenImage, ibUploadPhoto3, ivUploadPhoto3);
            }
            // step photo
            else if (requestCode > CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && requestCode <= CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_MAX) {
                int index = requestCode - CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
                Bitmap takenImage = BitmapFactory.decodeFile(getPhotoFile(getStepPhotoName(index)).getPath());
            }
        }
    }

    private Uri getPhotoFileUri(String name) {
        return FileProvider.getUriForFile(getContext(),
                "com.einstens3.ironchef.fileprovider",
                getPhotoFile(name));
    }

    private File getPhotoFile(String name) {
        File filesDir = getContext().getFilesDir();
        return new File(filesDir, getPhotoFilename(name));
    }

    private String getPhotoFilename(String name) {
        return "IMG_" + name + ".jpg";
    }

    private static String getStepPhotoName(int index) {
        return String.format(Locale.ENGLISH, STEP_PHOTO_NAME, index);
    }

    public class DynamicEditText implements View.OnClickListener {

        String hint;
        int resourceId;

        public DynamicEditText(int resourceId, String hint) {
            this.hint = hint;
            this.resourceId = resourceId;
        }

        @Override
        public void onClick(View v) {
            //read your lovely variable
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menuPublish:
               publish();
        }
        return false;
    }



}
