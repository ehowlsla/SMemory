package com.myspringway.secretmemory.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by yoontaesup on 2016. 6. 4..
 */

public class Post {

    private static final Random RANDOM = new Random();

    public long pos_c_at; // created at
    public long pos_u_at; // updated at
    public String author;
    public String pos_imgData;
    public String pos_content;
    public String pos_key;
    public List<String> pos_tagList;
    public int numOfComment = 0;
    public int numOfLike = 0;       // TODO: Like List 형태로 변경
    public Map<String, Boolean> likes = new HashMap<>();


    public Post() {
        // Default constructor required for calls to DataSanpshot.getValue(Post.class)
    }

    public Post(String key, String author, String body) {
        this.pos_c_at = System.currentTimeMillis();
        this.pos_u_at = System.currentTimeMillis();
        this.author = author;
        this.pos_imgData = tempImgUrls[RANDOM.nextInt() % (tempImgUrls.length - 1)];
        this.pos_content = body;
        this.pos_key = key;
    }

    public Post(String key, String author, String imgUrl, String body) {
        this.pos_c_at = System.currentTimeMillis();
        this.pos_u_at = System.currentTimeMillis();
        this.author = author;
        this.pos_imgData = imgUrl;
        this.pos_content = body;
        this.pos_key = key;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pos_c_at", pos_c_at);
        result.put("pos_u_at", pos_u_at);
        result.put("author", author);
        result.put("pos_imgData", pos_imgData);
        result.put("pos_content", pos_content);
        result.put("pos_tagList", pos_tagList);
        result.put("numOfComment", numOfComment);
        result.put("pos_key", pos_key);
        result.put("numOfLike", numOfLike);
        return result;
    }

    public static final String[] tempImgUrls = {

    };

}
