package com.example.administrator.searchbooks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.administrator.searchbooks.MainActivity.holder;

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

        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.title.setText(currentBook.getBookName());

        holder.description = (TextView) convertView.findViewById(R.id.description);
        holder.description.setText(currentBook.getDescription());

        holder.author = (TextView) convertView.findViewById(R.id.author);
        holder.author.setText(currentBook.getAuthor());

        return convertView;

    }
}

