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
import fr.free.gelmir.lerubanbleu.library.LibraryService;

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
        intentFilter.addAction(LibraryService.ACTION_ARTICLE_COMPLETE);
        registerReceiver(mLibraryReceiver, intentFilter);

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            Intent intent;
            ArrayList<Integer> articleNumbers = new ArrayList<Integer>();

            switch (view.getId())
            {
                case R.id.button1:
                    Log.d("DummyActivity", "Button 1 clicked");
                    intent = new Intent(LibraryService.ACTION_GET_ARTICLES);
                    articleNumbers.add(1);
                    intent.putIntegerArrayListExtra(LibraryService.EXTRA_ARTICLE_NUMBERS_LIST, articleNumbers);
                    sendBroadcast(intent);

                    break;

                case R.id.button2:
                    Log.d("DummyActivity", "Button 1 clicked");
                    intent = new Intent(LibraryService.ACTION_GET_ARTICLES);
                    articleNumbers.add(2);
                    intent.putIntegerArrayListExtra(LibraryService.EXTRA_ARTICLE_NUMBERS_LIST, articleNumbers);
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
            Log.d("DummyActivity", "LibraryReceiver event received!");

            // Article is available, display it
            if (intent.getAction().equals(LibraryService.ACTION_ARTICLE_COMPLETE)) {
                Log.d("DummyActivity", "ACTION_ARTICLE_COMPLETE event received!");

                int articleNumber;
                String filename;

                articleNumber = intent.getIntExtra(LibraryService.EXTRA_ARTICLE_NUMBER, 0);
                filename = intent.getStringExtra(LibraryService.EXTRA_ARTICLE_CONTENT_FILENAME);

                switch (articleNumber)
                {
                    case 1:
                        mTextView1.setText(filename);
                        break;

                    case 2:
                        mTextView2.setText(filename);
                        break;
                }
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