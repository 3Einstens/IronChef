package com.einstens3.ironchef.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.einstens3.ironchef.R;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class ComposeFragment extends Fragment {

    private static final String TAG = ComposeFragment.class.getSimpleName();

    private static final String ARG_PARAM_CHALLENGE_TO = "challengeTo"; // Recipe.objectId
    private static final String ARG_PARAM_CHALLENGE_ID = "challengeId"; // Challenge.objectId

    private static final String PHOTO_NAME = "photo"; // main photo
    private static final String STEP_PHOTO_NAME = "step_%d"; // Step photo

    // information for challenge. If normal compose, following two parameters will be `null`.
    String challengeTo;
    String challengeId;

    View view;
    Button btnPublish;
    Button btnSave;
    Button btnCancel;
    ImageView ivUploadPhoto;
    ImageButton ibUploadPhoto;
    EditText etTitle;
    EditText etDescription;
    EditText etCategories;
    EditText etIngredients;
    EditText etPrepTime;
    EditText etServing;
    ImageButton ibAddSteps;
    ImageButton ibAddIngridient;
    TextView tvChallengeStatus;

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
            Log.e(TAG, "challengeId -> " + challengeId +", challengeTo -> "+challengeTo);
        }
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

        btnPublish = (Button) view.findViewById(R.id.btnPublish);
        //btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        ivUploadPhoto = (ImageView) view.findViewById(R.id.ivUploadPhoto);
        ibUploadPhoto = (ImageButton) view.findViewById(R.id.ibUploadPhoto);

        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etCategories = (EditText) view.findViewById(R.id.etCategories);
        etPrepTime = (EditText) view.findViewById(R.id.etPrepTime);
        etServing = (EditText) view.findViewById(R.id.etServing);
        //etStep2 = (EditText) view.findViewById(R.id.etStep2);
        //etStep3 = (EditText) view.findViewById(R.id.etStep3);

        ibAddSteps = (ImageButton) view.findViewById(R.id.ibAddSteps);
        ibAddIngridient = (ImageButton) view.findViewById(R.id.ibAddIngridients);
        //ibStep2 = (ImageButton) view.findViewById(R.id.ibStep2);
        //ibStep3 = (ImageButton) view.findViewById(R.id.ibStep3);

        tvChallengeStatus = (TextView)view.findViewById(R.id.tvChallengeStatus);
    }

    private void updateControlStates() {
        ibUploadPhoto.setVisibility(View.VISIBLE);
        ivUploadPhoto.setVisibility(View.INVISIBLE);

        // update if challenge
        if(challengeId != null){
            ChallengeService.getChallenge(challengeId, new GetCallback<Challenge>() {
                @Override
                public void done(Challenge challenge, ParseException e) {
                    tvChallengeStatus.setText("Challenge Recipe");
                }
            });
        }else{
            tvChallengeStatus.setVisibility(View.GONE);
        }
        if(challengeTo != null){
            RecipeService.queryRecipe(challengeTo, new RecipeService.QueryRecipeCallback(){
                @Override
                public void success(Recipe recipe) {
                    etTitle.setText(recipe.getName());
                    etCategories.setText(StringUtils.fromList(recipe.getCategories()));
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
        ibAddSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo Dynamically add new editText
                addDynamicEditTexts(R.id.llStepListCompose,"Add Step");
            }
        });

        ibAddIngridient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDynamicEditTexts(R.id.llIngridentsCompose,"Add Ingridient");
            }
        });




        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: Save as Draft
//                Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_LONG).show();
//            }
//        });
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Required field value check

                // Save photo images
                ParseFile photo = getParseFile(PHOTO_NAME, true);
//                ParseFile step1Photo = getParseFile(getStepPhotoName(1), true);
//                ParseFile step2Photo = getParseFile(getStepPhotoName(2), true);
//                ParseFile step3Photo = getParseFile(getStepPhotoName(3), true);

                //  Save Recipe
                final Recipe recipe = new Recipe();
                recipe.setDraft(false);
                recipe.setPublic(false);
                recipe.setAuthor(ParseUser.getCurrentUser());
                recipe.setName(getText(etTitle));
                recipe.setDescription(getText(etDescription));
                recipe.setCookingTime(getLong(etPrepTime));
                recipe.setServing(getLong(etServing));
                recipe.setCategories(getList(etCategories));


                //iterarte over the linear layout and get steps added


                ArrayList<String> steps = getDataFromDynamicEditText(R.id.llStepListCompose);
                ArrayList<String> ingridients = getDataFromDynamicEditText(R.id.llIngridentsCompose);

                recipe.setSteps(steps);
                recipe.setIngredients(ingridients);

                Log.d("STEPS",steps.toString());
                Log.d("INGRIDIENTS",ingridients.toString());
                Toast.makeText(getContext(),steps.toString(),Toast.LENGTH_SHORT);

                if(photo!=null)
                    recipe.setPhoto(photo);

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
                                        if(challenge!=null){
                                            challenge.setState(Challenge.STATE_COMPLETED);
                                            challenge.setMyRecipe(recipe);
                                            challenge.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e==null)
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

                getActivity().finish();
            }
        });
    }


    public void addDynamicEditTexts(int linearLayoutResourceId,String hint){
        LinearLayout linearLayout = (LinearLayout) view.findViewById(linearLayoutResourceId);


        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        llparams.setMargins(0,5,0,0);
        ll.setLayoutParams(llparams);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton removeButton = new ImageButton(getContext());
        LinearLayout.LayoutParams rbparams = new LinearLayout.LayoutParams(100, 100);

        rbparams.gravity = Gravity.CENTER;
        removeButton.setLayoutParams(rbparams);

        removeButton.setBackgroundResource(R.drawable.cancel);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearParent =  (LinearLayout) v.getParent().getParent();
                LinearLayout linearChild = (LinearLayout) v.getParent();
                linearParent.removeView(linearChild);
            }
        });
        EditText editTextView = new EditText(getContext());

        editTextView.setHint(hint);
        editTextView.setBackgroundResource(R.drawable.compose_edittext);
        editTextView.setPadding(10,0,0,0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        editTextView.setLayoutParams(params);
        ll.addView(editTextView);
        ll.addView(removeButton);
        linearLayout.addView(ll);
    }

    public ArrayList<String> getDataFromDynamicEditText(int llResourceId){

        ArrayList<String> steps = new ArrayList<>();
        LinearLayout ll = (LinearLayout) view.findViewById(llResourceId);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View llView = ll.getChildAt(i);
            if(llView instanceof LinearLayout){
                EditText et = (EditText) ((LinearLayout) llView).getChildAt(0);
                if(et != null && !et.getText().toString().isEmpty())
                    steps.add(et.getText().toString());
            }
        }

        return steps;
    }

    private ParseFile getParseFile(String name, boolean withSave){
        ParseFile photo = null;
        File photoFile = getPhotoFile(name);
        if(photoFile.exists()) {
            photo = new ParseFile(photoFile);
            if(withSave)
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
        }catch (NumberFormatException e){
            return 0;
        }
    }

    private String getText(EditText et) {
        return et.getText().toString().trim();
    }

    private void displayImage(Bitmap photo) {
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
                displayImage(takenImage);
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
}
