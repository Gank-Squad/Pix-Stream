package uwu.nyaa.owo.finalproject;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import uwu.nyaa.owo.finalproject.data.FFmpegHelper;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.db.DatabaseConnection;


@ApplicationPath("/api")
public class APIApplication extends Application
{
    // this is my makeshift main method
    public APIApplication()
    {
        PathHelper.createMediaDirectory();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables();

        ImageMagickHelper.checkImageMagick();
        FFmpegHelper.checkFFmpeg();
    }

}