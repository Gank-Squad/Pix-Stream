package uwu.nyaa.owo.finalproject.data.db;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TableHashTag
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_hash_tag";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_hash_tag("
            + "hash_id serial NOT NULL, "
            + "tag_id serial NOT NULL, "
            + "CONSTRAINT fk_hash_id FOREIGN KEY(hash_id) REFERENCES tbl_hash(hash_id),"
            + "CONSTRAINT fk_tag_id FOREIGN KEY(tag_id) REFERENCES tbl_tag(tag_id),"
            + "PRIMARY KEY(hash_id, tag_id)"
            + ");";


    public static List<FullTag> getTags(int hash_id)
    {
        try(Connection c = DatabaseConnection.getConnection())
        {
            return getTags(hash_id, c);
        }
        catch (SQLException e)
        {
            WrappedLogger.warning("Exception while getting tags", e);
        }
        return new LinkedList<>();
    }
    public static List<FullTag> getTags(int hash_id, Connection c) throws SQLException
    {
        final String SQL = "SELECT tbl_tag.tag_id, tbl_tag.namespace_id, tbl_tag.subtag_id, tbl_namespace.namespace, tbl_subtag.subtag FROM tbl_hash_tag JOIN tbl_tag ON tbl_hash_tag.tag_id = tbl_tag.tag_id JOIN tbl_namespace ON tbl_tag.namespace_id = tbl_namespace.namespace_id JOIN tbl_subtag ON tbl_tag.subtag_id = tbl_subtag.subtag_id WHERE tbl_hash_tag.hash_id = ?";

        LinkedList<FullTag> items = new LinkedList<>();

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, hash_id);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next())
            {
                FullTag a = new FullTag();

                a.tag_id = rs.getInt(1);
                a.namespace_id = rs.getInt(2);
                a.subtag_id = rs.getInt(3);
                a.namespace = rs.getString(4);
                a.subtag = rs.getString(5);

                items.add(a);
            }
        }

        return items;
    }


    public static List<HashInfo> getFiles(int tag_id, int limit, boolean includeTags)
    {
        try(Connection c = DatabaseConnection.getConnection())
        {
            return getFiles(tag_id, limit, includeTags, c);
        }
        catch (SQLException e)
        {
            WrappedLogger.warning("Exception while getting tags", e);
        }
        return new LinkedList<>();
    }

    public static List<HashInfo> getFiles(int tag_id, int limit, boolean includeTags, Connection c) throws SQLException
    {
        final String SQL = "SELECT tbl_hash.hash_id, tbl_hash.hash, tbl_file.mime, tbl_file.size, tbl_file.width, tbl_file.height, tbl_file.duration, tbl_file.has_audio FROM tbl_hash_tag JOIN tbl_file ON tbl_hash_tag.hash_id = tbl_file.hash_id JOIN tbl_hash ON tbl_hash_tag.hash_id = tbl_hash.hash_id WHERE tbl_hash_tag.tag_id = ? LIMIT ?";

        LinkedList<HashInfo> items = new LinkedList<>();

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, tag_id);
            pstmt.setInt(2, limit);

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


    public static void insertAssociation(int hash_id, int tag_id)
    {
        final String SQL = "INSERT INTO tbl_hash_tag(hash_id, tag_id) VALUES (?, ?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, hash_id);
            pstmt.setInt(2, tag_id);
            pstmt.execute();
        }
        catch (SQLException e)
        {
            WrappedLogger.warning("Error adding hash-tag association", e);
        }
    }
}
