package uwu.nyaa.owo.finalproject.data.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.tinylog.Logger;

public class TableSubtag
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_subtag";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_subtag("
            + "subtag_id serial PRIMARY KEY, "
            + "subtag VARCHAR UNIQUE NOT NULL"
            + ");";


    public static int insertOrSelectBySubtag(String subtag)
    {
        try(Connection c = DatabaseConnection.getConnection())
        {
            return insertOrSelectBySubtag(subtag, c);

        }
        catch (SQLException e)
        {
            Logger.warn(e,String.format("Could not insertOrSelectBySubtag for value %s", subtag));
        }

        return -1;
    }

    public static int insertOrSelectBySubtag(String subtag, Connection c) throws SQLException
    {
        int subtagId = getSubtagID(subtag, c);

        if(subtagId != -1)
            return subtagId;

        final String SQL = "INSERT INTO tbl_subtag(subtag) VALUES (?) RETURNING subtag_id";

        try(PreparedStatement pstmt2 = c.prepareStatement(SQL))
        {
            pstmt2.setString(1, subtag.strip().toLowerCase());

            ResultSet rs = pstmt2.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return 1;
    }



    /**
     * Gets the id for the given subtag or -1
     * @param subtag The subtag to get the id
     * @return The id of the subtag or -1 if errors
     * @throws NullPointerException If the subtag or connection are null
     */
    public static int getSubtagID(String subtag) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return getSubtagID(subtag, c);
        }
        catch (SQLException e)
        {
            Logger.warn(e,String.format("SQL Exception searching for subtag: %s", subtag));
        }

        return -1;
    }

    /**
     * Gets the id for the given subtag or -1
     * @param subtag The subtag to search
     * @param c The database connection
     * @return The subtag id or -1 for error / not found
     * @throws SQLException
     * @throws NullPointerException If the subtag or connection are null
     */
    public static int getSubtagID(String subtag, Connection c) throws SQLException, NullPointerException
    {
        final String SQL = "SELECT subtag_id FROM tbl_subtag WHERE tbl_subtag.subtag = ?";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setString(1, subtag.strip().toLowerCase());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }
}
