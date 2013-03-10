package fr.free.gelmir.lerubanbleu.library;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */
public class Downloader
{
    private long mId;
    private DownloadManager mDownloadManager;

    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check the id and the action
            String action = intent.getAction();
            if (mDownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {


                // Return the file path
            }

        }
    };

    public long start(String url, String fileName, Context context)
    {
        // Check URL

        // Register receiver
        context.registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Request download
        mDownloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setVisibleInDownloadsUi(false);
        request.setShowRunningNotification(false);
        mId = mDownloadManager.enqueue(request);

        return mId;
    }




}
