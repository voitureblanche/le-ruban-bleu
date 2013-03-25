package fr.free.gelmir.lerubanbleu.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private static LibraryServiceHelperResultReceiver mLibraryHelperResultReceiver;
    private HashMap mHashMap;


    // Intent
    //-------
    public final static String GET_ARTICLE_COMPLETE          = "fr.free.gelmir.service.LibraryServiceHelper.getArticleComplete";
    public final static String GET_LATEST_ARTICLES_COMPLETE  = "fr.free.gelmir.service.LibraryServiceHelper.getLatestArticlesComplete";

    // Status
    public final static int STATUS_FAILED     = 1;
    public final static int STATUS_SUCCESSFUL = 2;

    // Temporary, useless once we will pass the complete article
    public final static String EXTRA_ARTICLE_NUMBER             = "fr.free.gelmir.service.LibraryService.extraArticleNumber";
    public final static String EXTRA_ARTICLE_CONTENT_FILENAME   = "fr.free.gelmir.service.LibraryService.extraArticleContentFilename";
    public final static String EXTRA_ARTICLE_CONTENT_URI        = "fr.free.gelmir.service.LibraryService.extraArticleContentUri";

    // Extras
    public final static String EXTRA_GET_ARTICLE_NUMBER_LIST    = "fr.free.gelmir.service.LibraryService.extraGetArticleNumberList";
    public final static String EXTRA_STATUS                     = "fr.free.gelmir.service.LibraryService.extraGetArticleStatus";


    // Constructor
    protected LibraryServiceHelper(Context context) {
        mLibraryHelperResultReceiver = new LibraryServiceHelperResultReceiver(null);
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

    // Get article
    //------------
    public int getArticle(Context context, int articleId)
    {
        Log.d("LibraryServiceHelper", "Get article");

        int requestId = 1;

        // Is the method pending?

        // Start service
        Intent intent = new Intent(context, LibraryService.class);
        intent.setAction(LibraryService.ACTION_GET_ARTICLE);
        intent.putExtra(LibraryService.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(LibraryService.EXTRA_RESULT_RECEIVER, mLibraryHelperResultReceiver);
        context.startService(intent);

        // Store and return request id
        return requestId;
    }

    // Get latest articles
    //--------------------
    public void getLatestArticles(Context context)
    {
        Intent intent = new Intent(context, LibraryService.class);
        intent.setAction(LibraryService.ACTION_GET_LATEST_ARTICLES);
        context.startService(intent);
    }


    // Result receiver
    //----------------
    class LibraryServiceHelperResultReceiver extends ResultReceiver
    {
        public LibraryServiceHelperResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
        }
    }

}
