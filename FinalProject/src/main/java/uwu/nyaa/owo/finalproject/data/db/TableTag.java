package uwu.nyaa.owo.finalproject.data.db;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TableTag
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_tag";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_tag("
            + "tag_id serial PRIMARY KEY, "
            + "subtag_id serial NOT NULL,"
            + "namespace_id serial NOT NULL,"
            + "UNIQUE (subtag_id, namespace_id),"
            + "CONSTRAINT fk_subtag_id FOREIGN KEY(subtag_id) REFERENCES tbl_subtag(subtag_id),"
            + "CONSTRAINT fk_namespace_id FOREIGN KEY(namespace_id) REFERENCES tbl_namespace(namespace_id)"
            + ");";


    public static int insertTag(String fullTag)
    {
        String[] split = fullTag.split(":", 2);
        String namespace;
        String subtag;

        if(split.length == 1)
        {
            namespace = "";
            subtag = split[0].strip().toLowerCase();
        }
        else
        {
            namespace = split[0].strip().toLowerCase();
            subtag = split[1].strip().toLowerCase();
        }

        final String SQL = "INSERT INTO tbl_tag(namespace_id, subtag_id) VALUES (?, ?) RETURNING tag_id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            int namespace_id = TableNamespace.insertOrSelectByNamespace(namespace, c);
            int subtag_id = TableSubtag.insertOrSelectBySubtag(subtag, c);

            pstmt.setInt(1, namespace_id);
            pstmt.setInt(2, subtag_id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            WrappedLogger.warning(String.format("Error inserting tag with value %s", fullTag), e);
        }

        return -1;
    }



    public static List<FullTag> getTags(int limit)
    {
        LinkedList<FullTag> items = new LinkedList<>();

        final String SQL = "SELECT tbl_tag.tag_id, tbl_tag.namespace_id, tbl_tag.subtag_id, tbl_namespace.namespace, tbl_subtag.subtag " +
                "FROM tbl_tag " +
                "JOIN tbl_namespace ON tbl_tag.namespace_id = tbl_namespace.namespace_id " +
                "JOIN tbl_subtag ON tbl_tag.subtag_id = tbl_subtag.subtag_id " +
                "LIMIT ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement pstmt = c.prepareStatement(SQL))
        {

            pstmt.setInt(1, limit);
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
            return items;
        }
        catch (SQLException e)
        {
            WrappedLogger.warning("Error searching for files", e);
        }

        return items;
    }


}
