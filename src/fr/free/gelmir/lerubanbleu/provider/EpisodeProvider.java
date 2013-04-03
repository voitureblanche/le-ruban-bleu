package fr.free.gelmir.lerubanbleu.provider;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 16/03/13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeProvider extends ContentProvider
{
    // URI authority, paths
    private static final String AUTHORITY = "fr.free.gelmir.lerubanbleu.provider.articleprovider";
    private static final String BASE_PATH = "articles";

    // URI
    public static final Uri    CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // MIME type for directories and items
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.lerubanbleu.article";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.lerubanbleu.article";

    // URI Matcher
    private static final int EPISODES = 1;
    private static final int EPISODE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, EPISODES);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", EPISODE_ID);
    }

    // Database open helper
    EpisodeDatabaseOpenHelper mEpisodeDatabaseOpenHelper;



    @Override
    public boolean onCreate() {
        mEpisodeDatabaseOpenHelper = new EpisodeDatabaseOpenHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        // SQLiteQueryBuilder is a helper class that creates the proper SQL syntax for us
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table we're querying
        queryBuilder.setTables(EpisodeTable.TABLE_NAME);

        // Match URI
        switch (sUriMatcher.match(uri))
        {
            // Episode id = image id
            case EPISODE_ID:
                queryBuilder.appendWhere(EpisodeTable.COLUMN_EPISODE_ID + "=" + uri.getLastPathSegment());
                break;

            // Error
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        // Get database
        SQLiteDatabase database = mEpisodeDatabaseOpenHelper.getWritableDatabase();

        // Make the query
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);

        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public String getType(Uri uri)
    {
        // Match URI
        switch (sUriMatcher.match(uri))
        {
            case EPISODES:
                return EpisodeProvider.CONTENT_TYPE;
            case EPISODE_ID:
                return EpisodeProvider.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        // Match URI
        if (sUriMatcher.match(uri) != EPISODE_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get content values
        ContentValues values;
        if (contentValues != null) {
            values = new ContentValues(contentValues);
        } else {
            values = new ContentValues();
        }

        // Make sure that the fields are all set ?
        if (!values.containsKey(EpisodeTable.COLUMN_IMAGE)) {
        }

        if (!values.containsKey(EpisodeTable.COLUMN_EPISODE_ID)) {
        }

        if (!values.containsKey(EpisodeTable.COLUMN_RESULT)) {
        }

        if (!values.containsKey(EpisodeTable.COLUMN_STATUS)) {
        }

        // Insert into database
        SQLiteDatabase database = mEpisodeDatabaseOpenHelper.getWritableDatabase();
        long rowId = database.insert(EpisodeTable.TABLE_NAME, EpisodeTable.COLUMN_RESULT, values);

        // Notify and return
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(EpisodeProvider.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs)
    {
        SQLiteDatabase database = mEpisodeDatabaseOpenHelper.getWritableDatabase();
        int count = 0;

        // Match URI
        switch(sUriMatcher.match(uri)) {
            case EPISODES:
                count = database.delete(EpisodeTable.TABLE_NAME, where, whereArgs);
                break;

            case EPISODE_ID:
                String rowId = uri.getLastPathSegment();
                count = database.delete(EpisodeTable.TABLE_NAME, EpisodeTable.COLUMN_ID + " = " + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;


    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] whereArgs)
    {
        SQLiteDatabase database = mEpisodeDatabaseOpenHelper.getWritableDatabase();
        int count = 0;

        // Match URI
        switch (sUriMatcher.match(uri)) {
            case EPISODES:
                count = database.update(EpisodeTable.TABLE_NAME, contentValues, where, whereArgs);
                break;

            case EPISODE_ID:
                String rowId = uri.getLastPathSegment();
                count = database.update(EpisodeTable.TABLE_NAME, contentValues, EpisodeTable.COLUMN_ID + "=" + rowId + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
