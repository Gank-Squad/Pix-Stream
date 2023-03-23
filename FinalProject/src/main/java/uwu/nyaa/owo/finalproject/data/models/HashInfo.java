package uwu.nyaa.owo.finalproject.data.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;

import java.util.List;


public class HashInfo
{
    @JsonProperty("hash_id")
    public int hash_id;
    
    @JsonIgnore
    public byte[] hash;
    
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
    private String hash_string = null;
    
    @JsonGetter
    @JsonProperty("mime")
    public String getMime()
    {
        return FileFormat.getMimeType((byte)this.mime);
    }
    
    @JsonGetter
    @JsonProperty("sha256")
    public String getStringHash()
    {
        if(this.hash == null)
            throw new NullPointerException("Cannot get the string hash because the byte[] hash is null");
        
        if(this.hash_string == null)
            this.hash_string = ByteHelper.bytesToHex(this.hash);
        
        return this.hash_string;
    }
    
    public String toString()
    {
        return String.format("<HashInfo id: %d hash: %s>", this.hash_id, this.getStringHash());
    }
}