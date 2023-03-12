package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        // // deletes and remakes any existing database with the name 'master'
        // createNewDatabase();

        try (Connection c = getConnection(); Statement statement = c.createStatement())
        {

            statement.execute(String.join("", new String[] {
                "CREATE TABLE IF NOT EXISTS accounts (",
                    "user_id serial PRIMARY KEY,",
                    "username VARCHAR ( 50 ) NOT NULL,",
                    "password VARCHAR ( 50 ) NOT NULL);"
            }));
            
            statement.execute(String.join("", new String[] {
                    "INSERT INTO accounts (user_id, username, password)",
                    "VALUES ('5', 'hell3o', 'password')",
                }));
            
            
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}