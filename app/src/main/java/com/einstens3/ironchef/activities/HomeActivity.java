package com.einstens3.ironchef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.einstens3.ironchef.R;
import com.einstens3.ironchef.fragments.ActivityNavigation;
import com.einstens3.ironchef.fragments.HomeFragment;
import com.einstens3.ironchef.fragments.MyListFragment;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements ActivityNavigation {

    private ParseUser currentUser;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;

    public class HomePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "My List"};

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeFragment();
            } else if (position == 1) {
                return new MyListFragment();
            } else {
                return null;
            }

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        pagerSlidingTabStrip.setViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null)
                    showComposeUI();
                else
                    Toast.makeText(HomeActivity.this, "To post the recipe, please login!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = ParseUser.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void showComposeUI() {
        Intent intent = new Intent(HomeActivity.this, ComposeActivity.class);
        startActivity(intent);
    }

    public void showComposeUIForChallenge(String challengeTo, String challengeId) {
        Intent intent = new Intent(HomeActivity.this, ComposeActivity.class);
        intent.putExtra("challengeTo", challengeTo); // Recipe.objectId
        intent.putExtra("challengeId", challengeId); // Challenge.objectId
        startActivity(intent);
    }
}
