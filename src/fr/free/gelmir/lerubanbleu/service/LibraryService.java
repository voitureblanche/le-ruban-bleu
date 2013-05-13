package fr.free.gelmir.lerubanbleu.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import fr.free.gelmir.lerubanbleu.util.CustomIntentService;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 09/03/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class LibraryService extends CustomIntentService
{
    // Actions
    public final static String ACTION_GET_EPISODE = "fr.free.gelmir.service.LibraryService.actionGetEpisode";
    public final static String ACTION_CANCEL_ALL = "fr.free.gelmir.service.LibraryService.actionCancelAll";

    // Extras
    public final static String EXTRA_EPISODE_POJO = "fr.free.gelmir.service.LibraryService.extraEpisodePojo";
    public final static String EXTRA_EPISODE_NB = "fr.free.gelmir.service.LibraryService.extraEpisodeNb";
    public final static String EXTRA_RESULT_RECEIVER = "fr.free.gelmir.service.LibraryService.extraResultReceiver";
    public final static String EXTRA_ORIGINAL_INTENT = "fr.free.gelmir.service.LibraryService.extraOriginalIntent";

    // Members
    private EpisodeProcessor mEpisodeProcessor;
    private ResultReceiver mResultReceiver;
    private Intent mIntent;

    // Result
    public enum Result {
        OK,
        KO
    }

    public LibraryService() {
        super("LibraryService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mEpisodeProcessor = new EpisodeProcessor(this);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // Save intent
        mIntent = intent;

        // Get request data from intent
        mResultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

        // Get episode
        //------------
        if (intent.getAction().equals(ACTION_GET_EPISODE)) {
            Log.d("LibraryService", "ACTION_GET_EPISODE received");

            // Get intent extras
            int episodeId = intent.getIntExtra(EXTRA_EPISODE_NB, -1);

            // Get episode
            EpisodeProcessorCallback callback = makeEpisodeProcessorCallback();
            mEpisodeProcessor.queryEpisode(episodeId, callback, this);
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


    private EpisodeProcessorCallback makeEpisodeProcessorCallback() {
        EpisodeProcessorCallback callback = new EpisodeProcessorCallback() {
            @Override
            public void send(Result result, Episode episode) {

                Log.d("EpisodeProcessorCallback", "Callback called!");

                int resultCode = 0;

                // Handle result
                switch (result) {
                    case EPISODE_PROCESSOR_OK:
                        resultCode = 0;
                        break;

                    case EPISODE_PROCESSOR_KO:
                        resultCode = -1;
                        break;
                }

                // Send to result receiver
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_ORIGINAL_INTENT, mIntent);
                bundle.putParcelable(EXTRA_EPISODE_POJO, episode);
                mResultReceiver.send(resultCode, bundle);

            }
        };
        return callback;
    }

}