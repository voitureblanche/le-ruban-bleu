package fr.free.gelmir.lerubanbleu.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: de menditte
 * Date: 14/03/13
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public final class LibraryServiceHelper
{
    // Singleton
    //----------
    private static final Object mLock = new Object();
    private static LibraryServiceHelper mLibraryServiceHelperInstance;
    private static Context mApplicationContext;

    private HashMap mHashMap;


    // Intent
    public final static String GET_EPISODE_COMPLETE = "fr.free.gelmir.service.LibraryServiceHelper.getArticleComplete";
    public final static String GET_LATEST_ARTICLES_COMPLETE  = "fr.free.gelmir.service.LibraryServiceHelper.getLatestArticlesComplete";

    // Status
    public final static int STATUS_FAILED     = 1;
    public final static int STATUS_SUCCESSFUL = 2;

    // Extras
    public final static String EXTRA_EPISODE = "fr.free.gelmir.service.LibraryService.extraArticle";
    public final static String EXTRA_STATUS     = "fr.free.gelmir.service.LibraryService.extraStatus";


    // Constructor
    protected LibraryServiceHelper(Context context)
    {
        mApplicationContext = context.getApplicationContext();
    }

    public synchronized static LibraryServiceHelper getInstance(Context context)
    {
        synchronized (mLock) {
            if(mLibraryServiceHelperInstance == null){
                mLibraryServiceHelperInstance = new LibraryServiceHelper(context);
            }
        }

        return mLibraryServiceHelperInstance;
    }

    public void getEpisode(Context context, int episodeId)
    {
        Log.d("LibraryServiceHelper", "Get episode");

        // Is the method pending?

        // Allocate result receiver
        ResultReceiver resultReceiver = new ResultReceiver(null){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetEpisodeResponse(resultCode, resultData);
            }
        };

        // Start service
        Intent intent = new Intent(context, LibraryService.class);
        intent.setAction(LibraryService.ACTION_GET_EPISODE);
        intent.putExtra(LibraryService.EXTRA_EPISODE_ID, episodeId);
        intent.putExtra(LibraryService.EXTRA_RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    private void handleGetEpisodeResponse(int resultCode, Bundle resultData)
    {
        // Get original intent and retrieve the episode id
        Intent intent = resultData.getParcelable(LibraryService.EXTRA_ORIGINAL_INTENT);
        int articleId = intent.getIntExtra(LibraryService.EXTRA_EPISODE_ID, -1);

        // Get episode
        Episode episode = resultData.getParcelable(LibraryService.EXTRA_EPISODE);

        // Broadcast result
        Intent broadcastIntent = new Intent(GET_EPISODE_COMPLETE);
        broadcastIntent.putExtra(EXTRA_EPISODE, episode);
        broadcastIntent.putExtra(EXTRA_STATUS, resultCode);
        mApplicationContext.sendBroadcast(broadcastIntent);
    }

    public void getLatestEpisodes(Context context)
    {
        Intent intent = new Intent(context, LibraryService.class);
        intent.setAction(LibraryService.ACTION_GET_LATEST_EPISODES);
        context.startService(intent);
    }




}
