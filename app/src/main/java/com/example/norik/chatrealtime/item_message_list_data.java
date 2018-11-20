package com.example.norik.chatrealtime;

public class item_message_list_data {
    String message;
    String name;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public item_message_list_data() {
        this.message = null;
        this.name = null;
    }
}
