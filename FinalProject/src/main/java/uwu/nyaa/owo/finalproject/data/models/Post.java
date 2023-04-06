package uwu.nyaa.owo.finalproject.data.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import uwu.nyaa.owo.finalproject.data.ByteHelper;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class Post
{
    @JsonIgnore
    public BarePost barePost;

    public List<BareHashInfo> files = new LinkedList<>();
    
    
    public Post()
    {

    }
    
    public Post(int post_id)
    {
        this.barePost = new BarePost(post_id);
    }
    
    public Post(int post_id, String title, String description)
    {
        this.barePost = new BarePost(post_id, title, description);
    }
    
    public Post(String title, String description)
    {
        this.barePost = new BarePost(title, description);
    }
    
    public Post(BarePost ba)
    {
        this.barePost = ba;
    }
    
    public Post(FileUpload fa)
    {
        this.files.add(new BareHashInfo(fa));
    }


    @JsonGetter
    @JsonProperty("post_id")
    public int getPostId()
    {
        return barePost.post_id;
    }

    @JsonGetter
    @JsonProperty("title")
    public String getTitle()
    {
        return barePost.title;
    }

    @JsonGetter
    @JsonProperty("description")
    public String getDescription()
    {
        return barePost.description;
    }

    @JsonGetter
    @JsonProperty("date_created")
    public long getPostedTime()
    {
        return barePost.posted.getTime();
    }
}
