package com.myspringway.secretmemory.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by legab on 2016-06-21.
 */
@IgnoreExtraProperties
public class Member {

    public String mem_name;
    public String mem_email;
    public long mem_j_at; // joined at

    public Member() {
        // Default constructor required for calls to DataSnapshot.getValue(Member.class)
    }

    public Member(String name, String email) {
        this.mem_email = mem_email;
    }
}
