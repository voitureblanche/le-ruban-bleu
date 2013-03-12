package fr.free.gelmir.lerubanbleu.library;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import fr.free.gelmir.lerubanbleu.feed.RssSaxParser;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class LibraryService extends Service
{
    public final static String ACTION_GET_ARTICLE = "fr.free.gelmir.LibraryService.getArticle";
    public final static String ACTION_GET_LATEST_ARTICLES = "fr.free.gelmir.LibraryService.getLatestArticles";
    public final static String ACTION_CANCEL_ALL = "fr.free.gelmir.LibraryService.cancelAll";
    public final static String ACTION_ARTICLE_COMPLETE = "fr.free.gelmir.LibraryService.articleComplete";
    public final static String ACTION_LATEST_ARTICLES_COMPLETE = "fr.free.gelmir.LibraryService.latestArticlesComplete";

    private DownloadManager mDownloadManager = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);
    private RssSaxParser    mRssParser = new RssSaxParser();

    @Override
    public IBinder onBind(Intent intent) {

        // Start feed update

        // Get latest articles

        // Start download task

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Library
        //--------

        // Register the receiver for LibraryService actions
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GET_ARTICLE);
        intentFilter.addAction(ACTION_GET_LATEST_ARTICLES);
        registerReceiver(mLibraryReceiver, intentFilter);

        // Download management
        //--------------------
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        // Register the receiver for DownloadManager actions
        intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mDownloadReceiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the receivers
        unregisterReceiver(mLibraryReceiver);
        unregisterReceiver(mDownloadReceiver);
    }

    private BroadcastReceiver mLibraryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Get article
            if (intent.getAction().equals(ACTION_GET_ARTICLE) ) {
                intent.getIntArrayExtra();

            }

            // Get latest articles
            else if (intent.getAction().equals(ACTION_GET_LATEST_ARTICLES)) {

            }
            // Cancel all actions
            else if (intent.getAction().equals(ACTION_CANCEL_ALL)) {

            }
        }
    };

    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Download completed



        }

    };

    private long downloadFile(String url) {
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setVisibleInDownloadsUi(false);
        request.setShowRunningNotification(false);
        return mDownloadManager.enqueue(request);
    }

    private

}