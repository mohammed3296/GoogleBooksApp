package com.mohammedabdullah3296.googlebooks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 9/10/2017.
 */

public class BooksAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BooksAdapter.class.getSimpleName();
    private  SquaredImageView imageView;
    public BooksAdapter(Activity context, List<Book> Books) {
        super(context, 0, Books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle());
        TextView authorView = (TextView) listItemView.findViewById(R.id.authors);
        authorView.setText(currentBook.getAuthors());
        final SquaredImageView imageView;
        //imageView = (SquaredImageView) listItemView.findViewById(R.id.feedImage1);
        ImageView bookImage =  (ImageView) listItemView.findViewById(R.id.thumbnail);
        Context context = getContext() ;
//        Picasso.with(dd).load(currentBook.getSmallThumbnailImage()).into(settingsDisplayImage);
        //Picasso.with(context).load(currentBook.getSmallThumbnailImage()).resize(50, 50).centerCrop().into(bookImage);
        Picasso.with(context).load(currentBook.getSmallThumbnailImage()).into(bookImage);
        return listItemView;
    }
    public SquaredImageView getImageView() {
        return imageView;
    }
}
