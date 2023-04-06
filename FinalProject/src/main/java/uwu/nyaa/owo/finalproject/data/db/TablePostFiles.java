package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.tinylog.Logger;

public class TablePostFiles
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_post_files";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_post_files("
            + "post_id serial NOT NULL, "
            + "hash_id serial NOT NULL, "
            + "CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES tbl_post(post_id),"
            + "CONSTRAINT fk_hash_id FOREIGN KEY(hash_id) REFERENCES tbl_hash(hash_id),"
            + "PRIMARY KEY(post_id, hash_id)"
            + ");";
    
    
    public static void insertAssociation(int post_id, int hash_id)
    {
        final String SQL = "INSERT INTO tbl_post_files(post_id, hash_id) VALUES (?, ?)";

        try (Connection c = DatabaseConnection.getConnection())
        {
            insertAssociation(post_id, hash_id, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e, "Error adding post-hash association {}-{}", post_id, hash_id);
        }
    }
    
    public static void insertAssociation(int post_id, int hash_id, Connection c) throws SQLException
    {
        final String SQL = "INSERT INTO tbl_post_files(post_id, hash_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, post_id);
            pstmt.setInt(2, hash_id);
            pstmt.execute();
        }
    }
}
