package uwu.nyaa.owo.finalproject.data.models;

import com.fasterxml.jackson.annotation.JsonGetter;

public class FullTag
{
    public int tag_id;
    public int subtag_id;
    public int namespace_id;
    public String subtag;
    public String namespace;

    @JsonGetter
    public String getTag()
    {
        return namespace + ":" + subtag;
    }

    public String toString()
    {
        return String.format("<Tag: %d %s:%s>", tag_id, namespace, subtag);
    }
}
