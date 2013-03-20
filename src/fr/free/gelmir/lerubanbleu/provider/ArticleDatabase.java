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
public class ArticleDatabase {

    // Table
    public static final String TABLE_NAME = "lerubanbleu";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";

    // ArticleDatabase creation SQL statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_IMAGE + " text not null, "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        Log.w(ArticleDatabase.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
