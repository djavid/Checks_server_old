package com.djavid.checkserver.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegistrationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    private long created;
    private long lastVisited;


    public RegistrationToken() { }

    public RegistrationToken(String token) {
        this.id = 0;
        this.token = token;
        created = System.currentTimeMillis();
        lastVisited = created;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public long getCreated() {
        return created;
    }
    public void setCreated(long created) {
        this.created = created;
        this.lastVisited = created;
    }

    public long getLastVisited() {
        return lastVisited;
    }
    public void setLastVisited(long lastVisited) {
        this.lastVisited = lastVisited;
    }
}
