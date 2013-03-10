package fr.free.gelmir.lerubanbleu.library;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */
// I will use BroadcastReceiver as indicated in:
// http://developer.samsung.com/android/technical-docs/Effective-communication-between-Service-and-Activity

public class DownloadService extends Service
{
    static final String ACTION_DOWNLOAD_COMPLETED = "download completed";

    private DownloadManager mDownloadManager = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        // Register the receiver
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryService.ACTION_GET_ARTICLE);
        intentFilter.addAction(LibraryService.ACTION_GET_LATEST_ARTICLES);
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mReceiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the receiver
        unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {



        }
    };


/*
    // Check URL

    // Register receiver
    this.registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    // Request download
    mDownloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
    Uri downloadUri = Uri.parse(url);
    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
    request.setVisibleInDownloadsUi(false);
    request.setShowRunningNotification(false);
    mId = mDownloadManager.enqueue(request);

    return mId;
    */

}
