package fr.free.gelmir.lerubanbleu.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import fr.free.gelmir.lerubanbleu.provider.EpisodeProvider;
import fr.free.gelmir.lerubanbleu.provider.EpisodeTable;
import fr.free.gelmir.lerubanbleu.util.EpisodeSaxParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 16/03/13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeProcessor
{
    private Context mContext;
    private Episode mEpisode;

    public EpisodeProcessor(Context context)
    {
        mContext = context;
    }


    public void queryEpisode(int episodeNb, EpisodeProcessorCallback callback)
    {
        int result = 0;

        // Query the episode
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri episodeUri = Uri.withAppendedPath(EpisodeProvider.CONTENT_URI, Integer.toString(episodeNb));
        Cursor cursor = contentResolver.query(episodeUri, null, null, null, null);

        // Fetch data from database
        if (cursor.moveToFirst())
        {
            mEpisode = new Episode();
            mEpisode.setEpisodeNb(episodeNb);
            Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(EpisodeTable.COLUMN_IMAGE_URI)));
            mEpisode.setImageUri(uri);
        }

        // Download episode because it has not been downloaded yet
        else {
            //int columnIndex = cursor.getColumnIndex(EpisodeTable.COLUMN_STATUS);
            //int status = cursor.getInt(columnIndex);
            //int columnReason = cursor.getColumnIndex(EpisodeTable.COLUMN_RESULT);
            //int reason = cursor.getInt(columnReason);

            // TODO Test network availability
            isNetworkAvailable();

            // HTTP request
            HttpURLConnection connection = null;
            try {
                // Create URL
                URL url = urlCreate("http://gelmir.free.fr/lerubanbleu/get.php5?episode=" + URLEncoder.encode(Integer.toString(episodeNb), "UTF-8"));

                // HTTP request
                connection = (HttpURLConnection) url.openConnection();  // this does no network IO
                InputStream inputStream = connection.getInputStream();  // this opens a connection, then sends GET & headers
                int httpStatus = connection.getResponseCode();

                // Error!
                if (httpStatus / 100 != 2) {
                    result = -1;
                }

                // Handle result
                else {
                    // Get XML
                    //String xml;
                    //xml = getXml(inputStream);
                    //Log.d("EpisodeProcessor", xml);

                    // Parse XML
                    EpisodeSaxParser parser = new EpisodeSaxParser();
                    mEpisode = parser.getEpisode(inputStream);

                    // Update database
                    //ContentValues contentValues = new ContentValues();
                    //contentValues.put();
                    //contentResolver.insert(EpisodeProvider.CONTENT_URI, contentValues);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

        }


        // Callback
        callback.send(result, mEpisode);

    }


    private String getXml(InputStream is)
    {
        String xml = null;
        BufferedReader br = null;
        StringBuilder sb = null;
        try {
            //GZIPInputStream gzis = new GZIPInputStream(is);
            //br = new BufferedReader(new InputStreamReader(gzis));
            br = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuilder();
            char[] buffer = new char[1000];
            int charsRead;

            // Get data from stream
            while ((charsRead = br.read(buffer)) != -1) {
                sb.append(buffer, 0, charsRead);
            }
            br.close();

            // Get XML
            xml = sb.toString();
            Log.d("EpisodeProcessor", Integer.toString(xml.length()));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xml;
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

}

