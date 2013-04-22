package fr.free.gelmir.lerubanbleu.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import fr.free.gelmir.lerubanbleu.provider.EpisodeProvider;
import fr.free.gelmir.lerubanbleu.provider.EpisodeTable;
import fr.free.gelmir.lerubanbleu.util.XmlSaxParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 16/03/13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeProcessor
{

    // Returned status
    public final static int STATUS_OK = 1;
    public final static int STATUS_KO = 2;

    private Context mContext;
    private Episode mEpisode;

    public EpisodeProcessor(Context context)
    {
        mContext = context;
    }


    public void queryEpisode(int episodeNb, EpisodeProcessorCallback callback, Context context)
    {
        EpisodeProcessorCallback.Result result = EpisodeProcessorCallback.Result.OK;

        // Database variables
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri episodeUri = Uri.withAppendedPath(EpisodeProvider.CONTENT_URI, Integer.toString(episodeNb));
        Log.d("EpisodeProcessor", episodeUri.toString());

        // Query the episode
        Cursor cursor = contentResolver.query(episodeUri, null, null, null, null);

        // Fetch data from database
        if (cursor.moveToFirst()) {
            // Read status
            int status = cursor.getInt(cursor.getColumnIndex(EpisodeTable.COLUMN_STATUS));
            switch (status)
            {
                // The episode has already been downloaded successfully
                case EpisodeTable.STATUS_SUCCESSFUL:

                    // Fill episode POJO
                    mEpisode = new Episode();
                    mEpisode.setEpisodeNb(episodeNb);
                    mEpisode.setImageUri(Uri.parse(cursor.getString(cursor.getColumnIndex(EpisodeTable.COLUMN_IMAGE_URI))));

                    break;

                // The episode could not be downloaded
                case EpisodeTable.STATUS_FAILED:

                    // Relaunch download
                    // Test network availability
                    if (isNetworkAvailable())
                    {
                        result = download(episodeNb, context, contentResolver, episodeUri);
                    }
                    else {
                        // TODO Update database with FAILED status and "no network" reason


                        mEpisode = null;
                        result = EpisodeProcessorCallback.Result.KO;
                    }
                    break;

                // Should never happen
                default:
                    mEpisode = null;
                    result = EpisodeProcessorCallback.Result.KO;
                    break;
            }

        }

        // The episode has never been downloaded yet
        else {

            // Insert episode in the table
            contentResolver.insert(episodeUri, new ContentValues());

            // Download episode
            result = download(episodeNb, context, contentResolver, episodeUri);
        }

        // Callback
        callback.send(result, mEpisode);

    }

    private boolean isNetworkAvailable() {
        boolean available = false;

        /** Getting the system's connectivity service */
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        /** Getting active network interface  to get the network's status */
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isAvailable()) {
            available = true;
        }

        /** Returning the status of the network */
        return available;
    }


    private URL urlCreate(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private EpisodeProcessorCallback.Result download(int episodeNb, Context context, ContentResolver contentResolver, Uri episodeUri)
    {

        EpisodeProcessorCallback.Result result = EpisodeProcessorCallback.Result.OK;
        HttpURLConnection connection = null;
        ContentValues contentValues = new ContentValues();


        // HTTP request
        try {

            // Update episode with PENDING status
            contentValues.clear();
            contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_PENDING);
            contentResolver.update(episodeUri, contentValues);

            // Create URL
            URL url = urlCreate("http://gelmir.free.fr/lerubanbleu/get.php5?episode=" + URLEncoder.encode(Integer.toString(episodeNb), "UTF-8"));

            // HTTP request
            connection = (HttpURLConnection) url.openConnection();  // this does no network IO
            InputStream inputStream = connection.getInputStream();  // this opens a connection, then sends GET & headers
            int httpStatus = connection.getResponseCode();

            // Error!
            if (httpStatus / 100 != 2) {
                result = EpisodeProcessorCallback.Result.KO;

                // Update database
                contentValues.clear();
                contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_FAILED);
                contentValues.put(EpisodeTable.COLUMN_RESULT, httpStatus);
                contentResolver.update(episodeUri, contentValues);
            }

            // Handle result
            else {
                // Parse XML
                GZIPInputStream gzis = new GZIPInputStream(inputStream);
                XmlSaxParser parser = new XmlSaxParser();
                mEpisode = parser.getEpisode(gzis, context);
                result = EpisodeProcessorCallback.Result.OK;

                // Update database
                contentValues.clear();
                contentValues.put(EpisodeTable.COLUMN_IMAGE_URI, mEpisode.getImageUri().toString());
                contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_SUCCESSFUL);
                contentResolver.update(episodeUri, contentValues);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }
}
