package fr.free.gelmir.lerubanbleu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.viewpagerindicator.UnderlinePageIndicator;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.fragment.EpisodeFragmentPagerAdapter;
import fr.free.gelmir.lerubanbleu.util.CustomViewPager;

public class ViewerActivity extends FragmentActivity
{
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
        EpisodeFragmentPagerAdapter episodeFragmentPagerAdapter = new EpisodeFragmentPagerAdapter(getSupportFragmentManager(), lastEpisode, totalNbEpisodes);

        // OnPageChangeListener
        MyOnPageChangeListener pageChangeListener = new MyOnPageChangeListener();

        // Viewpager
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(episodeFragmentPagerAdapter);

        // Bind the indicator to the viewpager
        // Bind early to avoid indicator blinking at position 0
        UnderlinePageIndicator indicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setFadeDelay(1000);
        indicator.setFadeLength(1000);
        indicator.setOnPageChangeListener(pageChangeListener);

        // Set page
        viewPager.setCurrentItem(lastEpisode);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            //Log.d("ViewerActivity", "MyOnPageChangeListener onPageSelected " + Integer.toString(i));

            // Save episode
            LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
            application.setUserLatestEpisode(i);

            // TODO: forward event to the CustomViewPager

        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

}
