package com.myspringway.secretmemory.model;

/**
 * Created by legab on 2016-06-21.
 */

public class Comment {
    public String uid;
    public String com_author;
    public String com_text;

    public Comment() {
        // Default constructor required for to DetaSnaphot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text) {
        this.uid = uid;
        this.com_author = author;
        this.com_text = text;
    }
}
