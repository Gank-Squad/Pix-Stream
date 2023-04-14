package uwu.nyaa.owo.finalproject;

import org.tinylog.Logger;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import uwu.nyaa.owo.finalproject.data.FFmpegHelper;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.db.DatabaseConnection;
import uwu.nyaa.owo.finalproject.data.db.TableTag;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;
import uwu.nyaa.owo.finalproject.system.ResourceLoader;


@ApplicationPath("/api")
public class APIApplication extends Application
{
    // True Main Method here!!!
    // welcome to our API 
    public APIApplication()
    {
        GlobalSettings.IS_DEBUG = true;
        
        ResourceLoader.loadTinyLogConfig();
        Logger.info("Starting...");

        
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

        GlobalSettings.updatePathsForLinux();
        GlobalSettings.updatePathsFromEnv();

        PathHelper.initPaths();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables(GlobalSettings.IS_DEBUG);

        ImageMagickHelper.checkImageMagick();
        FFmpegHelper.checkFFmpeg();
        
        if(GlobalSettings.IS_DEBUG)
        {
            Logger.debug("Adding predefined random tags");
//            TableFile.addFakeFiles(20);
            TableTag.addPredefinedTags(30);
//            TableHashTag.insertRandomAccociations(300);
        }
    }

}