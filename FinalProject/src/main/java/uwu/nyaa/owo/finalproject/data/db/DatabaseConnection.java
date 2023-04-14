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
    
    // public static final String POSTGRES_DOMAIN = "192.168.1.148";
    public static final String POSTGRES_DOMAIN = "localhost";
    public static final int POSTGRES_PORT = 5432;
    public static final String POSTGRES_URI = String.format("jdbc:postgresql://%s:%d/", POSTGRES_DOMAIN, POSTGRES_PORT);
    public static final String POSTGRES_USER = "postgres";
    // a very good idea, nobody will guess this ;3c
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

    /**
     * Creates the database
     */
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
                statement.execute(TablePostFiles.DELETION_QUERY);
                
                statement.execute(TableFile.DELETION_QUERY);
                statement.execute(TableLocalHash.DELETION_QUERY);
                statement.execute(TableHash.DELETION_QUERY);
                
                statement.execute(TableUsers.DELETION_QUERY);

                statement.execute(TableTag.DELETION_QUERY);
                statement.execute(TableSubtag.DELETION_QUERY);
                statement.execute(TableNamespace.DELETION_QUERY);
                
                statement.execute(TablePost.DELETION_QUERY);
            }
            
            // NOTE: order matters!
            // make sure foreign key tables are made last!!!
            statement.execute(TableHash.CREATION_QUERY);
            statement.execute(TableLocalHash.CREATION_QUERY);
            statement.execute(TableFile.CREATION_QUERY);
            
            statement.execute(TableUsers.CREATION_QUERY);

            statement.execute(TableSubtag.CREATION_QUERY);
            statement.execute(TableNamespace.CREATION_QUERY);
            statement.execute(TableTag.CREATION_QUERY);
            statement.execute(TableHashTag.CREATION_QUERY);
            
            statement.execute(TablePost.CREATION_QUERY);
            statement.execute(TablePostFiles.CREATION_QUERY);
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error while creating database tables");
        }
    }
    
    

    // DEBUG ONLY 
    public static void main(String args[]) throws IOException, InterruptedException, IM4JavaException
    {
        ResourceLoader.loadTinyLogConfig();
        Logger.info("Starting...");

        GlobalSettings.IS_DEBUG = true;
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

        GlobalSettings.updatePathsForLinux();
        
//        PathHelper.createMediaDirectory();
        DatabaseConnection.createDatabase();
        DatabaseConnection.createTables(true);

        GlobalSettings.updatePathsFromEnv();

        String title = "Hello world";
        String description = "nya nyan yanyn nyanyn ynynany ";
        
        int file  =TableHash.insertHash(new byte[] {});
        int file2 =TableHash.insertHash(new byte[] {1});
        int id = TablePost.insertPost(title, description);
        TablePostFiles.insertAssociation(id, file);
        TablePostFiles.insertAssociation(id, file2);
        Logger.info("post id {}", id);
        
        
        TablePost.getPosts(3, true);
        
    }
}




