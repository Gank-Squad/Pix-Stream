package uwu.nyaa.owo.finalproject.data.models;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;

public class FileUpload
{
    @JsonIgnore
    public FileProcessor.Hashes hashes;

    @JsonIgnore
    public File filePath;

    @JsonProperty("hash_id")
    public int hash_id;

    @JsonProperty("upload_accepted")
    public boolean accepted;

    @JsonProperty("hash")
    @JsonGetter
    public String getHash()
    {
        if (this.hashes != null)
        {
            return ByteHelper.bytesToHex(this.hashes.SHA256);
        }
        return "";
    }
}
