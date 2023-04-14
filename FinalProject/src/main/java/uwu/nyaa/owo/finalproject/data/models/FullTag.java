package uwu.nyaa.owo.finalproject.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FullTag
{
    public int tag_id;
    public int subtag_id;
    public int namespace_id;
    public String subtag;
    public String namespace;

    public String toString()
    {
        return String.format("<Tag: %d %s:%s>", tag_id, namespace, subtag);
    }
    
    @JsonIgnore
    public int getTagId()
    {
        return this.tag_id;
    }
}
