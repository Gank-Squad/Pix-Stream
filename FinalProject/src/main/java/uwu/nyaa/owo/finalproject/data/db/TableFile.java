package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.FileProcessor.ImmutableMessageDigest;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;
import uwu.nyaa.owo.finalproject.data.models.HashInfoBase;

/**
 * Stores file metadata about a hash as well as queries using this table
 * @author minno
 *
 */
public class TableFile
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_file";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_file("
            + "hash_id serial PRIMARY KEY, "
            + "size BIGINT NOT NULL, "
            + "mime INT NOT NULL,"
            + "width INT NOT NULL, "
            + "height INT NOT NULL,"
            + "duration INT NOT NULL,"
            + "has_audio BOOLEAN NOT NULL,"
            + "CONSTRAINT fk_hash_id FOREIGN KEY(hash_id) REFERENCES tbl_hash(hash_id)"
            + ");";
    

    /**
     * Inserts file information for the given hash_id
     * @param hashID The hash_id with the given information
     * @param size The size of the file in bytes
     * @param mime The mime type of the file
     * @param width The width of the image/video/audio or just 0
     * @param height The height of the image/video/audio or just 0
     * @param duration The duration of the video/audio/gif or just 0
     * @param has_audio If the file has audio or not
     * @return True if the data was inserted otherwise False
     */
    public static boolean insertFile(int hashID, long size, byte mime, int width, int height, int duration, boolean has_audio)
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertFile(hashID, size, mime, width, height, duration, has_audio, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("Error inserting file information with ID %d", hashID));
        }
        
        return false;
    }
    
    public static boolean insertFile(int hashID, long size, byte mime, int width, int height, int duration, boolean has_audio, Connection c) throws SQLException
    {
        final String SQL = "INSERT INTO tbl_file(hash_id, size, mime, width, height, duration, has_audio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, hashID);
            pstmt.setLong(2, size);
            pstmt.setInt(3, mime);
            pstmt.setInt(4, width);
            pstmt.setInt(5, height);
            pstmt.setInt(6, duration);
            pstmt.setBoolean(7, has_audio);
            pstmt.execute();
        }
        
        return true;
    }
    
    public static List<HashInfo> getFiles(int limit, boolean includeTags)
    {
        LinkedList<HashInfo> items = new LinkedList<>();
        
        final String SQL = "SELECT tbl_hash.hash_id, hash, mime, tbl_file.size, width, height, duration, has_audio FROM tbl_file JOIN tbl_hash ON tbl_file.hash_id = tbl_hash.hash_id LIMIT ?";
        
        try (Connection c = DatabaseConnection.getConnection(); 
                PreparedStatement pstmt = c.prepareStatement(SQL))
        {
          
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                HashInfo a = new HashInfo();
                
                a.hash_id = rs.getInt(1);
                a.hash = rs.getBytes(2);
                a.mime = rs.getInt(3);
                a.fileSize = rs.getLong(4);
                a.width = rs.getInt(5);
                a.height = rs.getInt(6);
                a.duration = rs.getInt(7);
                a.has_audio = rs.getBoolean(8);

                if(includeTags)
                {
                    a.tags = TableHashTag.getTags(a.hash_id, c);
                }
                
                items.add(a);
            }
            return items;
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error searching for files");
        }
        
        return items;
    }

    public static List<HashInfoBase> getFiles(List<Integer> hash_ids, boolean includeTags, Connection c) throws SQLException
    {
        return getFiles(hash_ids, includeTags, new LinkedList<>(), c);
    }
        
    public static List<HashInfoBase> getFiles(List<Integer> hash_ids, boolean includeTags, List<HashInfoBase> items, Connection c) throws SQLException
    {
        if (items == null)
            items = new LinkedList<>();
        
        final String SQL = "SELECT tbl_hash.hash_id, hash, mime, tbl_file.size, width, height, duration, has_audio FROM tbl_file JOIN tbl_hash ON tbl_file.hash_id = tbl_hash.hash_id WHERE tbl_hash.hash_id = ANY(?)";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            Array hashIdArrayParam = c.createArrayOf("INTEGER", hash_ids.toArray());
            pstmt.setArray(1, hashIdArrayParam);
            
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                HashInfo a = new HashInfo();
                
                a.hash_id = rs.getInt(1);
                a.hash = rs.getBytes(2);
                a.mime = rs.getInt(3);
                a.fileSize = rs.getLong(4);
                a.width = rs.getInt(5);
                a.height = rs.getInt(6);
                a.duration = rs.getInt(7);
                a.has_audio = rs.getBoolean(8);

                if(includeTags)
                {
                    a.tags = TableHashTag.getTags(a.hash_id, c);
                }
                
                items.add(a);
            }
        }
 
        return items;
    }


    public static HashInfo getFile(byte[] hash, boolean includeTags)
    {
        final String SQL = "SELECT tbl_hash.hash_id, hash, mime, tbl_file.size, width, height, duration, has_audio FROM tbl_file JOIN tbl_hash ON tbl_file.hash_id = tbl_hash.hash_id WHERE tbl_hash.hash = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement pstmt = c.prepareStatement(SQL))
        {

            pstmt.setBytes(1, hash);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next())
            {
                HashInfo a = new HashInfo();

                a.hash_id = rs.getInt(1);
                a.hash = rs.getBytes(2);
                a.mime = rs.getInt(3);
                a.fileSize = rs.getLong(4);
                a.width = rs.getInt(5);
                a.height = rs.getInt(6);
                a.duration = rs.getInt(7);
                a.has_audio = rs.getBoolean(8);

                if(includeTags)
                {
                    a.tags = TableHashTag.getTags(a.hash_id, c);
                }

                return a;
            }

            return null;
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error searching for files");
        }

        return null;
    }






    public static void addFakeFiles(int amount)
    {
        for(int i = 0; i < amount; i++)
        {
            long shittyRandom1 = ((long)System.currentTimeMillis()/(1 + i) % 200) + (int)(System.nanoTime() * (1+i) * (1+i)) / 3;
            long shittyRandom2 = ((long)System.nanoTime()/(1 + i) % 200) + (int)(System.currentTimeMillis() * (1+i) * (1+i)) / 3;
            
            if(shittyRandom1 < 0)
                shittyRandom1 = - shittyRandom1;
            
            if(shittyRandom2 < 0)
                shittyRandom2 = - shittyRandom2;
     
            long fakeSize = shittyRandom1 % 1024*1024*5;
            int fakeMime = (int)shittyRandom1 % 25;
            int fakeWidth = 10 + (int)shittyRandom1 % 1000;
            int fakeHeight = 10 + (int)shittyRandom2 % 1000;
            
            byte[] hash = ImmutableMessageDigest.getSHA256().digest((shittyRandom1 + "Hello").getBytes());
            int hash_id = TableHash.insertHash(hash);
            
            if(hash_id != -1)
            {
                TableFile.insertFile(hash_id, fakeSize, (byte)fakeMime, fakeWidth, fakeHeight, 0, false);
            }    
        }
    }
}