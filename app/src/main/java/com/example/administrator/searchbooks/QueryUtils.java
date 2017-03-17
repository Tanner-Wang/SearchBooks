package com.example.administrator.searchbooks;

import android.text.TextUtils;

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


public class QueryUtils {

    //设置URL，并设置没有URL的Exception方法---------
    public static URL createUrl(String StringUrl) {
        URL url = null;
        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return url;
    }

    //请求HTTP-----
    public static String makeHttpRequest(URL url) throws IOException {
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

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    public static ArrayList<Book> extractFeatureFromJson(String bookJson) {
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String description = volumeInfo.getString("description");
                String authors = volumeInfo.getString("authors");
                String url = volumeInfo.getString("previewLink");
                books.add(new Book(title, description, authors, url));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
