package com.example.administrator.searchbooks;

/**
 * Created by Administrator on 2017/3/12.
 */

public class Book {

    private String mBookName;
    private String mDescription;
    private String mAuthor;
    private String mUrl;

    public Book(String bookName, String description, String author, String url) {

        mBookName = bookName;
        mDescription = description;
        mAuthor = author;
        mUrl = url;
    }

    public String getBookName() {
        return mBookName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getmUrl() {
        return mUrl;
    }
}
