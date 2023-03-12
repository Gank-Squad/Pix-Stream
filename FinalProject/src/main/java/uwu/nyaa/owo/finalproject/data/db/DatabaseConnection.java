package uwu.nyaa.owo.finalproject.data.db;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.im4java.core.IM4JavaException;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.FileProcessor.Hashes;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.ImageProcessor;
import uwu.nyaa.owo.finalproject.data.filedetection.ImageFormat;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

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

    
    /**
     * Drops and creates the 'master' database 
     */
    public static void createNewDatabase()
    {
        try (Connection c = getConnection(""))
        {
            Statement statement = c.createStatement();

            statement.execute("DROP DATABASE IF EXISTS master");
            statement.executeUpdate("CREATE DATABASE master");

            statement.close();
        }
        catch (SQLException e)
        {
            if (e.getMessage().equals("ERROR: database \"master\" already exists"))
            {
                return;
            }

            WrappedLogger.warning("Error while creating a new database", e);
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
                statement.execute(TableFile.DELETION_QUERY);
                statement.execute(TableLocalHash.DELETION_QUERY);
                statement.execute(TableHash.DELETION_QUERY);
                
                statement.execute(TableUsers.DELETION_QUERY);
            }
            
            statement.execute(TableHash.CREATION_QUERY);
            statement.execute(TableLocalHash.CREATION_QUERY);
            statement.execute(TableFile.CREATION_QUERY);
            
            statement.execute(TableUsers.CREATION_QUERY);

        }
        catch (SQLException e)
        {
            WrappedLogger.warning("Error while creating database tables", e);
        }
    }
    
    

    public static void main(String args[]) throws IOException, InterruptedException, IM4JavaException
    {
        String test = "/mnt/Data/0_IMAGE/SELF/AOL_35.png";
        
        createTables(true);
        
        ImageMagickHelper.checkImageMagick();
        
        BufferedImage image = ImageMagickHelper.loadImageWithMagick(test);
        
        ImageProcessor.ImageInfo info = ImageMagickHelper.getImageInfo(test);
        System.out.println(info);
        
        TableHash.printAll();
        byte[] sha256 = FileProcessor.getSHA256(test);
        byte[] sha1 = FileProcessor.getSHA1(test);
        byte[] md5 = FileProcessor.getMD5(test);
        Hashes h = FileProcessor.getFileHashes(test);
        System.out.println(h);
        System.out.println("SHA256 Hash: " + ByteHelper.bytesToHex(sha256));
        System.out.println("SHA1   Hash: " + ByteHelper.bytesToHex(sha1));
        System.out.println("MD5    Hash: " + ByteHelper.bytesToHex(md5));
        
        FileProcessor.addFile(test);
        
//        int id = TableHash.insertHash(sha256);
//        
//        if(id != -1)
//        {
//            System.out.println("Inserted new hash with ID %d".formatted(id));
//            if(TableLocalHash.insertHashes(id, sha1, md5, null))
//            {
//                System.out.println("Inserted local hashes for %d".formatted(id));
//            }
//            else 
//            {
//                System.out.println("Failed to insert local hashes for %d".formatted(id));
//            }
//            
//            long fileSize = new File(test).length();
//            byte mime = ImageFormat.JPG;
//            int width = 0;
//            int height = 0;
//            int duration = 0;
//            boolean has_audio = false;
//            
//            if(TableFile.insertFile(id, fileSize, mime, width, height, duration, has_audio))
//            {
//                System.out.println("Inserted file information for ID %d".formatted(id));
//            }
//            
//        }
        
        
    }
}