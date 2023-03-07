package uwu.nyaa.owo.finalproject.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

public class FileProcessor
{
    private static final MessageDigest SHA_DIGEST;

    static {
        try
        {
            SHA_DIGEST = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Cannot get sha256 instance", e);
        }
    }
    

    /**
     * gets the sha256 file hash of the given file
     * @param path the path to the file
     * @return the sha256 file hash or an empty array if there is an error
     */
    public static byte[] getSHA256(String path)
    {
        return getSHA256(new File(path));
    }
    
    /**
     * gets the sha256 file hash of the given file
     * @param path the path to the file
     * @return the sha256 file hash or an empty array if there is an error
     */
    public static byte[] getSHA256(File path)
    {
        if(!path.isFile())
            return new byte[0];
        
        try
        {
            return SHA_DIGEST.digest(Files.readAllBytes(path.toPath()));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return new byte[0];
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
