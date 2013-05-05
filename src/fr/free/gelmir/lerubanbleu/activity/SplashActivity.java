package fr.free.gelmir.lerubanbleu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.util.XmlSaxParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 04/05/13
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public class SplashActivity extends Activity {

    private Thread mWaitThread = null;
    private AsyncTask mFetchDataTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        startWaitThread();
    }


    //
    private void startWaitThread()
    {
        mWaitThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        public void run() {
                           fetchData();
                        }
                    });
                }
                catch (InterruptedException e) {
                    //Log.d(HFR4droidApplication.TAG, "Launch cancelled");
                    finish();
                }
            }
        });
        mWaitThread.start();
    }


    private void fetchData()
    {
        // Check network
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Get number of episodes
        if (networkInfo != null && networkInfo.isAvailable()) {
            Log.d("SplashActivity", "fetching data!");
            mFetchDataTask = new GetTotalNumberTask();
            mFetchDataTask.execute((Void[]) null);
        }

        // Launch main activity
        else {
            Log.d("SplashActivity", "no network available!");
            finish();
            Intent intent = new Intent(SplashActivity.this, ViewerActivity.class);
            SplashActivity.this.startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mFetchDataTask != null)
            {
                mFetchDataTask.cancel(true);
                mFetchDataTask = null;
            }
            mWaitThread.interrupt();
        }
        return super.onKeyDown(keyCode, event);
    }


    private class GetTotalNumberTask extends AsyncTask<Void, Void, Integer>
    {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected Integer doInBackground(Void... params)
        {
            URL url;
            HttpURLConnection connection = null;
            int totalNumber = 0;

            try {
                url = new URL("http://gelmir.free.fr/lerubanbleu/get.php5?totalnumber");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            try {
                // HTTP request
                connection = (HttpURLConnection) url.openConnection();  // this does no network IO
                InputStream inputStream = connection.getInputStream();  // this opens a connection, then sends GET & headers
                int httpStatus = connection.getResponseCode();
                Log.d("GetTotalNumberTask", "http status = " + Integer.toString(httpStatus));

                // Error!
                if (httpStatus / 100 != 2) {
                    totalNumber = -1;
                }

                // Handle result
                else
                {
                    // Parse XML
                    GZIPInputStream gzis = new GZIPInputStream(inputStream);
                    XmlSaxParser parser = new XmlSaxParser();
                    totalNumber = parser.getTotalNumber(gzis);
                    Log.d("GetTotalNumberTask", "total number of episodes = " + Integer.toString(totalNumber));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                connection.disconnect();
                }
            }

            return totalNumber;

        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(Integer totalNumber)
        {
            /*
            if (totalNumber != 0) {
                // TODO: update preferences

            }
            */

            // Launch main activity
            finish();
            Intent intent = new Intent(SplashActivity.this, ViewerActivity.class);
            SplashActivity.this.startActivity(intent);
        }
    }

}
