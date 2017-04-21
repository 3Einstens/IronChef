package com.einstens3.ironchef.activities;

import com.parse.ui.ParseLoginDispatchActivity;

public class DispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return SplashActivity.class;
    }
}
