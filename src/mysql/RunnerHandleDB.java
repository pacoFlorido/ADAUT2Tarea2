package mysql;

import model.Runner;
import model.Team;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RunnerHandleDB {
    private static final Connection con = DatabaseConnection.getConn();

    public int insert(Runner runner) throws SQLException {
        String sql = "INSERT INTO runner(r_name,final_position,idTeam) VALUES (?,?,?)";
        String selectD = "SELECT id FROM team WHERE t_name=?";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,runner.getrName());
            ps.setInt(2,runner.getFinalPosition());
            ps.setInt(3,runner.getIdTeam());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }
        }
        return -1;
    }
    public boolean exists(int dorsal) throws SQLException {
        String sql = "SELECT * FROM runner WHERE dorsal = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1,dorsal);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return true;
            } else {
                return false;
            }
        }
    }
    public int update(int dorsal, int idTeam){
        String sql = "UPDATE runner SET idTeam=? WHERE dorsal = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,dorsal);
            ps.setInt(2,idTeam);

            ps.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
    public int delete(int dorsal) {
        String sql = "DELETE FROM runner WHERE dorsal = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,dorsal);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public List<Runner> getRunners() {
        List<Runner> runners = new ArrayList<>();
        String select = "SELECT * FROM runner";

        try (Statement st = con.createStatement();
             ResultSet results = st.executeQuery(select)){

            while (results.next()){
                Runner r = new Runner();
                r.setDorsal(results.getInt(1));
                r.setrName(results.getString(2));
                r.setFinalPosition(results.getInt(3));
                r.setIdTeam(4);
                runners.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return runners;
    }
}
