package mysql;

import util.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneralHandleDB {
    private static final Connection con = DatabaseConnection.getConn();

    public int deleteAllFromDb() throws SQLException {
        String sql = "{call deleteData()}";
        CallableStatement stmt = con.prepareCall(sql);
        stmt.execute();
        return -1;
    }
}
