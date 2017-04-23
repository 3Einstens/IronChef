package com.einstens3.ironchef.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.einstens3.ironchef.R;
import com.einstens3.ironchef.fragments.FragmentRefresh;
import com.einstens3.ironchef.fragments.HomeFragment;
import com.einstens3.ironchef.fragments.MyListFragment;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity implements ActivityNavigation {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private ParseUser currentUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @BindView(R.id.nvView)
    NavigationView nvView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;


    public class HomePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "My List"};

        private HomeFragment homeFragment;
        private MyListFragment myListFragment;

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if(homeFragment==null)
                    homeFragment =  new HomeFragment();
                return homeFragment;
            } else if (position == 1) {
                if(myListFragment==null)
                    myListFragment =  new MyListFragment();
                return myListFragment;
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

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayUseLogoEnabled(false);

        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        pagerSlidingTabStrip.setViewPager(viewPager);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null)
                    showComposeUI(fab);
                else
                    Toast.makeText(HomeActivity.this, "To post the recipe, please login!", Toast.LENGTH_LONG).show();
            }
        });
        // Find our drawer view
        mDrawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(mDrawerToggle);
        setupDrawerContent(nvView);
        View header = nvView.getHeaderView(0);
        TextView tvName = (TextView) header.findViewById(R.id.userName);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            tvName.setText(ParseUser.getCurrentUser().getUsername());
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch (menuItem.getItemId()) {
            case R.id.profile:
                //TODO
                Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show(); ;
                break;
            case R.id.logOut:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(HomeActivity.this, DispatchActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            default:
                break;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final int REQUEST_CODE_COMPOSE = 1000;

    public void showComposeUI(View view) {
        Intent intent = new Intent(HomeActivity.this, ComposeActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "compose");
        startActivityForResult(intent, REQUEST_CODE_COMPOSE, options.toBundle());
    }

    public void showComposeUIForChallenge(String challengeTo, String challengeId) {
        Intent intent = new Intent(HomeActivity.this, ComposeActivity.class);
        intent.putExtra("challengeTo", challengeTo); // Recipe.objectId
        intent.putExtra("challengeId", challengeId); // Challenge.objectId
        startActivityForResult(intent, REQUEST_CODE_COMPOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_COMPOSE) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Successful result fro Compose activity. Refresh Fragments
                    FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
                    for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                        Fragment fragment = fragmentPagerAdapter.getItem(i);
                        if (fragment != null && fragment instanceof FragmentRefresh) {
                            ((FragmentRefresh) fragment).update();
                        }
                    }
                }
            }, 500);
        }
    }
}
