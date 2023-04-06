package uwu.nyaa.owo.finalproject.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.ImageProcessor.ImageInfo;
import uwu.nyaa.owo.finalproject.data.VideoProcessor.VideoInfo;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.db.TableHash;
import uwu.nyaa.owo.finalproject.data.db.TableLocalHash;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;
import uwu.nyaa.owo.finalproject.data.models.FileUpload;

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




    public static FileUpload addFile(String path)
    {
        return addFile(new File(path));
    }
    
    public static FileUpload addFile(File f)
    {
        FileUpload fa = new FileUpload();
        fa.hash_id = -1;

        if(!f.isFile())
        {
            Logger.info("Could not add file: {}, because it does not exist or is not a file", f);
            return fa;
        }
            
        
        Hashes b = getFileHashes(f);
        
        if(b == null || b.SHA256.length == 0)
        {
            Logger.info("Could not add file: {}, because the SHA256 hash was empty", f);
            return fa;
        }

        fa.hashes = b;
        String fileHash = ByteHelper.bytesToHex(b.SHA256);
        String mediaPath = PathHelper.getMediaPath(fileHash);
        String thumbPath = PathHelper.getThmbnailPath(fileHash);
        File mediaFile = new File(mediaPath);
        File thumbFile = new File(thumbPath);
        File tmpMediaFile = new File(mediaFile.getAbsoluteFile() + ".tmp");

        if(mediaFile.getParentFile() != null)
        {
            mediaFile.getParentFile().mkdirs();
        }

        if(thumbFile.getParentFile() != null)
        {
            thumbFile.getParentFile().mkdirs();
        }

        if(mediaFile.exists())
        {
            fa.hash_id = TableHash.getHashID(b.SHA256);
            Logger.warn("Ignoring adding file {} because it media file already exist; Assuming it's in the db", f);
            return fa;
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
                Logger.warn("Failed to add file {}, because the image was invalid", f);
                return fa;
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
                    return fa;
                }
            }
            Logger.debug("About to create thumbnail from {} into {}", mediaFile, thumbPath);
            ImageProcessor.createThumbnail(mediaFile, thumbFile);
        }
        else if(FileFormat.isVideoType(mimeType) || FileFormat.isAudioType(mimeType))
        {
            VideoInfo i = VideoProcessor.getVideoInfo(f);
            
            width = i.width;
            height = i.height;
            duration = i.duration_ms;
            has_audio = i.hasAudio;
            
            if(!i.is_valid)
            {
                Logger.warn("Failed to add file {}, because the video was invalid", f);
                return fa;
            }
            
            try
            {
                VideoProcessor.encodeUniversal(f, tmpMediaFile);
                VideoProcessor.splitVideoForHLS(tmpMediaFile, mediaFile);
            }
            catch (IOException e)
            {
                Logger.warn(e, "Failed to encode video file {}", f);
                return fa;
            }

            if(!FileFormat.isAudioType(mimeType))
            {
                VideoProcessor.createThumbnail(tmpMediaFile, thumbFile);
            }

            tmpMediaFile.delete();
        }

        int hash_id = TableHash.insertHash(b.SHA256);

        fa.filePath = mediaFile;
        fa.hash_id = hash_id;

        if(hash_id == -1)
        {
            Logger.warn("Unable to insert file with hash {}", fileHash);
            return fa;
        }
        
        if(!TableLocalHash.insertHashes(hash_id, b.SHA1, b.MD5, b.PHASH))
        {
            Logger.warn("Unable to insert local hashes for file {}", fileHash);
        }
        
        if(!TableFile.insertFile(hash_id, fileSize, mimeType, width, height, duration, has_audio))
        {
            Logger.warn("Unable to insert file information for {}", fileHash);
        }

        fa.accepted = true;
        return fa;
    }
}
