package mysql;

import model.Runner;
import model.Team;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamHandleDB {
    private static final Connection con = DatabaseConnection.getConn();

    public List<Team> getTeamsWithMoreThanFiveRunners(){
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT t.id, t.t_name, count(r.idTeam) FROM runner r JOIN team t ON (t.id = r.idTeam) GROUP BY r.idTeam HAVING count(r.idTeam) >= 5";

        try (Statement st = con.createStatement();
             ResultSet results = st.executeQuery(sql)){

            while (results.next()){
                Team team = new Team();
                team.setId(results.getInt(1));
                team.settName(results.getString(2));
                int cantidad = results.getInt(3);
                System.out.println("Corredores: " + cantidad + ", " + team);
                teams.add(team);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return teams;
    }

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
        String sql = "SELECT r.dorsal, r.r_name, r.final_position, r.idTeam FROM runner r JOIN team t ON (r.idTeam=t.id) WHERE t.t_name = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,tname);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Runner r = new Runner();
                        r.setDorsal(rs.getInt(1));
                        r.setrName(rs.getString(2));
                        r.setFinalPosition(rs.getInt(3));
                        r.setIdTeam(rs.getInt(4));
                System.out.println(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}