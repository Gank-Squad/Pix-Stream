package uwu.nyaa.owo.finalproject.data;

import java.io.File;

import org.tinylog.Logger;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

/**
 * Handles folder structure and file pathing
 * @author minno
 *
 */
public class PathHelper
{
    // where allll the stuff goes, for uploaded media
    public static final File MEDIA_DIR_BASE;

    static  {
        MEDIA_DIR_BASE = new File(GlobalSettings.MEDIA_PATH);
        MEDIA_DIR_BASE.mkdirs();
    }
    
    /**
     * Creates all the needed directories for stuff
     */
    public static void initPaths()
    {
        MEDIA_DIR_BASE.mkdirs();
        
        if(!MEDIA_DIR_BASE.isDirectory())
        {
            Logger.error("Could not create media directory {}", MEDIA_DIR_BASE);
            throw new RuntimeException(String.format("Could not create media directory: %s", MEDIA_DIR_BASE));
        }

        PathHelper.createMediaDirectory();
    }
    
    /**
     * Creates the media directory structure, it looks something like: <br>
     *                      <br>
     * ./client_files       <br>
     *     /f00                                    <br>
     *        /00{hexadecimal}    - some file      <br>
     *        /00{hexadecimal}                     <br>
     *        /00{hexadecimal}                     <br>
     *     /f01                                    <br>
     *     /f02                                    <br>
     *     /f..                                    <br>
     *     /fff                                    <br>
     *                                             <br>
     *     /t00                                    <br>
     *     /t01                                    <br>
     *     /t02                                    <br>
     *     /t..                                    <br>
     *     /tff                                    <br>
     */
    public static void createMediaDirectory()
    {
        for(int i = 0; i < 256; i++)
        {
            String hex = String.format("%02X", i).toLowerCase();
            
            File subDir = new File(MEDIA_DIR_BASE, GlobalSettings.MEDIA_PATH_PREFIX + hex);
            subDir.mkdirs();

            if(GlobalSettings.IS_DEBUG)
            {
               Logger.debug("Creating: {}", subDir.getAbsolutePath());
            }
            
            File subDir2 = new File(MEDIA_DIR_BASE, GlobalSettings.THUMB_PATH_PREFIX + hex);
            subDir2.mkdirs();

            if(GlobalSettings.IS_DEBUG)
            {
                Logger.debug("Creating: {}", subDir2.getAbsolutePath());
            }
        }
    }
    
    /**
     * gets the path to the given media value, DOES NOT CHECK THE STRING, 
     * @param name a sha256 file hash which corresponds to a file with the hash
     * @return the path to the file 
     */
    public static String getMediaPath(String name)
    {
        String start = name.substring(0, 2);
        
        return new File(MEDIA_DIR_BASE, String.format("%s%s/%s",GlobalSettings.MEDIA_PATH_PREFIX, start, name)).getPath();
    }
    
    /**
     * gets the path to the given media value, DOES NOT CHECK THE STRING, 
     * @param name a sha256 file hash which corresponds to the thumbnail of the file with the hash
     * @return the path to the file 
     */
    public static String getThmbnailPath(String name)
    {
        String start = name.substring(0, 2);
        
        return new File(MEDIA_DIR_BASE, String.format("%s%s/%s",GlobalSettings.THUMB_PATH_PREFIX,start, name)).getPath();
    }

}
