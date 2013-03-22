package fr.free.gelmir.lerubanbleu.service;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import fr.free.gelmir.lerubanbleu.provider.ArticleDatabase;
import fr.free.gelmir.lerubanbleu.provider.ArticleProvider;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 16/03/13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class ArticleProcessor implements LoaderManager.LoaderCallbacks<Cursor>
{


    public ArticleProcessor() {



}


    public void queryArticle(ArticleProcessorCallback articleProcessorCallback) {

        // Allocate cursor loader
        String[] projection = { ArticleDatabase.COLUMN_ID };
        CursorLoader cursorLoader = new CursorLoader(this, ArticleProvider.CONTENT_URI, projection, null, null, null);

    }


    public void updateArticle() {



    }


    // Loader
    //-------

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        // Takes action based on the ID of the Loader that's being created
            switch (i) {
                case URL_LOADER:
                    // Returns a new CursorLoader
                    return new CursorLoader(
                            getActivity(),   // Parent activity context
                            mDataUrl,        // Table to query
                            mProjection,     // Projection to return
                            null,            // No selection clause
                            null,            // No selection arguments
                            null             // Default sort order
                    );
                default:
                    // An invalid id was passed in
                    return null;
            }
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

