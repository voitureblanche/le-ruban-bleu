package fr.free.gelmir.lerubanbleu.library;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    static final String ACTION_DOWNLOAD_FILE = "download file";

    @Override
    public IBinder onBind(Intent intent) {

        // Start feed update

        // Get latest articles

        // Start download task

        return null;
    }

    private BroadcastReceiver mUiReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Get article

            // Get latest articles from the UI or from

        }
    };

    private BroadcastReceiver mDownloadServiceReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Download completed

        }

    };

}