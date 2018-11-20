package com.example.norik.chatrealtime;

import android.media.Image;
import android.widget.ImageView;

public class RowItemsUserOnlineData {
    private String username;
    private int status;




    public RowItemsUserOnlineData(String username, int status) {
        this.username = username;
        this.status = status;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
