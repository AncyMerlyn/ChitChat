package com.usimedia.chitchat.model;

import java.util.Date;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContacts {
    String name;
    String status;
    Date lastSeen;

    public ChatContacts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
}
