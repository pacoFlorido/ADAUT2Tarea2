package mysql;

import model.Login;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginHandleDB {
    private static final Connection con = DatabaseConnection.getConn();

    public int insert(Login login) throws SQLException, NullPointerException {
        String sql = "INSERT INTO login(username,pass) VALUES (?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,login.getUsername());
            ps.setString(2,login.getPassword());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }
        }
        return -1;
    }
    public boolean exists(String name) throws SQLException {
        String sql = "SELECT * FROM login WHERE username=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,name);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean exists(String username, String password) throws SQLException {
        String sql = "SELECT * FROM login WHERE username=? AND pass=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,username);
            ps.setString(2,password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return true;
            } else {
                return false;
            }
        }

    }


    public int delete(String username){
        String sql = "DELETE FROM login WHERE username = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,username);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public List<Login> getLogins() {
        List<Login> logins = new ArrayList<>();
        String select = "SELECT * FROM login";

        try (Statement st = con.createStatement();
             ResultSet results = st.executeQuery(select)){

            while (results.next()){
                Login login = new Login();
                login.setUsername(results.getString(2));
                login.setPassword(results.getString(3));
                logins.add(login);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return logins;
    }

}
