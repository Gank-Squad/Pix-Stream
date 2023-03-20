package uwu.nyaa.owo.finalproject.data.db;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableNamespace
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_namespace";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_namespace("
            + "namespace_id serial PRIMARY KEY, "
            + "namespace VARCHAR UNIQUE NOT NULL"
            + ");";

    public static int insertOrSelectByNamespace(String namespace)
    {
        try(Connection c = DatabaseConnection.getConnection())
        {
            return insertOrSelectByNamespace(namespace, c);

        }
        catch (SQLException e)
        {
            WrappedLogger.warning(String.format("Could not insertOrSelectBySubtag for value %s", namespace), e);
        }

        return -1;
    }

    public static int insertOrSelectByNamespace(String namespace, Connection c) throws SQLException
    {
        final String SQL1 = "SELECT namespace_id FROM tbl_namespace WHERE namespace = ?";
        final String SQL2 = "INSERT INTO tbl_namespace(namespace) VALUES (?) RETURNING namespace_id";

        try (PreparedStatement pstmt = c.prepareStatement(SQL1))
        {
            pstmt.setString(1, namespace.strip().toLowerCase());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }

            try(PreparedStatement pstmt2 = c.prepareStatement(SQL2))
            {
                pstmt2.setString(1, namespace.strip().toLowerCase());

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
