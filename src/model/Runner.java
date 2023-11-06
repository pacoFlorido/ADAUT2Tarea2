package model;

public class Runner {
    private int dorsal;
    private String rName;
    private int finalPosition, idTeam;

    public Runner(String tName, int finalPosition, int idTeam) {
        this.rName = tName;
        this.finalPosition = finalPosition;
        this.idTeam = idTeam;
    }

    public Runner() {
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    @Override
    public String toString() {
        return "Runner{" +
                "dorsal=" + dorsal +
                ", rName='" + rName + '\'' +
                ", finalPosition=" + finalPosition +
                ", idTeam=" + this.idTeam + '}';
    }
}
