package com.einstens3.ironchef.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.fragments.ComposeFragment;

public class ComposeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String challengeTo = getIntent().getStringExtra("challengeTo");
        String challengeId = getIntent().getStringExtra("challengeId");
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null)
                return;
            ComposeFragment fragment = ComposeFragment.newInstance(challengeTo, challengeId);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }
}
