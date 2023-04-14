package uwu.nyaa.owo.finalproject.system;

import org.im4java.process.ProcessStarter;
import org.tinylog.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.logging.Level;

public class GlobalSettings
{
    public static boolean IS_DEBUG = false;

    public static final int THUMBNAIL_SIZE = 256;

    public static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.now(ZoneOffset.UTC);
    
    
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

    public static Boolean isLinux = false;

    /**
     * Tries to find ffmpeg, ffprobe and image magick in env variables 
     */
    public static void updatePathsFromEnv()
    {
        if (ProcessStarter.getGlobalSearchPath() == null)
        {
            Logger.info("could not find environmental variable IM4JAVA_TOOLPATH, using PATH instead");
            ProcessStarter.setGlobalSearchPath(System.getenv("PATH"));
        }
        else
        {
            Logger.info("global magick search path set [{}]", ProcessStarter.getGlobalSearchPath());
            GlobalSettings.IMAGE_MAGICK_PATH = ProcessStarter.getGlobalSearchPath();
        }

        if(System.getenv("FFMPEG") != null)
        {
            searchFFMPEG(System.getenv("FFMPEG"));
        }

        if(System.getenv("PATH") != null && File.pathSeparator != null)
        {
            for(String dir : System.getenv("PATH").split(File.pathSeparator))
            {
                searchFFMPEG(dir);
            }
        }

        if(new File(FFMPEG_PATH).isFile())
        {
            Logger.info("FFMPEG found {}", FFMPEG_PATH);
        }

        if(new File(FFPROBE_PATH).isFile())
        {
            Logger.info("FFPROBE found {}", FFPROBE_PATH);
        }
    }
    
    /**
     * sets the default paths to my linux machines ffmpeg and magick
     */
    public static void updatePathsForLinux()
    {
        if(System.getProperty("os.name").toLowerCase().equals("linux"))
        {
            Logger.info("Updating paths for linux");
            GlobalSettings.IMAGE_MAGICK_PATH = "/bin/";
            GlobalSettings.FFMPEG_PATH = "/bin/ffmpeg";
            GlobalSettings.FFPROBE_PATH = "/bin/ffprobe";
            isLinux = true;
        }
    }


    /**
     * Looks for ffmpeg and ffprobe in the given dir 
     * @param dir
     */
    public static void searchFFMPEG(String dir)
    {
        String ext = isLinux ? "" : ".exe";
        File f = Path.of(dir, "ffmpeg" + ext).toFile();

        if(f.isFile())
        {
            FFMPEG_PATH = f.getAbsolutePath();
        }

        f = Path.of(dir, "ffprobe" + ext).toFile();

        if(f.isFile())
        {
            FFPROBE_PATH = f.getAbsolutePath();
        }
    }
}
