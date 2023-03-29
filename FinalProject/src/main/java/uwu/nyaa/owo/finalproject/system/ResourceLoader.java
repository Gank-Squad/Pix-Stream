package uwu.nyaa.owo.finalproject.system;

import java.nio.file.Path;

import org.tinylog.configuration.Configuration;

public class ResourceLoader
{
    public static void loadTinyLogConfig()
    {
        Configuration.set("writer1", "file");
        Configuration.set("writer1.file", Path.of(GlobalSettings.LOGS_DIR_PATH.toString(), "logs.txt").toString());
        Configuration.set("writer1.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");
        Configuration.set("writer1.append", "true");
//        Configuration.set("writer1.exception", "full");

        Configuration.set("writer2", "console");
        Configuration.set("writer2.format", "[{date: yyyy-MM-dd HH:mm:ss.SSS}] [{level}] {message}");
    }
}
