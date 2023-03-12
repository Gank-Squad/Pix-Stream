package uwu.nyaa.owo.finalproject.data.db;

public class TableUsers
{
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_users";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_users("
            + "user_id serial PRIMARY KEY, "
            + "username VARCHAR UNIQUE NOT NULL, "
            + "password bytea NOT NULL,"
            + "is_admin boolean NOT NULL, "
            + "is_disabled boolean NOT NULL"
            + ");";
}
