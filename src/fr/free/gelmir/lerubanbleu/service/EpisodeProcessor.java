package fr.free.gelmir.lerubanbleu.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import fr.free.gelmir.lerubanbleu.provider.EpisodeProvider;
import fr.free.gelmir.lerubanbleu.provider.EpisodeTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public EpisodeProcessor(Context context)
    {
        mContext = context;
    }


    public void queryEpisode(int articleId, EpisodeProcessorCallback callback)
    {
        ContentResolver contentResolver = mContext.getContentResolver();
        Episode episode = new Episode(articleId, null);
        int result = 0;

        // Query the episode
        Uri uri = Uri.withAppendedPath(EpisodeProvider.CONTENT_URI, Integer.toString(articleId));
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        // Download episode because it has not been downloaded yet
        if (cursor.moveToFirst())
        {
            int columnIndex = cursor.getColumnIndex(EpisodeTable.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor.getColumnIndex(EpisodeTable.COLUMN_RESULT);
            int reason = cursor.getInt(columnReason);

            // HTTP request
            try {
                URL url = new URL("http://www.lerubanbleu.com/get.php?episode=" + Integer.toString(articleId));
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    readStream(connection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Update database
            ContentValues contentValues = new ContentValues();
            // contentValues.put();
            contentResolver.insert(EpisodeProvider.CONTENT_URI, contentValues);

        }

        // Callback
        callback.send(result, episode);

    }


    private void readStream(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

