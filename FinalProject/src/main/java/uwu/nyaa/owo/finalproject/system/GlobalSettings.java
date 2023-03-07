package uwu.nyaa.owo.finalproject.system;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class GlobalSettings
{
    public static String FFMPEG_PATH = "/bin/ffmpeg";
    public static String FFPROBE_PATH = "/bin/ffprobe";
    
    public static final Path LOG_FILE_DIRECTORY = Paths.get(".", "logs");
    
    public static Level LOG_LEVEL = Level.ALL;
}
