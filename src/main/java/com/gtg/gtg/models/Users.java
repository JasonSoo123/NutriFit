package com.gtg.gtg.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="Users")

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String username;

    private String name;

    private String email;

    private String password;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    public Users() {
    }

    public Users(String username, String name, String email, String password, Date birthday) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
