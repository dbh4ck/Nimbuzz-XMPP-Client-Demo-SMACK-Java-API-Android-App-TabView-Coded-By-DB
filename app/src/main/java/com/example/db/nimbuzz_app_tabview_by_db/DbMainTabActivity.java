package com.example.db.nimbuzz_app_tabview_by_db;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DbMainTabActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private dbDialog InfoDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_main_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Rooms"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Dialog for getting the xmpp settings
        InfoDialog = new dbDialog(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Coded By Db~@NC", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                //Toast.makeText(DbMainTabActivity.this,"Coded By Db~@NC", Toast.LENGTH_LONG).show();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    InfoDialog.show();
                                }
                            });

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        inst = this;

    }

    private static DbMainTabActivity inst;
    public static Activity instance() {
        return inst;
    }




    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {

            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ProfileFragment tab1 = new ProfileFragment();
                    return tab1;
                case 1:
                    ContactsFragment tab2 = new ContactsFragment();
                    return tab2;
                case 2:
                    RealFragment tab3 = new RealFragment();
                    return tab3;
                case 3:
                    conferenceFragment tab4 = new conferenceFragment();
                    return tab4;
                case 4:
                    AboutDbFragment tab5 = new AboutDbFragment();
                    return tab5;
                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


}
