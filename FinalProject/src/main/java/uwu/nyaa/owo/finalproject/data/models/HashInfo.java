package uwu.nyaa.owo.finalproject.data.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;

public class HashInfo extends HashInfoBase
{
    @JsonProperty("mime_int")
    public int mime;

    @JsonProperty("file_size")
    public long fileSize;

    @JsonProperty("width")
    public int width;

    @JsonProperty("height")
    public int height;

    @JsonProperty("duration")
    public int duration;

    @JsonProperty("has_audio")
    public boolean has_audio;

    public List<FullTag> tags;

    @JsonGetter
    @JsonProperty("mime")
    public String getMime()
    {
        return FileFormat.getMimeType((byte) this.mime);
    }

    public String toString()
    {
        return String.format("<HashInfo id: %d hash: %s>", this.hash_id, this.getStringHash());
    }
}