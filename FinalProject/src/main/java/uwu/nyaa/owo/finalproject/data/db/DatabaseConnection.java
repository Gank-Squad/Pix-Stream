package uwu.nyaa.owo.finalproject.data.db;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.im4java.core.IM4JavaException;
import org.im4java.process.ProcessStarter;
import org.postgresql.xml.NullErrorHandler;
import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.FFmpegHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.ImageProcessor;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;
import uwu.nyaa.owo.finalproject.system.ResourceLoader;

public class DatabaseConnection
{
    // pgdmin and postgres setup docker help
    // https://stackoverflow.com/questions/25540711/docker-postgres-pgadmin-local-connection
    
    // actually using postgres from java
    // https://www.postgresqltutorial.com/postgresql-jdbc/query/
    
    public static final String POSTGRES_URI = "jdbc:postgresql://localhost:5432/";
    public static final String POSTGRES_USER = "postgres";
    public static final String POSTGRES_PASSWORD = "123";

    public static final String POSTGRES_DATABASE = "master";

    static {
        try
        {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public static Connection getConnection(String database) throws SQLException
    {
        return DriverManager.getConnection(POSTGRES_URI + database, POSTGRES_USER, POSTGRES_PASSWORD);
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException
    {
        return getConnection(POSTGRES_DATABASE);
    }

    public static void createDatabase()
    {
        try (Connection c = getConnection("");
                Statement statement = c.createStatement())
        {
            statement.executeUpdate("CREATE DATABASE master");
        }
        catch (SQLException e)
        {
            if (e.getMessage().equals("ERROR: database \"master\" already exists"))
            {
                return;
            }

            Logger.warn(e, "Error while creating a new database");
        }
    }
    
    /**
     * Drops and creates the 'master' database 
     */
    public static void createNewDatabase()
    {
        try (Connection c = getConnection("");
                Statement statement = c.createStatement())
        {
            statement.execute("DROP DATABASE IF EXISTS master");
            statement.executeUpdate("CREATE DATABASE master");
        }
        catch (SQLException e)
        {
            if (e.getMessage().equals("ERROR: database \"master\" already exists"))
            {
                return;
            }

            Logger.warn(e, "Error while creating a new database");
        }
    }
    
    /**
     * Creates all the tables for the database
     */
    public static void createTables()
    {
        createTables(false);
    }
    
    /**
     * Creates all the tables for the database
     * @param fromNew Should all existing tables be dropped first
     */
    public static void createTables(boolean fromNew)
    {
        try (Connection c = getConnection(); Statement statement = c.createStatement())
        {
            if(fromNew)
            {
                // NOTE: order matters!
                // TableHash is a primary key for a bunch of foreign keys
                // you must delete all foreign keys first before you can delete it
                statement.execute(TableHashTag.DELETION_QUERY);

                statement.execute(TableFile.DELETION_QUERY);
                statement.execute(TableLocalHash.DELETION_QUERY);
                statement.execute(TableHash.DELETION_QUERY);
                
                statement.execute(TableUsers.DELETION_QUERY);

                statement.execute(TableTag.DELETION_QUERY);
                statement.execute(TableSubtag.DELETION_QUERY);
                statement.execute(TableNamespace.DELETION_QUERY);
            }
            
            statement.execute(TableHash.CREATION_QUERY);
            statement.execute(TableLocalHash.CREATION_QUERY);
            statement.execute(TableFile.CREATION_QUERY);
            
            statement.execute(TableUsers.CREATION_QUERY);

            statement.execute(TableSubtag.CREATION_QUERY);
            statement.execute(TableNamespace.CREATION_QUERY);
            statement.execute(TableTag.CREATION_QUERY);
            statement.execute(TableHashTag.CREATION_QUERY);
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error while creating database tables");
        }
    }
    
    

    public static void main(String args[]) throws IOException, InterruptedException, IM4JavaException
    {
        ResourceLoader.loadTinyLogConfig();
        Logger.info("Starting...");

        GlobalSettings.IS_DEBUG = true;
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

        GlobalSettings.updatePathsForLinux();
        
        PathHelper.createMediaDirectory();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables(true);

        ProcessStarter.setGlobalSearchPath(GlobalSettings.IMAGE_MAGICK_PATH);
        ImageMagickHelper.checkImageMagick();
        FFmpegHelper.checkFFmpeg();
        
        String test = "/home/minno/Pictures/6c6b02a2c269f2013587de08c2bad122bb343ab8ec18a61518cb3c7f9b443d8e";
//        createDatabase();
//        createTables(true);
        
        TableFile.addFakeFiles(10);
        
        int tag1 = TableTag.insertTag("hello:world");
        int tag2 = TableTag.insertTag("hello:world1");
        int tag3 = TableTag.insertTag("hello:world2");
        int tag4 = TableTag.insertTag("hello:world3");
        TableTag.addPredefinedTags(10);
//        TableFile.addFakeFiles(50);
//        TableTag.addPredefinedTags(200);
//        TableHashTag.insertRandomAccociations(20);
        TableHashTag.insertAssociation(1, tag4);
        TableHashTag.insertAssociation(1, tag3);
        TableHashTag.insertAssociation(1, tag2);
        TableHashTag.insertAssociation(1, tag1);
        
        TableHashTag.insertAssociation(2, tag2);
        TableHashTag.insertAssociation(2, tag1);
        
        TableHashTag.insertAssociation(3, tag2);
        TableHashTag.insertAssociation(3, tag1);
        
        TableHashTag.insertAssociation(4, tag3);
        TableHashTag.insertAssociation(4, tag4);
        TableHashTag.insertAssociation(4, tag2);
        
        TableHashTag.insertAssociation(5, tag3);
        TableHashTag.insertAssociation(5, tag4);
        
        TableHashTag.getFilesContaining(new int[] { 1, 2}, 100, false).forEach(x -> {
            System.out.println(x);
        });
        
        File f = new File (test);
        Logger.info(f.exists());
        BufferedImage buff = ImageProcessor.loadImage(f);
        
        if(buff == null)
            throw new NullPointerException();
        
        String output = "/home/minno/Sync/MSI-Portable-2Way/2023Winter/SoftwareSystems/Assignments/w23-csci2020u-project-team16/FinalProject/client_files/t81/81a9d81dd03f2bf3726b288255f300115efe91fafa3a3b42366053689a4d9c0d";
        File c = new File(output);
        
        if(c.getParentFile() != null)
            c.getParentFile().mkdirs();
        
        ImageMagickHelper.saveImageWithMagick(buff, test, FileFormat.Image.JPG);
        
//        FileProcessor.addFile(new  File(test));
        
        
        /*
         
          SELECT tbl_tag.tag_id, tbl_namespace.namespace, tbl_subtag.subtag 
            FROM tbl_tag 
            JOIN tbl_namespace ON tbl_tag.namespace_id = tbl_namespace.namespace_id
            JOIN tbl_subtag ON tbl_tag.subtag_id = tbl_subtag.subtag_id
         
         */
    }
}




