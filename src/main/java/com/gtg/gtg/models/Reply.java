package com.gtg.gtg.models;

import jakarta.persistence.*;

@Entity
@Table(name="Reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String username;
    private String content;
    private String postTitle;
    private String postUsername;

    public Reply() {
    }
    
    public Reply(int uid, String username, String content, String postTitle, String postUsername) {
        this.uid = uid;
        this.username = username;
        this.content = content;
        this.postTitle = postTitle;
        this.postUsername = postUsername;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    

    
}
