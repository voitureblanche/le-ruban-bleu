package fr.free.gelmir.lerubanbleu.library;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.feed.RssSaxParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class LibraryService extends Service
{
    public final static String ACTION_GET_ARTICLES              = "fr.free.gelmir.LibraryService.getArticle";
    public final static String ACTION_GET_LATEST_ARTICLES       = "fr.free.gelmir.LibraryService.getLatestArticles";
    public final static String ACTION_CANCEL_ALL                = "fr.free.gelmir.LibraryService.cancelAll";
    public final static String ACTION_ARTICLE_COMPLETE          = "fr.free.gelmir.LibraryService.articleComplete";
    public final static String ACTION_LATEST_ARTICLES_COMPLETE  = "fr.free.gelmir.LibraryService.latestArticlesComplete";

    public final static String EXTRA_ARTICLE_NUMBERS_LIST       = "fr.free.gelmir.LibraryService.extraArticleNumbersList";

    // Temporary, useless once we will pass the complete article
    public final static String EXTRA_ARTICLE_NUMBER             = "fr.free.gelmir.LibraryService.extraArticleNumber";
    public final static String EXTRA_ARTICLE_CONTENT_FILENAME   = "fr.free.gelmir.LibraryService.extraArticleContentFilename";
    public final static String EXTRA_ARTICLE_CONTENT_URI        = "fr.free.gelmir.LibraryService.extraArticleContentUri";

    private DownloadManager mDownloadManager;
    private RssSaxParser    mRssParser;
    private HashMap         mHashMap;

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

        mDownloadManager = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);
        mRssParser       = new RssSaxParser();
        mHashMap         = new HashMap();

        // Library
        //--------

        // Register the receiver for LibraryService actions
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GET_ARTICLES);
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
            // Get articles
            //-------------
            if (intent.getAction().equals(ACTION_GET_ARTICLES) ) {
                ArrayList<Integer> articleNumbers = intent.getIntegerArrayListExtra(EXTRA_ARTICLE_NUMBERS_LIST);

                for(int i=0; i < articleNumbers.size(); i++) {
                    // Get article in database


                    // Or download content
                    long downloadId;

                    switch (articleNumbers.get(i)) {
                        case 1:
                            downloadId = downloadFile("http://www.lerubanbleu.com/images/Episode-012.jpg");
                            mHashMap.put(downloadId, 1);
                            break;

                        case 2:
                            downloadId = downloadFile("http://www.lerubanbleu.com/images/Episode-013.jpg");
                            mHashMap.put(downloadId, 2);
                            break;

                        default:
                            break;
                    }

                }

            }

            // Get latest articles
            //--------------------
            else if (intent.getAction().equals(ACTION_GET_LATEST_ARTICLES)) {

                // Relaunch feed update async task



            }


            // Cancel all actions
            //-------------------
            else if (intent.getAction().equals(ACTION_CANCEL_ALL)) {

            }
        }
    };

    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Log.d("LibraryService", "ACTION_DOWNLOAD_COMPLETE intent received");

                String uri;
                String title;

                // Query the completed download
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor cursor = mDownloadManager.query(query);
                if (cursor.moveToFirst())
                {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));

                        // Test if the file is present
                        /*
                        String path = uri + title;
                        File file = new File(path);
                        if (file.exists()) {
                            Log.d("LibraryService", "File exists");
                        }
                        else {
                            Log.d("LibraryService", "File does not exist!");
                        }
                        */

                        // Find article number in the hashmap
                        Integer articleNumber = (Integer) mHashMap.get(downloadId);

                        // Broadcast intent
                        Log.d("LibraryService", "ACTION_ARTICLE_COMPLETE intent sent");
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(ACTION_ARTICLE_COMPLETE);
                        broadcastIntent.putExtra(EXTRA_ARTICLE_NUMBER, articleNumber);
                        broadcastIntent.putExtra(EXTRA_ARTICLE_CONTENT_URI, uri);
                        broadcastIntent.putExtra(EXTRA_ARTICLE_CONTENT_FILENAME, title);
                        sendBroadcast(broadcastIntent);
                    }
                }
            }
        }
    };

    private long downloadFile(String url)
    {
        Log.d("LibraryService", "Requesting download");
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setVisibleInDownloadsUi(false);
        request.setShowRunningNotification(false);
        return mDownloadManager.enqueue(request);
    }


}