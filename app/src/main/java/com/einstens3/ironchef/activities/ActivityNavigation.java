package com.einstens3.ironchef.activities;

import android.view.View;

public interface ActivityNavigation {
    void showComposeUI(View view);

    void showComposeUIForChallenge(String recipeId, String challengeId);
}
