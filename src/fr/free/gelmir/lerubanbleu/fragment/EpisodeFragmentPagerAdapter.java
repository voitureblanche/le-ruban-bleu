package fr.free.gelmir.lerubanbleu.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.HashMap;


/**
 * Custom fragment pager adapter to populate fragments inside of a ViewPager
 */
public class EpisodeFragmentPagerAdapter extends FragmentPagerAdapter
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
        Log.d("EpisodeFragmentPagerAdapter", "getItem " + Integer.toString(i));
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
    }
}
