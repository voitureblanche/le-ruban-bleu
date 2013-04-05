package fr.free.gelmir.lerubanbleu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import fr.free.gelmir.lerubanbleu.service.Episode;
import fr.free.gelmir.lerubanbleu.service.LibraryServiceHelper;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 12/03/13
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class DummyActivity extends Activity {
    Button mButton;
    ImageView mImageView;
    EditText mEditText;
    LibraryServiceHelper mLibraryServiceHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // Get widgets
        mButton = (Button) findViewById(R.id.button);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mEditText = (EditText) findViewById(R.id.episodeNumber);

        // Listen to buttons
        mButton.setOnClickListener(onClickListener);

        // LibraryService helper
        mLibraryServiceHelper = LibraryServiceHelper.getInstance(this);

        // Register to Library service intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryServiceHelper.GET_EPISODE_COMPLETE);
        registerReceiver(mLibraryServiceHelperReceiver, intentFilter);

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        public void onClick(View view)
        {
            Log.d("DummyActivity", "Button 1 clicked");
            mLibraryServiceHelper.getEpisode(view.getContext(), Integer.parseInt(mEditText.getText().toString()));
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister receiver?
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ask if the request is still pending with the request id


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver
        unregisterReceiver(mLibraryServiceHelperReceiver);
    }


    private BroadcastReceiver mLibraryServiceHelperReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            // Get article
            //------------
            if (intent.getAction().equals(LibraryServiceHelper.GET_EPISODE_COMPLETE))
            {
                Log.d("DummyActivity", "ACTION_GET_ARTICLE_COMPLETE intent received");
                int status = intent.getIntExtra(LibraryServiceHelper.EXTRA_STATUS, 0);

                // Parse status
                switch (status)
                {
                    case LibraryServiceHelper.STATUS_SUCCESSFUL:
                        Log.d("DummyActivity", "STATUS_SUCCESSFUL");
                        Episode episode = intent.getParcelableExtra(LibraryServiceHelper.EXTRA_EPISODE);
                        Uri episodeImageUri = episode.getImageUri();

                        // Display image
                        mImageView.setImageURI(episodeImageUri);
                        break;

                    case LibraryServiceHelper.STATUS_FAILED:
                        Log.d("DummyActivity", "STATUS_FAILED");
                        /* switch ()
                        {
                            case 1:
                                mTextView1.setText("failed :-(");
                                break;

                            case 2:
                                mTextView2.setText("failed :-(");
                                break;
                        } */
                        break;
                }
            }

            // Get latest articles
            //--------------------
            else if (intent.getAction().equals(LibraryServiceHelper.GET_LATEST_ARTICLES_COMPLETE))
            {


            }
        }

    };

}