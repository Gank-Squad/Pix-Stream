package uwu.nyaa.owo.finalproject.system;

import org.tinylog.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class GlobalSettings
{
    public static boolean IS_DEBUG = true;
    
//    public static String IMAGE_MAGICK_PATH = "C:/bin/imageMagick"; // "F:/Programs/ImageMagick";
    public static String IMAGE_MAGICK_PATH = "F:/Programs/ImageMagick";


//    public static String FFMPEG_PATH = "C:/bin/ffmpeg/ffmpeg.exe";//"F:/Programs/FFMPEG/ffmpeg-2023-03-05/bin/ffmpeg";
    public static String FFMPEG_PATH = "F:/Programs/FFMPEG/ffmpeg-2023-03-05/bin/ffmpeg";


//    public static String FFPROBE_PATH = "C:/bin/ffmpeg/ffprobe.exe";//"F:/Programs/FFMPEG/ffmpeg-2023-03-05/bin/ffprobe";
    public static String FFPROBE_PATH = "F:/Programs/FFMPEG/ffmpeg-2023-03-05/bin/ffprobe"; // "C:/bin/ffmpeg/ffprobe.exe"


    public static final Path LOGS_DIR_PATH = Paths.get(".", "logs");
    
    public static Level LOG_LEVEL = Level.ALL;
    
    /**
     * the directory where all dynamic media will be stored
     */
    public static String MEDIA_PATH = "client_files/";
    
    /**
     * the value starting each of the subdirectories for media
     */
    public static String MEDIA_PATH_PREFIX = "f";
    
    /**
     * the value starting each of the subdirectories for thumbnails
     */
    public static String THUMB_PATH_PREFIX = "t";
    
    
    public static void updatePathsForLinux()
    {
        if(System.getProperty("os.name").toLowerCase().equals("linux"))
        {
            Logger.info("Updating paths for linux");
            GlobalSettings.IMAGE_MAGICK_PATH = "/bin/";
            GlobalSettings.FFMPEG_PATH = "/bin/ffmpeg";
            GlobalSettings.FFPROBE_PATH = "/bin/ffprobe";
        }
    }
}
