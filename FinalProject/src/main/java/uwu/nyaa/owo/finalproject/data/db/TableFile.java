package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

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
        final String SQL = "INSERT INTO tbl_file(hash_id, size, mime, width, height, duration, has_audio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection c = DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, hashID);
            pstmt.setLong(2, size);
            pstmt.setInt(3, mime);
            pstmt.setInt(4, width);
            pstmt.setInt(5, height);
            pstmt.setInt(6, duration);
            pstmt.setBoolean(7, has_audio);
            pstmt.execute();
            return true;
        }
        catch (SQLException e)
        {
            WrappedLogger.warning(String.format("Error inserting file information with ID %d", hashID), e);
        }
        
        return false;
    }
}
