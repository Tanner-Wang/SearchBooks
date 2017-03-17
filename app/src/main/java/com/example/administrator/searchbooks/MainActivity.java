package com.example.administrator.searchbooks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.administrator.searchbooks.QueryUtils.createUrl;
import static com.example.administrator.searchbooks.QueryUtils.extractFeatureFromJson;
import static com.example.administrator.searchbooks.QueryUtils.makeHttpRequest;

public class MainActivity extends AppCompatActivity {

    public static String bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    static ViewHolder holder = new ViewHolder();
    BooksAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        holder.enterText = (EditText) findViewById(R.id.enter);
        holder.searchText = (TextView) findViewById(R.id.search_button);
        holder.searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
                String enterBookName = holder.enterText.getText().toString();
                bookUrl += enterBookName;
                startTask(bookUrl);
            }
        });

    }

    private void startTask(String bookUrl) {
        BookAsyncTask task = new BookAsyncTask();
        task.execute(bookUrl);
    }

    static class ViewHolder {
        EditText enterText;
        TextView searchText;
        TextView title;
        TextView description;
        TextView author;
        ListView bookListView;

    }

    private class BookAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url = createUrl(urls[0]);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {

            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String eJsonResponse) {
            if (eJsonResponse == null) {
                return;
            }

            holder.bookListView = (ListView) findViewById(R.id.list);
            ArrayList<Book> books = extractFeatureFromJson(eJsonResponse);
            adapter = new BooksAdapter(MainActivity.this, books);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            holder.bookListView.setAdapter(adapter);

            holder.bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Book currentBook = adapter.getItem(position);
                    Uri bookUri = Uri.parse(currentBook.getmUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                    startActivity(websiteIntent);
                }
            });

        }

    }


}
