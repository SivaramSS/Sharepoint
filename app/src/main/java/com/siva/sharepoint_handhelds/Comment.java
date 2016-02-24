package com.siva.sharepoint_handhelds;

/**
 * Created by sivaram-pt862 on 15/02/16.
 */
public class Comment {

    String cid,content,userid,fname;
    String cdate;

    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCdate() {
        return cdate;
    }
    public void setCdate(String cdate) {
        this.cdate = cdate;
    }
}
