package com.juane.remotecontrol;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.juane.remotecontrol.ui.components.pager.BottomBarAdapter;
import com.juane.remotecontrol.ui.components.pager.NoSwipePager;
import com.juane.remotecontrol.ui.fragments.MainFragment;
import com.juane.remotecontrol.ui.fragments.PowerFragment;
import com.juane.remotecontrol.ui.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NoSwipePager viewPager;

    private BottomNavigationView navigation;
    private MainFragment mainFragment = new MainFragment();
    private PowerFragment powerFragment = new PowerFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_new);

        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewPager);

        //optimisation
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(false);

        BottomBarAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        pagerAdapter.addFragments(mainFragment); // POS 0
        pagerAdapter.addFragments(powerFragment); // POS 1
        pagerAdapter.addFragments(settingsFragment); // POS 2

        viewPager.setAdapter(pagerAdapter);

        //Handling the tab clicks
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.controlsTabId:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.powerTabId:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.settingsTabId:
                        viewPager.setCurrentItem(2);
                        return true;

                }
                return false;
            }
        });
    }

    public MainFragment getMainFragment(){
        return mainFragment;
    }

    public PowerFragment getPowerFragment(){
        return powerFragment;
    }


    public void changeTab(int tabId) {
        navigation.setSelectedItemId(tabId);
    }
}

