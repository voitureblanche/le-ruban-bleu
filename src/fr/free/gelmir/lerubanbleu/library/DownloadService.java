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

    private DownloadManager mDownloadManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDownloadManager = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);

        // Register the library receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LibraryService.ACTION_GET_ARTICLES);
        intentFilter.addAction(LibraryService.ACTION_GET_LATEST_ARTICLES);
        registerReceiver(mLibraryServiceReceiver, intentFilter);

        // Register the download manager receiver
        intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mDownloadManagerReceiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the receivers
        unregisterReceiver(mLibraryServiceReceiver);
        unregisterReceiver(mDownloadManagerReceiver);

    }

    private BroadcastReceiver mLibraryServiceReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            /*
            // Check URL

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

    };

    private BroadcastReceiver mDownloadManagerReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // A download has been completed, sending intent to Library
            Intent intentForLibrary = new Intent();
            intent.setAction(ACTION_DOWNLOAD_COMPLETED);
            sendBroadcast(intentForLibrary);

        }
    };

}
