package com.inspect.ocr;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import com.inspect.ocr.common.Common;
import com.inspect.ocr.common.LocalStorageManager;
import com.inspect.ocr.fragments.FragmentLocalList;
import com.inspect.ocr.fragments.FragmentServerList;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.inspect.ocr.common.Common.currentActivity;

public class MedicineBookActivity extends AppCompatActivity {

    // Titles of the individual pages (displayed in tabs)
    private String[] PAGE_TITLES = new String[]{};

    // The fragments that are used as the individual pages

    private ViewPager mViewPager;

    private FragmentServerList serverListFragment;
    private FragmentLocalList localListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_book);
        currentActivity = this;

/*
        LocalStorageManager localStorageManager = new LocalStorageManager();
        localStorageManager.setUserLoginStatus(  ( (Integer)Profile.Companion.getUserId() ).toString() );
*/

        RelativeLayout cameraBtn = findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Common.currentActivity, ActivityPatientInfo.class);
                startActivity(intent);
            }
        });

        PAGE_TITLES = new String[] {
                getResources().getString(R.string.server_img_tab),
                getResources().getString(R.string.local_img_tab)
        };

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        serverListFragment = new FragmentServerList(mViewPager);
        localListFragment = new FragmentLocalList(mViewPager);

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF4081"));
        tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#cacaca"), Color.parseColor("#ffffff"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return serverListFragment;
                case 1:
                    return localListFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }
    }
}
