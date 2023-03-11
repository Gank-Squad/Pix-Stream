package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection
{
    public static void main(String args[])
    {
        Connection c = null;
        try
        {
            Class.forName("org.postgresql.Driver");

            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "123");

            c.setAutoCommit(false);
            System.out.println("Opened database successfully");


        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}