package fr.free.gelmir.lerubanbleu.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import fr.free.gelmir.lerubanbleu.provider.ArticleProvider;
import fr.free.gelmir.lerubanbleu.provider.ArticleTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 16/03/13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class ArticleProcessor
{
    private Context mContext;

    public ArticleProcessor(Context context)
    {
        mContext = context;
    }


    public void queryArticle(ArticleProcessorCallback articleProcessorCallback, int articleId) {

        ContentResolver contentResolver = mContext.getContentResolver();

        // Query the article
        Uri uri = Uri.withAppendedPath(ArticleProvider.CONTENT_URI, Integer.toString(articleId));
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        // Download article because it has not been downloaded yet
        if (cursor.moveToFirst())
        {
            int columnIndex = cursor.getColumnIndex(ArticleTable.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor.getColumnIndex(ArticleTable.COLUMN_RESULT);
            int reason = cursor.getInt(columnReason);

            // HTTP request
            try {
                URL url = new URL("http://www.lerubanbleu.com/get.php?article=" + Integer.toString(articleId));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                readStream(connection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Update database
            ContentValues contentValues = new ContentValues();
            // contentValues.put();
            contentResolver.insert(ArticleProvider.CONTENT_URI, contentValues);

        }
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

