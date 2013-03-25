package fr.free.gelmir.lerubanbleu.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import fr.free.gelmir.lerubanbleu.util.IntentService;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class LibraryService extends IntentService
{
    // Actions
    public final static String ACTION_GET_ARTICLE           = "fr.free.gelmir.service.LibraryService.actionGetArticle";
    public final static String ACTION_GET_LATEST_ARTICLES   = "fr.free.gelmir.service.LibraryService.actionGetLatestArticles";
    public final static String ACTION_CANCEL_ALL            = "fr.free.gelmir.service.LibraryService.actionCancelAll";

    // Extras
    public final static String EXTRA_ARTICLE         = "fr.free.gelmir.service.LibraryService.extraArticle";
    public final static String EXTRA_ARTICLE_ID      = "fr.free.gelmir.service.LibraryService.extraArticleId";
    public final static String EXTRA_RESULT_RECEIVER = "fr.free.gelmir.service.LibraryService.extraResultReceiver";

    // Members
    private ArticleProcessor mArticleProcessor;


    public LibraryService() {
        super("LibraryService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mArticleProcessor = new ArticleProcessor(this);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // Get article
        //------------
        if (intent.getAction().equals(ACTION_GET_ARTICLE))
        {
            Log.d("LibraryService", "ACTION_GET_ARTICLE received");

            // Get intent extras
            int articleId = intent.getIntExtra(LibraryService.EXTRA_ARTICLE_ID, -1);
            ResultReceiver resultReceiver = intent.getParcelableExtra(LibraryService.EXTRA_RESULT_RECEIVER);

            // Get article
            Article article;
            article = mArticleProcessor.queryArticle(articleId);

            // Return result
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_ARTICLE, article);
            resultReceiver.send(0, bundle);

        }

        // Cancel all actions
        //-------------------
        else if (intent.getAction().equals(ACTION_CANCEL_ALL)) {

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}