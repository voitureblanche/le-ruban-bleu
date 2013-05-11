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
        EpisodeProcessorCallback.Result result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_OK;
        Cursor cursor;

        // Database variables
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri episodeUri = Uri.withAppendedPath(EpisodeProvider.CONTENT_URI, Integer.toString(episodeNb));
        Log.d("EpisodeProcessor", episodeUri.toString());

        // Query the episode
        cursor = contentResolver.query(episodeUri, null, null, null, null);

        // Fetch data from database
        if (cursor.moveToFirst()) {

            // Read status
            int status = cursor.getInt(cursor.getColumnIndex(EpisodeTable.COLUMN_STATUS));
            switch (status)
            {
                // The episode has already been downloaded successfully
                case EpisodeTable.STATUS_SUCCESSFUL:

                    Log.d("EpisodeProcessor", "Episode successfully found in the database!");

                    // Fill episode POJO
                    mEpisode = new Episode();
                    mEpisode.setEpisodeNb(episodeNb);
                    mEpisode.setImageUri(Uri.parse(cursor.getString(cursor.getColumnIndex(EpisodeTable.COLUMN_IMAGE_URI))));

                    break;

                // The episode could not be downloaded
                case EpisodeTable.STATUS_FAILED:

                    Log.d("EpisodeProcessor", "Episode with FAILED status found in the database!");

                    // Test network availability
                    if (isNetworkAvailable())
                    {
                        // Relaunch download
                        result = download(cursor, context);
                    }
                    else {

                        // Update database with FAILED status and NO_NETWORK reason
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(EpisodeTable.COLUMN_REASON, EpisodeTable.ERROR_NO_NETWORK);
                        contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_FAILED);
                        String where = EpisodeTable.COLUMN_ID + " = ? ";
                        int id = cursor.getInt(cursor.getColumnIndex(EpisodeTable.COLUMN_ID));
                        String[] whereArgs = { Integer.toString(id) };
                        contentResolver.update(episodeUri, contentValues, where, whereArgs);

                        // Returned parameters
                        mEpisode = null;
                        result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_KO;
                    }
                    break;

                // Should never happen
                default:

                    // Returned parameters
                    mEpisode = null;
                    result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_KO;
                    break;
            }

        }

        // The episode has not been found in the table
        else {
            Log.d("EpisodeProcessor", "No episode found in the database!");

            // Insert episode in the table
            ContentValues contentValues = new ContentValues();
            contentValues.put(EpisodeTable.COLUMN_EPISODE_NB, episodeNb);
            contentResolver.insert(episodeUri, contentValues);

            // Get cursor
            cursor = contentResolver.query(episodeUri, null, null, null, null);

            // Download episode
            result = download(cursor, context);
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

        if (networkInfo != null && networkInfo.isAvailable()) {
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

    private EpisodeProcessorCallback.Result download(Cursor cursor, Context context)
    {
        cursor.moveToFirst();

        // Database variables
        String where = EpisodeTable.COLUMN_ID + " = ? ";
        int id = cursor.getInt(cursor.getColumnIndex(EpisodeTable.COLUMN_ID));
        String[] whereArgs = { Integer.toString(id) };
        int episodeNb = cursor.getInt(cursor.getColumnIndex(EpisodeTable.COLUMN_EPISODE_NB));
        ContentResolver contentResolver = context.getContentResolver();
        Uri episodeUri = Uri.withAppendedPath(EpisodeProvider.CONTENT_URI, Integer.toString(episodeNb));

        EpisodeProcessorCallback.Result result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_KO;
        HttpURLConnection connection = null;
        ContentValues contentValues = new ContentValues();

        // HTTP request
        try {

            // Update database with RUNNING status
            contentValues.clear();
            contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_RUNNING);
            contentResolver.update(episodeUri, contentValues, where, whereArgs);

            // Create URL
            URL url = urlCreate("http://gelmir.free.fr/lerubanbleu/get.php5?episode=" + URLEncoder.encode(Integer.toString(episodeNb), "UTF-8"));

            // HTTP request
            connection = (HttpURLConnection) url.openConnection();  // this does no network IO
            InputStream inputStream = connection.getInputStream();  // this opens a connection, then sends GET & headers
            int httpStatus = connection.getResponseCode();
            Log.d("EpisodeProcessor", "http status = " + Integer.toString(httpStatus));

            // Error!
            if (httpStatus / 100 != 2) {
                Log.d("EpisodeProcessor", "Download failed!");

                // Update database
                contentValues.clear();
                contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_FAILED);
                contentValues.put(EpisodeTable.COLUMN_REASON, httpStatus);
                contentResolver.update(episodeUri, contentValues, where, whereArgs);

                result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_KO;
            }

            // Handle result
            else {
                Log.d("EpisodeProcessor", "Download successful!");

                // Parse XML
                GZIPInputStream gzis = new GZIPInputStream(inputStream);
                XmlSaxParser parser = new XmlSaxParser();
                mEpisode = parser.getEpisode(gzis, context);
                result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_OK;

                // Update database
                contentValues.clear();
                contentValues.put(EpisodeTable.COLUMN_IMAGE_URI, mEpisode.getImageUri().toString());
                contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_SUCCESSFUL);
                contentValues.put(EpisodeTable.COLUMN_REASON, httpStatus);
                contentResolver.update(episodeUri, contentValues, where, whereArgs);
            }

        } catch (IOException e) {
            Log.d("EpisodeProcessor", "exception!");

            // Update database
            contentValues.clear();
            contentValues.put(EpisodeTable.COLUMN_STATUS, EpisodeTable.STATUS_FAILED);
            contentValues.put(EpisodeTable.COLUMN_REASON, EpisodeTable.ERROR_UNKNOWN);
            contentResolver.update(episodeUri, contentValues, where, whereArgs);

            result = EpisodeProcessorCallback.Result.EPISODE_PROCESSOR_KO;

            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }
}
