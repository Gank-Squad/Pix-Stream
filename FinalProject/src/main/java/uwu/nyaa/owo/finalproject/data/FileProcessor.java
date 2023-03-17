package uwu.nyaa.owo.finalproject.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uwu.nyaa.owo.finalproject.data.ImageProcessor.ImageInfo;
import uwu.nyaa.owo.finalproject.data.VideoProcessor.VideoInfo;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.db.TableHash;
import uwu.nyaa.owo.finalproject.data.db.TableLocalHash;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

public class FileProcessor
{
    /**
     * This is required because MessageDigest is not threadsafe and they all share the same instance
     * <br>
     * This class clones the instances, then resets them to empty and returns a cloned copy whenever you want an instance
     * @author minno
     *
     */
    public static class ImmutableMessageDigest
    {
        private static final MessageDigest SHA256_DIGEST;
        private static final MessageDigest SHA1_DIGEST;
        private static final MessageDigest MD5_DIGEST;

        static {
            try
            {
                SHA256_DIGEST = cloneMessageDigest(MessageDigest.getInstance("SHA-256"));
                SHA256_DIGEST.reset();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("Cannot get sha256 instance", e);
            }
            
            try
            {
                SHA1_DIGEST = cloneMessageDigest(MessageDigest.getInstance("SHA-1"));
                SHA1_DIGEST.reset();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("Cannot get sha1 instance", e);
            }
            
            try
            {
                MD5_DIGEST = cloneMessageDigest(MessageDigest.getInstance("MD5"));
                MD5_DIGEST.reset();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("Cannot get MD5 instance", e);
            }
        }
        
        private static MessageDigest cloneMessageDigest(MessageDigest original) 
        {
            MessageDigest clone = null;
            try 
            {
                clone = (MessageDigest) original.clone();
            } 
            catch (CloneNotSupportedException cnse) 
            {
                throw new RuntimeException("Failed to instantiate a new MessageDigest instance.",cnse );
            } 
            
            return clone;
        }
        
        public static MessageDigest getSHA256()
        {
            return cloneMessageDigest(SHA256_DIGEST);
        }
        
        public static MessageDigest getSHA1()
        {
            return cloneMessageDigest(SHA1_DIGEST);
        }
        
        public static MessageDigest getMD5()
        {
            return cloneMessageDigest(MD5_DIGEST);
        }
    }
    
    
    
    public static class Hashes 
    {
        public byte[] SHA256;
        public byte[] SHA1  ;
        public byte[] MD5   ;
        public byte[] PHASH ;
        
        public String toString()
        {
            return String.format("<Hashes SHA256: %s SHA1: %s MD5: %s>", 
                    ByteHelper.bytesToHex(SHA256),
                    ByteHelper.bytesToHex(SHA1),
                    ByteHelper.bytesToHex(MD5));
        }
    }
    
    
    /**
     * Gets the SHA256, SHA1 and MD5 file hashes for the given file
     * @param path The path to the file
     * @return The SHA256, SHA1 and MD5 hashes or null
     */
    public static Hashes getFileHashes(String path)
    {
        return getFileHashes(new File(path));
    }
    
    /**
     * Gets the SHA256, SHA1 and MD5 file hashes for the given file
     * @param path The path to the file
     * @return The SHA256, SHA1 and MD5 hashes or null
     */
    public static Hashes getFileHashes(File path)
    {
        if(!path.isFile())
            return null;
        
        // 1 mb
        final int BUFFER_SIZE = 1024 * 1024;
        
        try(FileInputStream fis = new FileInputStream(path);
                BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE))
        {
            MessageDigest sha256 = ImmutableMessageDigest.getSHA256();
            MessageDigest sha1 = ImmutableMessageDigest.getSHA1();
            MessageDigest md5 = ImmutableMessageDigest.getMD5();
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = 0;
            
            while((read = bis.read(buffer, 0, buffer.length)) != -1)
            {
                sha256.update(buffer, 0, read);
                sha1.update(buffer, 0, read);
                md5.update(buffer, 0, read);
            }
            
            Hashes a = new Hashes();
            a.SHA1 = sha1.digest();
            a.SHA256 = sha256.digest();
            a.MD5 = md5.digest();
            
            return a;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
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
        return hashFile(ImmutableMessageDigest.getSHA256(), path);
    }
    
    /**
     * Computes the SHA256 file hash of the given file
     * @param path the path to the file
     * @return the SHA256 file hash or a byte[] with length 0
     */
    public static byte[] getSHA256(File path)
    {
        return hashFile(ImmutableMessageDigest.getSHA256(), path);
    }
    
    /**
     * Computes the SHA1 file hash of the given file
     * @param path the path to the file
     * @return the SHA1 file hash or a byte[] with length 0
     */
    public static byte[] getSHA1(String path)
    {
        return hashFile(ImmutableMessageDigest.getSHA1(), path);
    }
    
    /**
     * Computes the SHA1 file hash of the given file
     * @param path the path to the file
     * @return the SHA1 file hash or a byte[] with length 0
     */
    public static byte[] getSHA1(File path)
    {
        return hashFile(ImmutableMessageDigest.getSHA1(), path);
    }
    
    /**
     * Computes the MD5 file hash of the given file
     * @param path the path to the file
     * @return the MD5 file hash or a byte[] with length 0
     */
    public static byte[] getMD5(String path)
    {
        return hashFile(ImmutableMessageDigest.getMD5(), path);
    }
    
    /**
     * Computes the MD5 file hash of the given file
     * @param path the path to the file
     * @return the MD5 file hash or a byte[] with length 0
     */
    public static byte[] getMD5(File path)
    {
        return hashFile(ImmutableMessageDigest.getMD5(), path);
    }

    
    public static boolean addFile(String path)
    {
        return addFile(new File(path));
    }
    
    public static boolean addFile(File f)
    {
        if(!f.isFile()) 
        {
            WrappedLogger.info(String.format("Could not add file: %s, because it does not exist or is not a file", f));
            return false;
        }
            
        
        Hashes b = getFileHashes(f);
        
        if(b == null || b.SHA256.length == 0)
        {
            WrappedLogger.info(String.format("Could not add file: %s, because the SHA256 hash was empty", f));
            return false;
        }
        
        String fileHash = ByteHelper.bytesToHex(b.SHA256);
        String mediaPath = PathHelper.getMediaPath(fileHash);
        String thumbPath = PathHelper.getThmbnailPath(fileHash);
        File mediaFile = new File(mediaPath);
        File thumbFile = new File(thumbPath);
        File tmpMediaFile = new File(mediaFile.getAbsoluteFile() + ".tmp");
        
        if(mediaFile.exists())
        {
            WrappedLogger.warning(String.format("Ignoring adding file %s because it media file already exist; Assuming it's in the db", f));
            return false;
        }
        
        long fileSize = f.length();
        byte mimeType = FileDetector.getFileMimeType(f);
        int width = 0;
        int height = 0;
        int duration = 0;
        boolean has_audio = false;
        
        
        if(FileFormat.isImageType(mimeType))
        {
            ImageInfo i = ImageProcessor.getImageInfo(f);
            
            width = i.width;
            height = i.height;

            if(!i.is_valid)
            {
                WrappedLogger.warning(String.format("Failed to add file %s, because the image was invalid", f));
                return false;
            }
            
            if(!f.renameTo(mediaFile))
            {
                try
                {
                    Files.copy(f.toPath(), mediaFile.toPath());
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            
            ImageProcessor.createThumbnail(mediaFile, thumbFile);
        }
        else if(FileFormat.isVideoType(mimeType))
        {
            VideoInfo i = VideoProcessor.getVideoInfo(f);
            
            width = i.width;
            height = i.height;
            duration = i.duration_ms;
            has_audio = i.hasAudio;
            
            if(!i.is_valid)
            {
                WrappedLogger.warning(String.format("Failed to add file %s, because the video was invalid", f));
                return false;
            }
            
            try
            {
                VideoProcessor.encodeUniversal(f, tmpMediaFile);
                VideoProcessor.splitVideoForHLS(tmpMediaFile, mediaFile);
            }
            catch (IOException e)
            {
                WrappedLogger.warning(String.format("Failed to encode video file %s", f), e);
                return false;
            }
            
            VideoProcessor.createThumbnail(tmpMediaFile, thumbFile);
            
            tmpMediaFile.delete();
        }
        else if(FileFormat.isAudioType(mimeType))
        {
            has_audio = true;
        }
   
        int hash_id = TableHash.insertHash(b.SHA256);
        
        if(hash_id == -1)
        {
            WrappedLogger.warning(String.format("Unable to insert file with hash %s", fileHash));
            return false;
        }
        
        if(!TableLocalHash.insertHashes(hash_id, b.SHA1, b.MD5, b.PHASH))
        {
            WrappedLogger.warning(String.format("Unable to insert local hashes for file %s", fileHash));
        }
        
        if(!TableFile.insertFile(hash_id, fileSize, mimeType, width, height, duration, has_audio))
        {
            WrappedLogger.warning(String.format("Unable to insert file information for %s", fileHash));
        }
        
        return true;
    }
}
