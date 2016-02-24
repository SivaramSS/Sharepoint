package com.siva.sharepoint_handhelds;

/**
 * Created by sivaram-pt862 on 12/02/16.
 */
public class Post {

    String userid, fname, lname, url, title, content, aid;
    int count_likes, count_comments;
    String uldatetime;
    int liked;
    String countlikestext;
    String countcommentstext;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount_comments() {
        return count_comments;
    }

    public void setCount_comments(int count_comments) {
        this.count_comments = count_comments;
    }

    public int getCount_likes() {
        return count_likes;
    }

    public void setCount_likes(int count_likes) {
        this.count_likes = count_likes;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int isLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUldatetime() {
        return uldatetime;
    }

    public void setUldatetime(String uldatetime) {
        this.uldatetime = uldatetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCountlikestext() {
        return countlikestext;
    }

    public void setCountlikestext(String countlikestext) {
        this.countlikestext = countlikestext;
    }

    public String getCountcommentstext() {
        return countcommentstext;
    }

    public void setCountcommentstext(String countcommentstext) {
        this.countcommentstext = countcommentstext;
    }
}
