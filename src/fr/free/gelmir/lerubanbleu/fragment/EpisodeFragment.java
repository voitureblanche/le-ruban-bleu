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
import android.widget.ImageView;
import android.widget.TextView;
import fr.free.gelmir.lerubanbleu.R;
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

    private static final String EPISODE_NUMBER = "fr.free.gelmir.fragment.episodeFragment.episodeNumber";

    LibraryServiceHelper mLibraryServiceHelper;

    public EpisodeFragment() {
    }

    public static final EpisodeFragment newInstance(int episodeNb)
    {
        Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " instantiated");
        EpisodeFragment fragment = new EpisodeFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(EPISODE_NUMBER, episodeNb);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
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
        //mLibraryServiceHelper.getEpisode(mContext, episodeNb);

        // Inflate view
        View view = inflater.inflate(R.layout.fr_episode, container, false);
        TextView textView = (TextView) view.findViewById(R.id.episodeNb);
        textView.setText(Integer.toString(episodeNb));
        ImageView imageView = (ImageView) view.findViewById(R.id.watch);
        imageView.setImageResource(R.drawable.watch);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);
        Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " paused");

        // Unregister the receiver
        getActivity().unregisterReceiver(mLibraryServiceHelperReceiver);
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);
        Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " resumed");

        // Relaunch episode get?

        // Register to Library service intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryServiceHelper.GET_EPISODE_COMPLETE);
        getActivity().registerReceiver(mLibraryServiceHelperReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);
        Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " destroyed");
    }


    private BroadcastReceiver mLibraryServiceHelperReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
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

                        //
                        TextView textView = (TextView) getView().findViewById(R.id.episodeNb);
                        textView.setText("OK!");

                        break;

                    case LibraryServiceHelper.STATUS_KO:
                        Log.d("EpisodeFragment", "STATUS_KO");

                        break;
                }
            }

        }
    };

}
