package com.mohammedabdullah3296.googlebooks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 9/10/2017.
 */

 
public class BookLoader extends AsyncTaskLoader<List<Book>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BookLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.w(LOG_TAG, "BookLoader consrructor");
    }

    @Override
    protected void onStartLoading() {
        Log.w(LOG_TAG, "onStartLoading forceLoad");
        forceLoad();

    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Book> loadInBackground() {
        Log.w(LOG_TAG, "loadInBackground loadInBackground");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Books.
        List<Book> Books = QueryUtils.fetchBookData(mUrl);
        return Books;
    }
}