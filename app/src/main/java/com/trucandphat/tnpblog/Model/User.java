package com.trucandphat.tnpblog.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {
    private String uid;
    private String username;
    private String email;
    private Date dateOfBirth;
    private Date dateCreated;
    private int blogNumber;

    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String uid, String username,String email, Date dateOfBirth, Date dateCreated,int blogNumber) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.dateCreated = dateCreated;
        this.blogNumber = blogNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
