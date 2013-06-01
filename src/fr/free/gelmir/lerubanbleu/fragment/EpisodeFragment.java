package fr.free.gelmir.lerubanbleu.fragment;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.provider.EpisodeProvider;
import fr.free.gelmir.lerubanbleu.provider.EpisodeTable;
import fr.free.gelmir.lerubanbleu.service.Episode;
import fr.free.gelmir.lerubanbleu.service.LibraryServiceHelper;
import fr.free.gelmir.lerubanbleu.util.CustomImageView;
import fr.free.gelmir.lerubanbleu.util.CustomViewPager;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 30/04/13
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeFragment extends Fragment {

    // Static
    private static final String EPISODE_NUMBER = "fr.free.gelmir.fragment.episodeFragment.episodeNumber";
    private static final String IMAGE_URI_STRING = "fr.free.gelmir.fragment.episodeFragment.imageUriString";
    private static final String REGISTERED_RECEIVER = "fr.free.gelmir.fragment.episodeFragment.registeredReceiver";


    public static final EpisodeFragment newInstance(int episodeNb)
    {
        Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " instantiated");
        EpisodeFragment fragment = new EpisodeFragment();

        // Bundle
        Bundle bundle = new Bundle(2);
        bundle.putInt(EPISODE_NUMBER, episodeNb);
        bundle.putString(IMAGE_URI_STRING, "");
        bundle.putBoolean(REGISTERED_RECEIVER, false);
        fragment.setArguments(bundle);

        return fragment;
    }

    // Constructor
    public EpisodeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);

        // Inflate view
        View view = inflater.inflate(R.layout.fr_episode, container, false);
        TextView textView = (TextView) view.findViewById(R.id.episodeNbTextView);
        textView.setText(Integer.toString(episodeNb + 1));

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);
        boolean registeredReceiver = bundle.getBoolean(REGISTERED_RECEIVER);
        //Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " paused");

        // Unregister the receiver
        if (registeredReceiver) {
            getActivity().unregisterReceiver(mLibraryServiceHelperReceiver);
            bundle.putBoolean(REGISTERED_RECEIVER, false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // Get arguments
        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);
        String bundleImageUriString = bundle.getString(IMAGE_URI_STRING);
        //Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " resumed");

        // Test the image URI
        if (bundleImageUriString.equals("")) {

            // Query the episode
            Cursor cursor;
            ContentResolver contentResolver = getActivity().getContentResolver();
            Uri episodeUri = Uri.withAppendedPath(EpisodeProvider.CONTENT_URI, Integer.toString(episodeNb));
            cursor = contentResolver.query(episodeUri, null, null, null, null);

            // An episode has been found
            if (cursor.moveToFirst()) {

                // Read status
                int status = cursor.getInt(cursor.getColumnIndex(EpisodeTable.COLUMN_STATUS));
                switch (status)
                {
                    // The episode has already been downloaded successfully
                    case EpisodeTable.STATUS_SUCCESSFUL:
                        //Log.d("EpisodeFragment", "episode successfully found in the database!");

                        String imageUriString = cursor.getString(cursor.getColumnIndex(EpisodeTable.COLUMN_IMAGE_URI));

                        // Save image URI in the arguments bundle
                        bundle.putString(IMAGE_URI_STRING, imageUriString);

                        // Display the episode
                        Uri imageUri = Uri.parse(imageUriString);
                        displayEpisode(imageUri);
                        break;

                    // The episode could not be downloaded
                    case EpisodeTable.STATUS_FAILED:
                        //Log.d("EpisodeFragment", "episode with FAILED status found in the database!");
                        getEpisode(episodeNb);
                        break;

                    // The episode is currently being downloaded
                    case EpisodeTable.STATUS_RUNNING:
                        //Log.d("EpisodeFragment", "episode with RUNNING status found in the database!");
                        // TODO: listen to notification

                        break;

                    // Should never happen
                    default:
                        // Returned parameters
                        break;
                }
            }

            // No episode found: get the episode
            else {
                getEpisode(episodeNb);
            }

            cursor.close();
        }

        // Display the episode
        else {
            Uri imageUri = Uri.parse(bundleImageUriString);
            displayEpisode(imageUri);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Bundle bundle = this.getArguments();
        int episodeNb = bundle.getInt(EPISODE_NUMBER);
        Log.d("EpisodeFragment", "fragment " + Integer.toString(episodeNb) + " destroyed");
    }


    private void getEpisode(int episodeNb)
    {
        // Register to Library service intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryServiceHelper.GET_EPISODE_COMPLETE);
        intentFilter.addDataScheme("lerubanbleu");
        intentFilter.addDataAuthority("fr.free.gelmir.lerubanbleu", null);
        intentFilter.addDataPath("/" + Integer.toString(episodeNb), 0);
        Bundle bundle = this.getArguments();
        bundle.putBoolean(REGISTERED_RECEIVER, true);
        getActivity().registerReceiver(mLibraryServiceHelperReceiver, intentFilter);

        // Get episode
        LibraryServiceHelper libraryServiceHelper = LibraryServiceHelper.getInstance(getActivity());
        libraryServiceHelper.getEpisode(getActivity(), episodeNb);
    }

    private void displayEpisode(Uri imageUri)
    {
        //Log.d("displayEpisode", "displaying episode " + imageUri.toString());

        // Hide progress bar
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.episodeProgressBar);
        progressBar.setVisibility(View.GONE);

        // Configure ImageView
        // Consider using setImageBitmap() instead?
        CustomImageView episodeView = (CustomImageView) getView().findViewById(R.id.episodeCustomImageView);
        episodeView.setImageURI(imageUri);

        // Configure zoom
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        if (application.getZoomLevel() == 0) {
            //Log.d("displayEpisode", "zoom level 0");
            episodeView.initZoom(CustomImageView.ZOOM_LEVEL_0, 0);
        }
        else {
            //Log.d("displayEpisode", "zoom level 1");
            CustomViewPager viewPager = (CustomViewPager) getActivity().findViewById(R.id.viewpager);
            Bundle bundle = this.getArguments();
            int episodeNb = bundle.getInt(EPISODE_NUMBER);
            int currentItem =  viewPager.getCurrentItem();
            int alignment = episodeNb < currentItem ? CustomImageView.ALIGN_RIGHT : CustomImageView.ALIGN_LEFT;
            episodeView.initZoom(CustomImageView.ZOOM_LEVEL_1, alignment);
        }

        // Show ImageView
        episodeView.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver mLibraryServiceHelperReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(LibraryServiceHelper.GET_EPISODE_COMPLETE))
            {
                //Log.d("EpisodeFragment", "GET_EPISODE_COMPLETE intent received");
                int status = intent.getIntExtra(LibraryServiceHelper.EXTRA_STATUS, -1);

                // Parse status
                switch (status)
                {
                    case LibraryServiceHelper.STATUS_OK:
                        //Log.d("EpisodeFragment", "STATUS_OK");
                        Episode episode = intent.getExtras().getParcelable(LibraryServiceHelper.EXTRA_EPISODE_POJO);
                        Uri episodeImageUri = episode.getImageUri();
                        displayEpisode(episodeImageUri);

                        break;

                    case LibraryServiceHelper.STATUS_KO:
                        //Log.d("EpisodeFragment", "STATUS_KO");
                        // TODO: toast message + update view with error icon
                        break;
                }
            }
        }
    };

}
