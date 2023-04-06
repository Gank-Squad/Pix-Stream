package uwu.nyaa.owo.finalproject.data.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import uwu.nyaa.owo.finalproject.data.ByteHelper;

public class HashInfoBase
{
    @JsonProperty("hash_id")
    public int hash_id;

    @JsonIgnore
    public byte[] hash;

    private String hash_string = null;

    public HashInfoBase()
    {
        hash_id = -1;
    }

    public HashInfoBase(FileUpload fa)
    {
        hash_id = fa.hash_id;
        hash = fa.hashes.SHA256;
    }

    @JsonGetter
    @JsonProperty("hash")
    public String getStringHash()
    {
        if (this.hash == null)
            throw new NullPointerException("Cannot get the string hash because the byte[] hash is null");

        if (this.hash_string == null)
            this.hash_string = ByteHelper.bytesToHex(this.hash);

        return this.hash_string;
    }
}
