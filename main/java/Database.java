import java.sql.*;
import java.nio.file.*;

public class Database {
    private final String url;
    public Database(String file) { this.url="jdbc:sqlite:"+file; }
    public Connection connect() throws SQLException {
        Connection c=DriverManager.getConnection(url);
        try(Statement s=c.createStatement()){ s.execute("PRAGMA foreign_keys = ON"); }
        return c;
    }
    public void initialize() throws SQLException {
        try(Connection c=connect(); Statement s=c.createStatement()){
            s.executeUpdate("CREATE TABLE IF NOT EXISTS transactions ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT NOT NULL, date TEXT NOT NULL,"+
                "amount REAL NOT NULL CHECK(amount > 0), description TEXT, type TEXT NOT NULL CHECK(type IN ('INCOME','EXPENSE')))");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS profile (id INTEGER PRIMARY KEY CHECK(id=1), first_name TEXT, last_name TEXT, address TEXT, phone TEXT, email TEXT)");
            s.executeUpdate("INSERT OR IGNORE INTO profile(id,first_name,last_name,address,phone,email) VALUES(1,'','','','','')");
        }
    }
}
