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

    // Reason
    public final static int ERROR_UNKNOWN = 1000;
    public final static int ERROR_FILE_ERROR = 1001;
    public final static int ERROR_UNHANDLED_HTTP_CODE = 1002;
    public final static int ERROR_HTTP_DATA_ERROR = 1004;
    public final static int ERROR_TOO_MANY_REDIRECTS = 1005;
    public final static int ERROR_INSUFFICIENT_SPACE = 1006;
    public final static int ERROR_DEVICE_NOT_FOUND = 1007;
    public final static int ERROR_CANNOT_RESUME = 1008;
    public final static int ERROR_FILE_ALREADY_EXISTS = 1009;
    public final static int ERROR_BLOCKED = 1010;
    public final static int PAUSED_WAITING_TO_RETRY = 1;
    public final static int PAUSED_WAITING_FOR_NETWORK = 2;
    public final static int PAUSED_QUEUED_FOR_WIFI = 3;
    public final static int PAUSED_UNKNOWN = 4;

    // Status
    public final static int STATUS_PENDING = 1 << 0;
    public final static int STATUS_RUNNING = 1 << 1;
    public final static int STATUS_PAUSED = 1 << 2;
    public final static int STATUS_SUCCESSFUL = 1 << 3;
    public final static int STATUS_FAILED = 1 << 4;

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
