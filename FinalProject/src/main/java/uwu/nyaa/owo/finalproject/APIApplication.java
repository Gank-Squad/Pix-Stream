package uwu.nyaa.owo.finalproject;

import org.im4java.process.ProcessStarter;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import uwu.nyaa.owo.finalproject.data.FFmpegHelper;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.db.DatabaseConnection;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;


@ApplicationPath("/api")
public class APIApplication extends Application
{
    // this is my makeshift main method
    public APIApplication()
    {
        boolean debug = true;
        
        GlobalSettings.updatePathsForLinux();
        
        PathHelper.createMediaDirectory();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables(true);

        ProcessStarter.setGlobalSearchPath(GlobalSettings.IMAGE_MAGICK_PATH);
        ImageMagickHelper.checkImageMagick();
        FFmpegHelper.checkFFmpeg();
        
        if(debug)
        {
            TableFile.addFakeFiles(50);    
        }
    }

}