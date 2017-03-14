package com.example.administrator.searchbooks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";

//    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText enterText = (EditText) findViewById(R.id.enter);
        TextView searchText = (TextView) findViewById(R.id.search_button);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterBookName = enterText.getText().toString();
                bookUrl += enterBookName;
                BookAsynscTask task = new BookAsynscTask();
                task.execute(bookUrl);
                bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
            }
        });


    }

    //设置URL，并设置没有URL的Exception方法---------
    private URL createUrl(String StringUrl) {
        URL url = null;
        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
            e.printStackTrace();
        }
        return url;
    }

    //请求HTTP-----
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(8000);
            urlConnection.setConnectTimeout(12000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
            e.printStackTrace();

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

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<Book> extractFeatureFromJson(String bookJson) {
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();
        try {


            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                String title = book.getString("title");
                String description = book.getString("description");
                String authors = book.getString("authors");
                String url = book.getString("previewLink");
                books.add(new Book(title, description, authors, url));

            }

        } catch (JSONException e) {
            bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
            e.printStackTrace();
        }
        return books;
    }

    private class BookAsynscTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            URL url = createUrl(urls[0]);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String eJsonResponse) {
            if (eJsonResponse == null) {
                return;
            }

            ArrayList<Book> books = extractFeatureFromJson(eJsonResponse);
            ListView bookListView = (ListView) findViewById(R.id.list);

            // Create a new {@link ArrayAdapter} of earthquakes
            final BooksAdapter adapter = new BooksAdapter(
                    MainActivity.this, books);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            bookListView.setAdapter(adapter);

            bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
