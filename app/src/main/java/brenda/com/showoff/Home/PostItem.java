package brenda.com.showoff.Home;

public class PostItem {

    // Declare all the variables to store the game data.
    public String post_id;
    public String post_url;
    public String post_username;
    private String post_role;
    private String post_upvotes;
    public String post_comments;
    private String type;
    private String desc;
    private String timestamp;

    public PostItem(String post_id, String post_url, String post_username, String post_role, String post_upvotes, String post_comments) {
        this.post_id = post_id;
        this.post_url = post_url;
        this.post_username = post_username;
        this.post_role = post_role;
        this.post_upvotes = post_upvotes;
        this.post_comments = post_comments;
    }

    public PostItem(String imgurl) {

        this.post_url = imgurl;
    }

    public PostItem() {

    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getPost_username() {
        return post_username;
    }

    public void setPost_username(String post_username) {
        this.post_username = post_username;
    }

    public String getPost_role() {
        return post_role;
    }

    public void setPost_role(String post_role) {
        this.post_role = post_role;
    }

    public String getPost_upvotes() {
        return post_upvotes;
    }

    public void setPost_upvotes(String post_upvotes) {
        this.post_upvotes = post_upvotes;
    }

    public String getPost_comments() {
        return post_comments;
    }

    public void setPost_comments(String post_comments) {
        this.post_comments = post_comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
