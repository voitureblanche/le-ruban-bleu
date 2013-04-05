package fr.free.gelmir.lerubanbleu.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 18/03/13
 * Time: 00:52
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeTable
{
    // Table
    public static final String TABLE_NAME           = "lerubanbleu";
    public static final String COLUMN_ID            = "_id";
    public static final String COLUMN_EPISODE_NB    = "episode_nb";
    public static final String COLUMN_IMAGE_URI     = "image_uri";
    public static final String COLUMN_STATUS        = "status";
    public static final String COLUMN_RESULT        = "result";

    // EpisodeTable creation SQL statement
    private static final String SQL_TABLE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EPISODE_NB + " integer not null, "
            + COLUMN_IMAGE_URI + " text not null, "
            + COLUMN_STATUS + " text, "
            + COLUMN_RESULT + " text "
            + ");";

    // Create table
    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(SQL_TABLE_CREATE);
    }

    // Upgrade table
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        Log.w(EpisodeTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
