package uwu.nyaa.owo.finalproject.data.db;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            WrappedLogger.warning(String.format("Could not insertOrSelectBySubtag for value %s", subtag), e);
        }

        return -1;
    }

    public static int insertOrSelectBySubtag(String subtag, Connection c) throws SQLException
    {
        final String SQL1 = "SELECT subtag_id FROM tbl_subtag WHERE subtag = ?";
        final String SQL2 = "INSERT INTO tbl_subtag(subtag) VALUES (?) RETURNING subtag_id";

        try (PreparedStatement pstmt = c.prepareStatement(SQL1))
        {
            pstmt.setString(1, subtag.strip().toLowerCase());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }

            try(PreparedStatement pstmt2 = c.prepareStatement(SQL2))
            {
                pstmt2.setString(1, subtag.strip().toLowerCase());

                rs = pstmt2.executeQuery();

                if(rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }

        return 1;
    }
}
