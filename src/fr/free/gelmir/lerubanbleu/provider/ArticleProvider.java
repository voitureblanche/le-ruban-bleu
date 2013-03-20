package fr.free.gelmir.lerubanbleu.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 16/03/13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class ArticleProvider extends ContentProvider {

    // Open helper
    ArticleDatabaseOpenHelper mArticleDatabaseOpenHelper;


    @Override
    public boolean onCreate() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2)
    {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Check if the caller has requested a column which does not exists
        //checkColumns(projection);

        // Set the table
        queryBuilder.setTables(mArticleDatabaseOpenHelper.TABLE_NAME);

        // Build query
        queryBuilder.appendWhere(mArticleDatabaseOpenHelper.COLUMN_ID + "="  + uri.getLastPathSegment());


        SQLiteDatabase db = mArticleDatabase.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
