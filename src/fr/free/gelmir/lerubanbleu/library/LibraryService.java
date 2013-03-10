package fr.free.gelmir.lerubanbleu.library;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class LibraryService extends Service
{

    @Override
    public IBinder onBind(Intent intent) {

        // Start feed update

        // Get latest articles

        // Start download task
        new DownloadTask().execute();

        return null;
    }


    public void getLatestArticles() {
        // Relaunch a feed update

    }

    public void getArticle() {

    }


    private class FeedTask extends AsyncTask<void, void, void>
    {
        @Override
        protected void doInBackground(void... voids) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class DownloadTask extends AsyncTask<void, void, void>
    {
        @Override
        protected void doInBackground(void... voids) {



        }

        public void AddDownload(String url, String filename) {

            // Add download to download list

            // Allocate downloader
            Downloader downloader = new Downloader();
            downloader.start(url, filename, this);
        }

    }

}
