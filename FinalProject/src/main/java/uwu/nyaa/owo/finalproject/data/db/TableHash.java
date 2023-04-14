package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.ByteHelper;

/**
 * Stores a file hash with a primary key, used for all files
 * @author minno
 *
 */
public class TableHash
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_hash";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_hash("
            + "hash_id serial PRIMARY KEY, "
            + "hash bytea UNIQUE NOT NULL"
            + ");";
    
    
    /**
     * Inserts a new hash into the database
     * @param sha256Hash The SHA256 hash to insert
     * @return The hash_id created or -1
     */
    public static int insertHash(byte[] sha256Hash)
    {
        try (Connection c = DatabaseConnection.getConnection())
        {

            return insertHash(sha256Hash, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("Error inserting hash %s", ByteHelper.bytesToHex(sha256Hash)));    
        }
        
        return -1;
    }
    
    public static int insertHash(byte[] sha256Hash, Connection c) throws SQLException
    {
        final String SQL = "INSERT INTO tbl_hash(hash) VALUES (?) RETURNING hash_id";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {

            pstmt.setBytes(1, sha256Hash);
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {
                return rs.getInt(1);    
            }
        }
       
        return -1;
    }
    /**
     * Looks for the hash_id of the given SHA256
     * @param sha256Hash The SHA256 to search
     * @return The hash_id or -1
     */
    public static int getHashID(byte[] sha256Hash)
    {
        final String SQL = "SELECT hash_id FROM tbl_hash WHERE hash = ?";
        
        try (Connection c = DatabaseConnection.getConnection(); 
            PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setBytes(1, sha256Hash);
            pstmt.execute();
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("Error while searching for %s", ByteHelper.bytesToHex(sha256Hash)));
        }
        
        return -1;
    }
    
    
    /**
     * Prints all rows to stdout
     */
    public static void printAll()
    {
        final String SQL = "SELECT * FROM tbl_hash";
        
        try (Connection c = DatabaseConnection.getConnection(); 
             Statement stmt = c.createStatement())
        {
            ResultSet rs = stmt.executeQuery(SQL);
            
            while(rs.next())
            {
                System.out.println(rs.getInt(1) + " " + ByteHelper.bytesToHex(rs.getBytes(2)));
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
