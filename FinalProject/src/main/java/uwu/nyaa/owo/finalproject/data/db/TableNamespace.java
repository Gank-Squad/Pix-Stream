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


    /**
     * gets the id for a given namespace or creates the namespace and gets the id
     * @param namespace The namespace to get / create
     * @return The id of an existing or new namespace or -1 for errors
     * @throws NullPointerException If the namespace or connection are null
     */
    public static int insertOrSelectByNamespace(String namespace) throws NullPointerException
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

    /**
     * gets the id for a given namespace or creates the namespace and gets the id
     * @param namespace The namespace to get / create
     * @param c The database connection to use
     * @return The id of an existing or new namespace or -1 for errors
     * @throws SQLException if any of the queries or db stuff goes wrong
     * @throws NullPointerException If the namespace or connection are null
     */
    public static int insertOrSelectByNamespace(String namespace, Connection c) throws SQLException, NullPointerException
    {
        int namespaceId = getNamespaceID(namespace, c);

        if(namespaceId != -1)
            return namespaceId;

        final String SQL = "INSERT INTO tbl_namespace(namespace) VALUES (?) RETURNING namespace_id";

        try(PreparedStatement pstmt2 = c.prepareStatement(SQL))
        {
            pstmt2.setString(1, namespace.strip().toLowerCase());

            ResultSet rs = pstmt2.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }


    /**
     * Gets the id for the given namespace or -1
     * @param namespace The namespace to get the id
     * @return The id of the namespace or -1 if errors
     * @throws NullPointerException If the namespace or connection are null
     */
    public static int getNamespaceID(String namespace) throws NullPointerException
    {
        try (Connection c = DatabaseConnection.getConnection())
        {
            return getNamespaceID(namespace, c);
        }
        catch (SQLException e)
        {
            WrappedLogger.warning(String.format("SQL Exception searching for namespace: %s", namespace), e);
        }

        return -1;
    }

    /**
     * Gets the id for the given namespace or -1
     * @param namespace The namespace to search
     * @param c The database connection
     * @return The namespace id or -1 for error / not found
     * @throws SQLException
     * @throws NullPointerException If the namespace or connection are null
     */
    public static int getNamespaceID(String namespace, Connection c) throws SQLException, NullPointerException
    {
        final String SQL = "SELECT namespace_id FROM tbl_namespace WHERE tbl_namespace.namespace = ?";

        try (PreparedStatement pstmt = c.prepareStatement(SQL))
        {
            pstmt.setString(1, namespace.strip().toLowerCase());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }

        return -1;
    }
}
