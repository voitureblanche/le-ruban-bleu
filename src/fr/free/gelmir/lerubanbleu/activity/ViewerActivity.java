package fr.free.gelmir.lerubanbleu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import fr.free.gelmir.lerubanbleu.R;

import java.util.ArrayList;

public class ViewerActivity extends FragmentActivity
{
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        // FragmentPagerAdapter
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        // Restore latest episode or start with first one
        // EpisodeFragment episodeFragment1 = EpisodeFragment.newInstance(1, this);
        // fragmentPagerAdapter.addFragment(episodeFragment1);

        // Viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(0);

        // FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Restore user preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get last current page and display it

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Setup an editor on user preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put current page
        editor.putLong("currentPage", 10);

        // Commit the edits!
        editor.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Custom fragment pager adapter to populate fragments inside of a ViewPager
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter
    {
        private final ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
            notifyDataSetChanged();
        }
    }


}
