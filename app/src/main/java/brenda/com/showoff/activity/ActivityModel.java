package brenda.com.showoff.activity;

public class ActivityModel {

    public String fname;
    public String lname;
    public String imgurl;
    public String upvote;
    public String created;
    public String userId;
    public String postID;

    public ActivityModel(String fname, String lname, String imgurl, String upvote, String created) {
        this.fname = fname;
        this.lname = lname;
        this.imgurl = imgurl;
        this.upvote = upvote;
        this.created = created;
    }

    public ActivityModel() {

    }


    public String getFname() {
        return fname;
    }

    public String getCreated() {
        return created;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getLname() {
        return lname;
    }

    public String getUpvote() {
        return upvote;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setUpvote(String upvote) {
        this.upvote = upvote;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getPostID() { return postID; }

    public void setPostID(String postID) { this.postID = postID; }
}
