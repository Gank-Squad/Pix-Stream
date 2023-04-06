package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.models.Post;

public class TablePost
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_post";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_post("
            + "post_id serial PRIMARY KEY, "
            + "time TIMESTAMP NOT NULL, "
            + "title VARCHAR NOT NULL,"
            + "description VARCHAR NOT NULL"
            + ");";
    
    
    public static int insertPost(String title, String description) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertPost(title, description, c);
        }
        catch (SQLException e) 
        {
            Logger.warn(e, "Error while inserting post with title {} and description {}", title, description);
        }
        return -1;
    }
    
    public static int insertPost(String title, String description, Connection c) throws NullPointerException, SQLException
    {
        final String SQL = "INSERT INTO tbl_post (time, title, description) VALUES (?, ?, ?) RETURNING tbl_post.post_id";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            pstmt.setTimestamp(1, timestamp, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }
    
    public static int insertPost(String title, String description, List<Integer> files)
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertPost(title, description, files, c);
        }
        catch (SQLException e) 
        {
            Logger.warn(e, "Error while inserting post with title {} and description {}", title, description);
        }
        return -1;
    }
    
    public static int insertPost(String title, String description, List<Integer> files, Connection c) throws SQLException
    {
        int post_id = insertPost(title, description, c);
        
        if(post_id == -1)
        {
            Logger.warn("Failed to insert post with files because post_id was -1. Title {}, description {}, files {}", title, description, files);
            return -1;
        }
        
        for(Integer i : files)
        {
            TablePostFiles.insertAssociation(post_id, i, c);
        }
        
        return post_id;
    }
    
    
    
    public static List<Post> getPosts(int limit)
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return  getPosts(limit, c);
        }
        catch (SQLException e) 
        {
            Logger.warn(e, "Error while getting posts with limit {}", limit);
        }
        
        return new LinkedList<>();
    }
    
    public static List<Post> getPosts(int limit, Connection c) throws SQLException
    {
        List<Post> posts = new LinkedList<>();
        
        final String SQL = "";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            
        }
        
        
        return posts;
    }
}
