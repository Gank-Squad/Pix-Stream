package uwu.nyaa.owo.finalproject.data.models;

import java.sql.Timestamp;

public class BarePost
{
    public int post_id;
    public String title;
    public String description;
    public Timestamp posted = new Timestamp(System.currentTimeMillis());;
    
    public BarePost()
    {
        this.post_id = -1;
        this.title = "";
        this.description = "";
    }
    
    public BarePost(int posts_id)
    {
        this();
        this.post_id = posts_id;
    }
    
    public BarePost(int posts_id, String title, String description)
    {
        this.title = title;
        this.description = description;
        this.post_id = posts_id;
    }
    
    public BarePost(String title, String description)
    {
        this.title = title;
        this.description = description;
        this.post_id = -1;
    }
}
