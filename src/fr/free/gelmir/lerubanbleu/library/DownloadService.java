package fr.free.gelmir.lerubanbleu.library;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */
// I will use Messenger as indicated in:
// http://developer.samsung.com/android/technical-docs/Effective-communication-between-Service-and-Activity

public class DownloadService extends Service
{
    // Keeps track of all current registered clients
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();

    // Messages
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_DOWNLOAD_FILE = 3;

    // Handler of incoming messages from clients
    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case MSG_REGISTER_CLIENT:
                    mClients.add(message.replyTo);
                    break;

                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(message.replyTo);
                    break;

                case MSG_DOWNLOAD_FILE:
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

                    break;

                default:
                    super.handleMessage(message);
            }
        }
    }

    // Messenger we publish for clients to send messages to IncomingHandler.
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel on-going downloads
    }


    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Check the id and the action
            String action = intent.getAction();
            if (mDownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {



            }
        }
    };

    
}
