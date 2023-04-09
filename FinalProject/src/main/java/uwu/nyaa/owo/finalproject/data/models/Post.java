package uwu.nyaa.owo.finalproject.data.models;

import java.util.LinkedList;
import java.util.List;

public class Post extends PostBase
{
    public List<HashInfoBase> files = new LinkedList<>();
    
    
    public Post()
    {
        super();
    }
    public Post(PostBase p)
    {
        this.post_id = p.post_id;
        this.title = p.title;
        this.description = p.description;
        this.created_at = p.created_at;
    }
    public Post(int post_id, String title, String description)
    {
        super(post_id, title, description);
    }
    
    public Post(String title, String description)
    {
        super(title, description);
    }
    
    public Post(int post_id)
    {
        super(post_id);
    }
}
