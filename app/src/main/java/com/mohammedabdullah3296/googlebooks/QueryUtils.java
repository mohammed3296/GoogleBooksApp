package com.mohammedabdullah3296.googlebooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Abdullah on 9/10/2017.
 */

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Book> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Book} object
        List<Book> Books = extractFeatureFromJson(jsonResponse);

        // Return the {@link Book}
        return Books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Book} object by parsing out information
     * about the first Book from the input BookJSON string.
     */
    private static List<Book> extractFeatureFromJson(String BookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(BookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Books to
        List<Book> Books = new ArrayList<>();
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(BookJSON);
            int totalItems = baseJsonResponse.getInt("totalItems");
            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or Books).
            if (totalItems != 0) {
                JSONArray BookArray = null;
                if (baseJsonResponse.has("items")) {
                    BookArray = baseJsonResponse.getJSONArray("items");
                }
                // For each Book in the BookArray, create an {@link Book} object

                for (int i = 0; i < BookArray.length(); i++) {
                    // Get a single Book at position i within the list of Books
                    JSONObject currentBook = BookArray.getJSONObject(i);
                    // For a given Book, extract the JSONObject associated with the
                    // key called "properties", which represents a list of all properties
                    // for that Book.
                    String bookId = currentBook.getString("id");
                    String selfLink = currentBook.getString("selfLink");
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                    String title = "No Title Found";
                    if (volumeInfo.has("title")) {
                        title = volumeInfo.getString("title");
                    }


                    JSONArray authors = null;
                    String authorsString = null;
                    if (volumeInfo.has("authors")) {
                        authors = volumeInfo.getJSONArray("authors");
                        if (authors.length() > 0) {
                            for (int k = 0; k < authors.length(); k++) {
                                authorsString += authors.getString(k);
                            }
                        }
                    }

                    String publisher = "No Publisher Found";
                    if (volumeInfo.has("publisher")) {
                        publisher = volumeInfo.getString("publisher");
                    }

                    String publishedDate = "No publishedDate Found";
                    if (volumeInfo.has("publishedDate")) {
                        publishedDate = volumeInfo.getString("publishedDate");
                    }

                    String description = "No Description Found";
                    if (volumeInfo.has("description")) {
                        description = volumeInfo.getString("description");
                    }

                    int pageCount = 0;
                    if (volumeInfo.has("pageCount")) {
                        pageCount = volumeInfo.getInt("pageCount");
                    }

                    JSONObject imageLinks = null;
                    String smallThumbnail = "https://www.jainsusa.com/images/store/landscape/not-available.jpg";
                    if (volumeInfo.has("imageLinks")) {
                        Log.e(LOG_TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<imageLinks founds >>>>>>>>>>>>>");
                        imageLinks = volumeInfo.getJSONObject("imageLinks");
                        if (imageLinks.has("thumbnail")) {
                            smallThumbnail = imageLinks.getString("thumbnail");
                            Log.e(LOG_TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<smallThumbnail founds >>>>>>>>>>>>>");
                        }
                    }

                    String language = "No language found";
                    if (volumeInfo.has(language)) {
                        language = volumeInfo.getString("language");
                    }

                    String previewLink = "https://s3.amazonaws.com/ae-plugins/shared/images/default-thumb-300x300.png";
                    if (volumeInfo.has("previewLink")) {
                        previewLink = volumeInfo.getString("previewLink");
                        Log.e(LOG_TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<previewLink founds >>>>>>>>>>>>>");
                    }
                    String infoLink = "https://media.licdn.com/mpr/mpr/AAEAAQAAAAAAAAqBAAAAJDZlNGRkZDZhLWU1Y2YtNGRjMi04ODNkLWQ4ODQ5ZTQwZWRlYg.jpg";
                    if (volumeInfo.has("infoLink")) {
                        infoLink = volumeInfo.getString("infoLink");
                        Log.e(LOG_TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<infoLink founds >>>>>>>>>>>>>");
                    }

                    Book book = new Book(bookId, selfLink, title, authorsString, publisher, publishedDate, description, pageCount, smallThumbnail, language, previewLink, infoLink);
                    Log.i("OBGECT" + i, book.toString());
                    // Add the new {@link Book} to the list of Books.
                    Books.add(book);
                }

            } else {
                return null;
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Book JSON results", e);
        }

        // Return the list of Books
        return Books;
    }
}