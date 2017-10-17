package com.mohammedabdullah3296.googlebooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private View loadingIndicator;
    private TextView mEmptyStateTextView;
    private static final int Book_LOADER_ID = 0;
    private BooksAdapter mAdapter;
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URLTirmFirst = "https://www.googleapis.com/books/v1/volumes?q=";
    private static String USGS_REQUEST_URL = "";

    private SearchView searchView;
    private static String SEARCH_QUERY = "keyword";
    private static String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        searchView = (SearchView) findViewById(R.id.search_viewr);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                method(searchView.getQuery().toString());
                //  method2(searchView.getQuery().toString());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                key = searchView.getQuery().toString();
                return true;
            }
        });


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SEARCH_QUERY)) {
                String key = savedInstanceState
                        .getString(SEARCH_QUERY);
                searchView.setQuery(key, false);

            }
        }

        // Find a reference to the {@link ListView} in the layout
        GridView BookGridView = (GridView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of Books as input
        mAdapter = new BooksAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        Log.w(LOG_TAG, "setAdapter");
        BookGridView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        BookGridView.setEmptyView(mEmptyStateTextView);


        /*
        // Start the AsyncTask to fetch the Book data
        BookAsyncTask task = new BookAsyncTask();
        task.execute(USGS_REQUEST_URL);

        */
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected Book.
        BookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current Book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri BookUri = Uri.parse(currentBook.getPreviewLink());

                // Create a new intent to view the Book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, BookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        getLoaderManager().initLoader(Book_LOADER_ID, null, this);
    }

    private void method(String hhh) {
        USGS_REQUEST_URL = USGS_REQUEST_URLTirmFirst + hhh.replace(" ", "%20");
        loadingIndicator.setVisibility(View.VISIBLE);
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            Log.w(LOG_TAG, "getLoaderManager");
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

//            Log.w(LOG_TAG, "initLoader");
//            loaderManager.initLoader(Book_LOADER_ID, null, this);

            //  LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> githubSearchLoader = loaderManager.getLoader(Book_LOADER_ID);
            if (githubSearchLoader == null) {
                loaderManager.initLoader(Book_LOADER_ID, null, this);
            } else {
                loaderManager.restartLoader(Book_LOADER_ID, null, this);
            }

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(Book_LOADER_ID, null, this);


        searchView.setQuery(key, false);
    }


    private void method2(String hhh) {
        USGS_REQUEST_URL = USGS_REQUEST_URLTirmFirst + hhh;
        loadingIndicator.setVisibility(View.VISIBLE);
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            Log.w(LOG_TAG, "getLoaderManager");


            // Start the AsyncTask to fetch the earthquake data
            BookAsyncTask task = new BookAsyncTask();
            task.execute(USGS_REQUEST_URL);


        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.w(LOG_TAG, "onCreateLoader");
        return new BookLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        loadingIndicator.setVisibility(View.GONE);
        Log.w(LOG_TAG, "onLoadFinished");
        mEmptyStateTextView.setText(R.string.no_books);
        // Clear the adapter of previous Book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.w(LOG_TAG, "onLoaderReset");
// Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link Book}s as the result.
         */
        @Override
        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Create a new loader for the given URL
            Log.w(LOG_TAG, "doInBackground");
            List<Book> result = QueryUtils.fetchBookData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of Book data from a previous
         * query to USGS. Then we update the adapter with the new list of Books,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Book> books) {
            loadingIndicator.setVisibility(View.GONE);
            Log.w(LOG_TAG, "onLoadFinished");
            mEmptyStateTextView.setText(R.string.no_books);
            // Clear the adapter of previous Book data
            mAdapter.clear();

            // If there is a valid list of {@link Book}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // COMPLETED (4) Make sure super.onSaveInstanceState is called before doing anything else
        super.onSaveInstanceState(outState);

        // COMPLETED (5) Put the contents of the TextView that contains our URL into a variable
        String queryUrl = searchView.getQuery().toString();


        // COMPLETED (6) Using the key for the query URL, put the string in the outState Bundle
        outState.putString(SEARCH_QUERY, queryUrl);
    }


}
