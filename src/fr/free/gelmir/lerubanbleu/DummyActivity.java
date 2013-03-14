package fr.free.gelmir.lerubanbleu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fr.free.gelmir.lerubanbleu.service.LibraryService;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 12/03/13
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class DummyActivity extends Activity
{
    Button mButton1, mButton2, mButton3;
    TextView mTextView1, mTextView2, mTextView3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // Start service
        Intent intent = new Intent(this, LibraryService.class);
        startService(intent);

        // Get widgets
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mTextView1 = (TextView) findViewById(R.id.textView1);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView3 = (TextView) findViewById(R.id.textView3);

        // Listen to buttons
        mButton1.setOnClickListener(onClickListener);
        mButton2.setOnClickListener(onClickListener);
        mButton3.setOnClickListener(onClickListener);

        // Register to Library service intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryService.ACTION_GET_ARTICLE_COMPLETE);
        registerReceiver(mLibraryReceiver, intentFilter);

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        public void onClick(View view)
        {
            Intent intent;
            ArrayList<Integer> articleNumbers = new ArrayList<Integer>();

            switch (view.getId())
            {
                case R.id.button1:
                    Log.d("DummyActivity", "Button 1 clicked");
                    intent = new Intent(LibraryService.ACTION_GET_ARTICLE);
                    articleNumbers.add(1);
                    intent.putIntegerArrayListExtra(LibraryService.EXTRA_GET_ARTICLE_NUMBER_LIST, articleNumbers);
                    sendBroadcast(intent);

                    break;

                case R.id.button2:
                    Log.d("DummyActivity", "Button 1 clicked");
                    intent = new Intent(LibraryService.ACTION_GET_ARTICLE);
                    articleNumbers.add(2);
                    intent.putIntegerArrayListExtra(LibraryService.EXTRA_GET_ARTICLE_NUMBER_LIST, articleNumbers);
                    sendBroadcast(intent);
                    break;

                case R.id.button3:
                    Log.d("DummyActivity", "Button 3 clicked");
                    intent = new Intent(LibraryService.ACTION_GET_LATEST_ARTICLES);
                    sendBroadcast(intent);
                    break;
            }
        }
    };


    private BroadcastReceiver mLibraryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            // Get article
            //------------
            if (intent.getAction().equals(LibraryService.ACTION_GET_ARTICLE_COMPLETE))
            {
                Log.d("DummyActivity", "ACTION_GET_ARTICLE_COMPLETE intent received");
                int status = intent.getIntExtra(LibraryService.EXTRA_STATUS, 0);
                int articleNumber = intent.getIntExtra(LibraryService.EXTRA_ARTICLE_NUMBER, 0);

                // Parse status
                switch (status)
                {
                    case LibraryService.STATUS_SUCCESSFUL:
                        Log.d("DummyActivity", "STATUS_SUCCESSFUL");
                        String filename;
                        String uri;
                        filename = intent.getStringExtra(LibraryService.EXTRA_ARTICLE_CONTENT_FILENAME);
                        uri = intent.getStringExtra(LibraryService.EXTRA_ARTICLE_CONTENT_URI);
                        Log.d("DummyActivity", uri+filename);

                        switch (articleNumber)
                        {
                            case 1:
                                mTextView1.setText(uri + "/" + filename);
                                break;

                            case 2:
                                mTextView2.setText(uri + "/" + filename);
                                break;
                        }

                        break;

                    case LibraryService.STATUS_FAILED:
                        Log.d("DummyActivity", "STATUS_FAILED");
                        switch (articleNumber)
                        {
                            case 1:
                                mTextView1.setText("failed :-(");
                                break;

                            case 2:
                                mTextView2.setText("failed :-(");
                                break;
                        }
                        break;
                }
            }

            // Get latest articles
            //--------------------
            else if (intent.getAction().equals(LibraryService.ACTION_GET_LATEST_ARTICLES_COMPLETE))
            {


            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver
        unregisterReceiver(mLibraryReceiver);

    }
}