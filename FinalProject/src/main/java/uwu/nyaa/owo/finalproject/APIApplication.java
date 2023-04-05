package uwu.nyaa.owo.finalproject;

import org.im4java.process.ProcessStarter;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.tinylog.Logger;
import uwu.nyaa.owo.finalproject.data.FFmpegHelper;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.db.DatabaseConnection;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.db.TableHashTag;
import uwu.nyaa.owo.finalproject.data.db.TableTag;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;
import uwu.nyaa.owo.finalproject.system.ResourceLoader;


@ApplicationPath("/api")
public class APIApplication extends Application
{
    // this is my makeshift main method
    public APIApplication()
    {
        ResourceLoader.loadTinyLogConfig();
        Logger.info("Starting...");

        GlobalSettings.IS_DEBUG = true;
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

        GlobalSettings.updatePathsForLinux();
        GlobalSettings.updatePathsFromEnv();

        PathHelper.createMediaDirectory();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables(true);

        ImageMagickHelper.checkImageMagick();
        FFmpegHelper.checkFFmpeg();
        
        if(GlobalSettings.IS_DEBUG)
        {
//            TableFile.addFakeFiles(20);
            TableTag.addPredefinedTags(30);
//            TableHashTag.insertRandomAccociations(300);
        }
    }

}