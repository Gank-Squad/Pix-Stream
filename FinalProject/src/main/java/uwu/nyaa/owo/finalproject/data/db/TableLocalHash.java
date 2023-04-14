package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.tinylog.Logger;

/**
 * Stores extra hash information about a file 
 * @author minno
 *
 */
public class TableLocalHash
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_local_hash";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_local_hash("
            + "hash_id serial PRIMARY KEY, "
            + "md5 bytea UNIQUE NOT NULL,"
            + "sha1 bytea UNIQUE NOT NULL,"
            + "phash bytea  UNIQUE,"
            + "CONSTRAINT fk_hash_id FOREIGN KEY(hash_id) REFERENCES tbl_hash(hash_id)"
            + ");";
    
    
    /**
     * Inserts the given hashes with the associated hash_id
     * @param hashID The hash_id with the associated hash
     * @param sha1 The SHA256 file hash
     * @param md5 The MD5 file hash
     * @param phash The PHash, optionally
     * @return
     */
    public static boolean insertHashes(int hashID, byte[] sha1, byte[] md5, byte[] phash)
    {
        
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertHashes(hashID, sha1, md5, phash, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, String.format("Error inserting hashes with ID %d", hashID));
        }
        
        return false;
    }
    
    public static boolean insertHashes(int hashID, byte[] sha1, byte[] md5, byte[] phash, Connection c) throws SQLException
    {
        final String SQL = "INSERT INTO tbl_local_hash(hash_id, md5, sha1, phash) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, hashID);
            pstmt.setBytes(2, md5);
            pstmt.setBytes(3, sha1);
            pstmt.setBytes(4, phash);
            pstmt.execute();
        }
      
        return true;
    }
}
