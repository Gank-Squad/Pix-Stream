package uwu.nyaa.owo.finalproject.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

public class FileProcessor
{
    private static final MessageDigest SHA256_DIGEST;
    private static final MessageDigest SHA1_DIGEST;
    private static final MessageDigest MD5_DIGEST;

    static {
        try
        {
            SHA256_DIGEST = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Cannot get sha256 instance", e);
        }
        
        try
        {
            SHA1_DIGEST = MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Cannot get sha1 instance", e);
        }
        
        try
        {
            MD5_DIGEST = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Cannot get MD5 instance", e);
        }
    }
    
    /**
     * Computes the file hash of the given file with the given hash algorithm
     * @param hashAlgorithm The hash algorithm
     * @param path The path to the file
     * @return The hash of the file or a byte[] with length 0
     */
    public static byte[] hashFile(MessageDigest hashAlgorithm, String path)
    {
        return hashFile(hashAlgorithm, new File(path));
    }
    
    /**
     * Computes the file hash of the given file with the given hash algorithm
     * @param hashAlgorithm The hash algorithm
     * @param path The path to the file
     * @return The hash of the file or a byte[] with length 0
     */
    public static byte[] hashFile(MessageDigest hashAlgorithm, File path)
    {
        if(!path.isFile())
            return new byte[0];
        
        try
        {
            return hashAlgorithm.digest(Files.readAllBytes(path.toPath()));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return new byte[0];
    }
    
    
    /**
     * Computes the SHA256 file hash of the given file
     * @param path the path to the file
     * @return the SHA256 file hash or a byte[] with length 0
     */
    public static byte[] getSHA256(String path)
    {
        return hashFile(SHA256_DIGEST, path);
    }
    
    /**
     * Computes the SHA256 file hash of the given file
     * @param path the path to the file
     * @return the SHA256 file hash or a byte[] with length 0
     */
    public static byte[] getSHA256(File path)
    {
        return hashFile(SHA256_DIGEST, path);
    }
    
    /**
     * Computes the SHA1 file hash of the given file
     * @param path the path to the file
     * @return the SHA1 file hash or a byte[] with length 0
     */
    public static byte[] getSHA1(String path)
    {
        return hashFile(SHA1_DIGEST, path);
    }
    
    /**
     * Computes the SHA1 file hash of the given file
     * @param path the path to the file
     * @return the SHA1 file hash or a byte[] with length 0
     */
    public static byte[] getSHA1(File path)
    {
        return hashFile(SHA1_DIGEST, path);
    }
    
    /**
     * Computes the MD5 file hash of the given file
     * @param path the path to the file
     * @return the MD5 file hash or a byte[] with length 0
     */
    public static byte[] getMD5(String path)
    {
        return hashFile(MD5_DIGEST, path);
    }
    
    /**
     * Computes the MD5 file hash of the given file
     * @param path the path to the file
     * @return the MD5 file hash or a byte[] with length 0
     */
    public static byte[] getMD5(File path)
    {
        return hashFile(MD5_DIGEST, path);
    }
    
    
    
    public static boolean addFile(String path)
    {
        File f = new File(path);
        
        if(!f.isFile()) 
        {
            WrappedLogger.info(String.format("Could not add file: %s, because it does not exist", f));
            return false;
        }
            
        
        byte[] b = FileProcessor.getSHA256(f);
        
        if(b.length == 0)
        {
            WrappedLogger.info(String.format("Could not add file: %s, because the sha256 hash was empty", f));
            return false;
        }
        
        String fileHash = ByteHelper.bytesToHex(b);
        String mediaPath = PathHelper.getMediaPath(fileHash);
        
        // TODO: finish this function
        throw new RuntimeException("Unimplemented method addFile, there's a todo you need to finish the code!");
        
//        return false;
    }
}
