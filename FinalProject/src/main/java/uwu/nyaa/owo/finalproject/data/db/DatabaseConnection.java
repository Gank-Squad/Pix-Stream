package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
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
                statement.execute(TableLocalHash.DELETION_QUERY);
                statement.execute(TableHash.DELETION_QUERY);
            }
            
            statement.execute(TableHash.CREATION_QUERY);
            statement.execute(TableLocalHash.CREATION_QUERY);

        }
        catch (SQLException e)
        {
            WrappedLogger.warning("Error while creating database tables", e);
        }
    }
    
    

    public static void main(String args[])
    {
        String test = "/home/minno/Sync/MSI-Portable-2Way/2023Winter/SoftwareSystems/Assignments/w23-csci2020u-project-team16/LICENSE";
        // // deletes and remakes any existing database with the name 'master'
//        createNewDatabase();

        createTables(true);
        
        TableHash.printAll();
        byte[] sha256 = FileProcessor.getSHA256(test);
        byte[] sha1 = FileProcessor.getSHA1(test);
        byte[] md5 = FileProcessor.getMD5(test);
        System.out.println("SHA256 Hash: " + ByteHelper.bytesToHex(sha256));
        System.out.println("SHA1   Hash: " + ByteHelper.bytesToHex(sha1));
        System.out.println("MD5    Hash: " + ByteHelper.bytesToHex(md5));
        
        int id = TableHash.insertHash(sha256);
        
        if(id != -1)
        {
            TableLocalHash.insertHashes(id, sha1, md5, null);
        }
        
        System.out.println(String.format("Found hash_id: %d", id));
    }
}