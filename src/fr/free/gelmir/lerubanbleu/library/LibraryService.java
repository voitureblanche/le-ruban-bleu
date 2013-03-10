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

    // Action
    static final String ACTION_GET_ARTICLE = "get article";
    static final String ACTION_GET_LATEST_ARTICLES = "get latest articles";


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



}
