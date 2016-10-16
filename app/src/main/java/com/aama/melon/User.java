package com.aama.melon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by mike on 10/16/16.
 */

public class User {

    User()
    {
        
    }

    User(String f, String t)
    {
        fb = f;
        tw = t;
    }

    private String fb;
    private String tw;

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public String toJSON()
    {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public static User fromJSON(String s)
    {
        Gson gson = new Gson();
        return gson.fromJson(s,User.class);
    }
}
