package uwu.nyaa.owo.finalproject.data.db;

import uwu.nyaa.owo.finalproject.data.StringHelper;
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


    /**
     * Inserts the given tag and returns it's new id, returns -1 if error / exists
     * @param fullTag The tag to insert
     * @return The new tag id or -1
     * @throws NullPointerException if fulltag or db connection is null
     */
    public static int insertTag(String fullTag) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return insertTag(fullTag, c);
        }
        catch (SQLException e)
        {
            WrappedLogger.warning(String.format("Error inserting tag with value %s", fullTag), e);
        }

        return -1;
    }

    /**
     * Inserts the given tag and returns it's new id, returns -1 if error / exists
     * @param fullTag The tag to insert
     * @param c The database connection
     * @return The new tag id or -1
     * @throws NullPointerException if fulltag or db connection is null
     */
    public static int insertTag(String fullTag, Connection c) throws SQLException, NullPointerException
    {
        String[] tag = StringHelper.partitionTag(fullTag);

        int namespace_id = TableNamespace.insertOrSelectByNamespace(tag[0], c);
        int subtag_id = TableSubtag.insertOrSelectBySubtag(tag[1], c);

        final String SQL = "INSERT INTO tbl_tag(namespace_id, subtag_id) VALUES (?, ?) RETURNING tag_id";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, namespace_id);
            pstmt.setInt(2, subtag_id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }


    /**
     * gets the id for the given tag or -1
     * @param fulltag The tag to search
     * @return The tag id or -1
     * @throws NullPointerException if the tag is null
     */
    public static int getTagID(String fulltag) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return getTagID(fulltag, c);
        }
        catch (SQLException e)
        {
            WrappedLogger.warning(String.format("SQL Exception getting tag id for %s", fulltag), e);
        }

        return -1;
    }

    /**
     * Gets the tag id of the given tag or -1
     * @param fulltag The tag to search
     * @param c The database connection
     * @return The tag id or -1
     * @throws SQLException
     * @throws NullPointerException If the tag or db connection is null
     */
    public static int getTagID(String fulltag, Connection c) throws SQLException, NullPointerException
    {
        // TODO: make this use 1 query instead of 3

        String[] tag = StringHelper.partitionTag(fulltag);

        int namespaceID = TableNamespace.getNamespaceID(tag[0], c);
        int subtagID = TableSubtag.getSubtagID(tag[1], c);

        if(namespaceID == -1 || subtagID == -1)
            return -1;

        final String SQL = "SELECT tag_id FROM tbl_tag WHERE namespace_id = ? AND subtag_id = ?";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setInt(1, namespaceID);
            pstmt.setInt(2, subtagID);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }


    /**
     * Gets as many tags as the limit allows
     * @param limit The number of tags to get
     * @return A list of tags or en empty list
     */
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
