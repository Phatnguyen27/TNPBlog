package com.trucandphat.tnpblog.Model;

import java.io.Serializable;
import java.util.Date;


public class Blog implements Serializable {
    private String id;
    private String content;
    private Date dateCreated;
    private int type;
    private String title;
    private String authorId;
    private String authorName;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    private int like;
    private int dislike;

    public static enum BlogType {
        Education("Education", 0),
        Confession("Confession", 1),
        Entertainment("Entertainment",2);

        private String stringValue;
        private int intValue;
        private BlogType(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int toInt() {
            return intValue;
        }
    }

    public Blog() {}

    public Blog(String id,String title,String content,String authorId,String authorName,int type,Date dateCreated,int likeAmount,int dislikeAmount) {
        this.id =id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.type = type;
        this.dateCreated = dateCreated;
        this.like = likeAmount;
        this.dislike = dislikeAmount;
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }
}
