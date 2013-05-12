package fr.free.gelmir.lerubanbleu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.fragment.EpisodeFragment;

import java.util.HashMap;

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
        setContentView(R.layout.ac_viewer);

        // Get last current page and display it
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        int lastEpisode = application.getUserLatestEpisode();
        int totalNbEpisodes = application.getTotalNbEpisodes();
        Log.d("ViewerActivity", "latest episode " + Integer.toString(lastEpisode));
        Log.d("ViewerActivity", "total number of episodes " + Integer.toString(totalNbEpisodes));

        // FragmentPagerAdapter
        EpisodeFragmentPagerAdapter fragmentPagerAdapter = new EpisodeFragmentPagerAdapter(getSupportFragmentManager(), lastEpisode, totalNbEpisodes);

        // Viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(fragmentPagerAdapter);
        Log.d("ViewerActivity", "setCurrentItem " + Integer.toString(lastEpisode));
        mViewPager.setCurrentItem(lastEpisode);
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

        // Save episode
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        application.setUserLatestEpisode(mViewPager.getCurrentItem());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Custom fragment pager adapter to populate fragments inside of a ViewPager
     */
    private class EpisodeFragmentPagerAdapter extends FragmentPagerAdapter
    {
        private HashMap<Integer, EpisodeFragment> mHashMap = new HashMap<Integer, EpisodeFragment>();
        private int mTotalNbEpisodes;

        public EpisodeFragmentPagerAdapter(FragmentManager fragmentManager, int initialEpisode, int totalNbEpisodes) {
            super(fragmentManager);
            mTotalNbEpisodes = totalNbEpisodes;
            allocateFragments(initialEpisode);
        }

        @Override
        public EpisodeFragment getItem(int i) {
            Log.d("EpisodeFragmentPAgerAdapter", "getItem " + Integer.toString(i));
            allocateFragments(i);
            return mHashMap.get(i);
        }

        @Override
        public int getCount() {
            return mTotalNbEpisodes;
        }

        // Allocate fragments
        private void allocateFragments(int episodeNb)
        {
            // Allocate episode number fragment
            if (mHashMap.get(episodeNb) == null) {
                mHashMap.put(episodeNb, EpisodeFragment.newInstance(episodeNb));
            }

            // Allocate preceding fragments
            for (int i=episodeNb-3; i<episodeNb; i++) {
                if (i >= 0) {
                    if (mHashMap.get(i) == null) {
                        mHashMap.put(i, EpisodeFragment.newInstance(i));
                    }
                }
            }

            // Allocate following fragments
            for (int i=episodeNb+1; i<episodeNb+4; i++) {
                if (i <= mTotalNbEpisodes) {
                    if (mHashMap.get(i) == null) {
                        mHashMap.put(i, EpisodeFragment.newInstance(i));
                    }
                }
            }
        }
    }

}
