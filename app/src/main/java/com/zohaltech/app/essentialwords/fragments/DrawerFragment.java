package com.zohaltech.app.essentialwords.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zohaltech.app.essentialwords.activities.AboutActivity;
import com.zohaltech.app.essentialwords.activities.BookmarksActivity;
import com.zohaltech.app.essentialwords.activities.IntroductionActivity;
import com.zohaltech.app.essentialwords.activities.SchedulerActivity;
import com.zohaltech.app.essentialwords.classes.App;

import com.zohaltech.app.essentialwords.R;


public class DrawerFragment extends Fragment {

    NavigationView navView;
    DrawerLayout   drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navView = (NavigationView) view.findViewById(R.id.navView);

        //updateUi();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.nav_scheduler:
                        intent = new Intent(getActivity(), SchedulerActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_bookmarks:
                        intent = new Intent(getActivity(), BookmarksActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_help:
                        intent = new Intent(getActivity(), IntroductionActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_about:
                        intent = new Intent(getActivity(), AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                App.handler.postDelayed(new Runnable() {
                    public void run() {
                        drawerLayout.closeDrawers();
                    }
                }, 500);
                return false;
            }
        });
    }

    public void setUp(DrawerLayout drawerLayout, final Toolbar toolbar) {
        this.drawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
}