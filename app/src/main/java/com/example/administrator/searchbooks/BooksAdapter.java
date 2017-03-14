package com.example.administrator.searchbooks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/12.
 */

public class BooksAdapter extends ArrayAdapter<Book> {

    public BooksAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(currentBook.getBookName());

        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(currentBook.getDescription());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        return convertView;

    }
}

