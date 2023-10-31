package model;

public class Team {
    private int id;
    private String tName;

    public Team(String tName) {
        this.tName = tName;
    }

    public Team() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", TeamName='" + tName + '\'' +
                '}';
    }
}
