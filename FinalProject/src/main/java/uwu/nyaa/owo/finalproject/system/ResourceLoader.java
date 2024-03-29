package uwu.nyaa.owo.finalproject.system;

import java.nio.file.Path;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

public class ResourceLoader
{
    public static void loadTinyLogConfig()
    {
        try
        {
            Configuration.set("writer1", "file");
            Configuration.set("writer1.file", Path.of(GlobalSettings.LOGS_DIR_PATH.toString(), "logs.txt").toString());
            Configuration.set("writer1.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");
            Configuration.set("writer1.append", "true");
            Configuration.set("writer1.level", "trace");

            Configuration.set("writer2", "console");
            Configuration.set("writer2.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");
            
            if(GlobalSettings.IS_DEBUG)
            {
                Configuration.set("writer2.level", "trace");
            }
            else 
            {
                Configuration.set("writer2.level", "info");
            }
                
            Logger.info("TinyLog has initialized!");
        }
        catch (UnsupportedOperationException e)
        {
            Logger.warn("Tried to update tinylog config, it was already set");
        }
    }
}
