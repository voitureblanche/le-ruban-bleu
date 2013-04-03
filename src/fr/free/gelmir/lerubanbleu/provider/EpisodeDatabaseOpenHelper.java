package fr.free.gelmir.lerubanbleu.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 18/03/13
 * Time: 00:52
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeDatabaseOpenHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "lerubanbleu.db";
    private static final int    DATABASE_VERSION = 1;

    public EpisodeDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        EpisodeTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        EpisodeTable.onUpgrade(database, oldVersion, newVersion);
    }
}
