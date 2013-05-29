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
import android.widget.Toast;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.util.XmlSaxParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 04/05/13
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public class SplashActivity extends Activity {

    private Thread mSplashThread = null;
    private AsyncTask mSplashAsyncTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ac_splash);

        // Allocate async task
        mSplashAsyncTask = new SplashAsyncTask();

        // Launch thread
        Runnable splashRunnable = new SplashRunnable(mSplashAsyncTask);
        mSplashThread = new Thread(splashRunnable);
        mSplashThread.start();
    }


    private void startSplash()
    {
        // Check network
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Get number of episodes
        if (networkInfo != null && networkInfo.isAvailable()) {
            Log.d("SplashActivity", "launching asynctask!");
            mSplashAsyncTask.execute((Void[]) null);
        }

        // The network is not available
        else {
            Log.d("SplashActivity", "no network available!");

            // Retrieve total number of episodes
            LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
            int totalNbEpisodes = application.getTotalNbEpisodes();

            // First start: display an error
            if (totalNbEpisodes == 0) {
                Toast.makeText(this, "Grmbl !?", Toast.LENGTH_LONG).show();

                // TODO: add a refresh button
            }

            // Launch the gallery
            else {
                finish();
                Intent intent = new Intent(SplashActivity.this, ViewerActivity.class);
                SplashActivity.this.startActivity(intent);
            }
        }
    }


    private void endSplash()
    {
        // Launch main activity
        // Retrieve total number of episodes
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        int totalNbEpisodes = application.getTotalNbEpisodes();

        // First start: display an error
        if (totalNbEpisodes == 0) {
            Toast.makeText(getApplicationContext(), "Grmbl !?", Toast.LENGTH_LONG).show();

            // TODO: add a refresh button
        }

        // Launch the viewer
        else {
            finish();
            Intent intent = new Intent(SplashActivity.this, ViewerActivity.class);
            SplashActivity.this.startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSplashAsyncTask != null) {
                mSplashAsyncTask.cancel(true);
                mSplashAsyncTask = null;
            }
            mSplashThread.interrupt();
        }
        return super.onKeyDown(keyCode, event);
    }


    private class SplashAsyncTask extends AsyncTask<Void, Void, Integer>
    {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected Integer doInBackground(Void... params) {
            URL url;
            HttpURLConnection connection = null;
            int errorCode = 0;

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
                Log.d("SplashAsyncTask", "http status = " + Integer.toString(httpStatus));

                // Error!
                if (httpStatus / 100 != 2) {
                    errorCode = 0;
                }

                // Handle result
                else {
                    // Parse XML
                    GZIPInputStream gzis = new GZIPInputStream(inputStream);
                    XmlSaxParser parser = new XmlSaxParser();
                    errorCode = parser.getTotalNumber(gzis);
                    Log.d("SplashAsyncTask", "total number of episodes = " + Integer.toString(errorCode));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                connection.disconnect();
                }
            }

            return errorCode;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(Integer errorCode) {

            // Save total number of episodes in the application preferences
            if (errorCode != 0) {
                LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
                application.setTotalNbEpisodes(errorCode);
            }

            // End the splash session
            endSplash();
        }
    }


    // Runnable that will 1) sleep 2) launch the startSplash() function 3) ensure the async task does not last for a too long time
    // Runnable can be interrupted by the user
    private class SplashRunnable implements Runnable {

        //AsyncTask mAsyncTask;

        // Constructor
        public SplashRunnable(AsyncTask asyncTask) {
            //mAsyncTask = asyncTask;
        }

        public void run() {
            try {
                Thread.sleep(2000);
                runOnUiThread(new Runnable() {
                    public void run() {
                        startSplash();
                    }
                });
                mSplashAsyncTask.get(4000, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                Log.d("SplashRunnable", "splash cancelled");
                finish();
            }
            catch (TimeoutException e) {
                Log.d("SplashRunnable", "splash timeout");
                mSplashAsyncTask.cancel(true);
                runOnUiThread(new Runnable() {
                    public void run() {
                        endSplash();
                    }
                });
            }
            catch (ExecutionException e) {
                Log.d("SplashRunnable", "splash error");
                finish();
            }
        }
    }

}
