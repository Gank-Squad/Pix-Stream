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
import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.FileProcessor.Hashes;
import uwu.nyaa.owo.finalproject.data.ImageMagickHelper;
import uwu.nyaa.owo.finalproject.data.ImageProcessor;
import uwu.nyaa.owo.finalproject.data.filedetection.ImageFormat;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

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

            WrappedLogger.warning("Error while creating a new database", e);
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
        GlobalSettings.updatePathsForLinux();
        ProcessStarter.setGlobalSearchPath(GlobalSettings.IMAGE_MAGICK_PATH);
        
        String test = "/mnt/Data/0_IMAGE/SELF/AOL_35.png";
        createDatabase();
        createTables();
        
        ImageMagickHelper.checkImageMagick();
        
        TableFile.addFakeFiles(10);
        
        TableFile.getFiles(10).forEach(x -> {
            System.out.println(x);
        });
    }
}




