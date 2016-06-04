package com.usimedia.chitchat.model;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContacts implements Serializable {
    String name;
    String status;
    Date lastSeen;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ChatContacts{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", lastSeen=" + lastSeen +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatContacts that = (ChatContacts) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(status, that.status) &&
                Objects.equal(lastSeen, that.lastSeen) &&
                Objects.equal(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, status, lastSeen, email);
    }
}
