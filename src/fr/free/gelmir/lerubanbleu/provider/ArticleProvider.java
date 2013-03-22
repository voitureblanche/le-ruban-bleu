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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        // SQLiteQueryBuilder is a helper class that creates the
        // proper SQL syntax for us.
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        // Set the table we're querying.
        qBuilder.setTables(ArticleDatabase.TABLE_NAME);

        // Check if the caller has requested a column which does not exists
        // checkColumns(projection);

        // If the query ends in a specific record number, we're
        // being asked for a specific record, so set the
        // WHERE clause in our query.
        /*
        if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
            qBuilder.appendWhere("_id=" + uri.getPathLeafId());
        }
        */

        // Build query
        // queryBuilder.appendWhere(ArticleDatabase.COLUMN_ID + "="  + uri.getLastPathSegment());

        // Get database
        SQLiteDatabase db = mArticleDatabaseOpenHelper.getWritableDatabase();

        // Make the query
        Cursor cursor = qBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

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
