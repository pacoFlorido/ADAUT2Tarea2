package mysql;

import model.Team;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamHandleDB {
    private static final Connection con = DatabaseConnection.getConn();

    public int insert(Team team) throws SQLException {
        String sql = "INSERT INTO team(t_name) VALUES (?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,team.gettName());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }
        }
        return -1;
    }
    public int update(int id, Team team){
        String sql = "UPDATE team SET t_name=? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,team.gettName());
            ps.setInt(2,id);

            ps.executeUpdate();

            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
    public int delete(int id) {
        String sql = "DELETE FROM team WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        String select = "SELECT * FROM team";

        try (Statement st = con.createStatement();
             ResultSet results = st.executeQuery(select)){

            while (results.next()){
                Team team = new Team();
                team.setId(results.getInt(1));
                team.settName(results.getString(2));
                teams.add(team);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return teams;
    }
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT * FROM team WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean exists(String name) throws SQLException {
        String sql = "SELECT * FROM team WHERE t_name=?";
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
    public Team getTeam(String name) throws SQLException {
        String sql = "SELECT * FROM team WHERE t_name = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Team t = new Team();
                t.setId(rs.getInt(1));
                t.settName(rs.getString(2));
                return t;
            }
        }
        return null;
    }
    public void getRunnersEquipo(String tname){
        String sql = "SELECT r.dorsal, r.r_name, r.final_position FROM runner r JOIN team t ON (r.idTeam=t.id) WHERE r.idTeam = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,tname);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                System.out.println(
                        "\n======================"
                        + "\nDorsal: " + rs.getInt(1)
                        + "\nNombre: " + rs.getString(2)
                        + "\nPosción final: " + rs.getInt(3)
                        +"\n======================");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
