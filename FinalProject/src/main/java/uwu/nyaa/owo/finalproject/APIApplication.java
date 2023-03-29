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
        
        PathHelper.createMediaDirectory();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables();

        ProcessStarter.setGlobalSearchPath(GlobalSettings.IMAGE_MAGICK_PATH);
        ImageMagickHelper.checkImageMagick();
        FFmpegHelper.checkFFmpeg();
        
        if(GlobalSettings.IS_DEBUG)
        {
            TableFile.addFakeFiles(10);

            System.out.println( TableTag.insertTag("hello:world"));
            System.out.println( TableTag.insertTag("hello:dog"));
            System.out.println( TableTag.insertTag("hello:cat"));
            System.out.println( TableTag.insertTag("hello:person"));
            System.out.println( TableTag.insertTag("hello:nyah"));
            System.out.println( TableTag.insertTag("hello:tag"));
            System.out.println( TableTag.insertTag("hello:stuff"));
            System.out.println( TableTag.insertTag("hello:person"));

            TableHashTag.insertAssociation(1, 2);
            TableHashTag.insertAssociation(1, 3);
            TableHashTag.insertAssociation(1, 5);
            TableHashTag.insertAssociation(2, 4);
            TableHashTag.insertAssociation(2, 4);
            TableHashTag.insertAssociation(1, 6);
        }
    }

}