package fr.free.gelmir.lerubanbleu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.free.gelmir.lerubanbleu.service.Episode;
import fr.free.gelmir.lerubanbleu.service.LibraryServiceHelper;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 30/04/13
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeFragment extends Fragment {

    private static final String EPISODE_NUMBER = "fr.free.gelmir.expandingviewpager.episodeNumber";


    Context mContext;
    LibraryServiceHelper mLibraryServiceHelper;


    public EpisodeFragment() {

    }

    public static final EpisodeFragment newInstance(int episodeNb)
    {
        Log.d("EpisodeFragment", "new fragment " + Integer.toString(episodeNb) + " instantiated");
        EpisodeFragment fragment = new EpisodeFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(EPISODE_NUMBER, episodeNb);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);

        // LibraryService helper
        mLibraryServiceHelper = LibraryServiceHelper.getInstance(getActivity());

        // Register to Library service intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryServiceHelper.GET_EPISODE_COMPLETE);
        getActivity().registerReceiver(mLibraryServiceHelperReceiver, intentFilter);

        // Get episode
        mLibraryServiceHelper.getEpisode(mContext, episodeNb);

        return null;
    }


    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

        // Unregister the receiver?
    }


    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        // Relaunch episode get

    }


    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.

        // Unregister the receiver
        mContext.unregisterReceiver(mLibraryServiceHelperReceiver);

    }


    private BroadcastReceiver mLibraryServiceHelperReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            // Get total number of episodes
            //-----------------------------
            if (intent.getAction().equals(LibraryServiceHelper.GET_EPISODE_COMPLETE))
            {
                Log.d("EpisodeFragment", "GET_EPISODE_COMPLETE intent received");
                int status = intent.getIntExtra(LibraryServiceHelper.EXTRA_STATUS, -1);

                // Parse status
                switch (status)
                {
                    case LibraryServiceHelper.STATUS_OK:
                        Log.d("EpisodeFragment", "STATUS_OK");
                        Episode episode = intent.getExtras().getParcelable(LibraryServiceHelper.EXTRA_EPISODE_POJO);
                        Uri episodeImageUri = episode.getImageUri();

                        // TODO: display image


                        break;

                    case LibraryServiceHelper.STATUS_KO:
                        Log.d("EpisodeFragment", "STATUS_KO");

                        break;
                }
            }

        }
    };

}
