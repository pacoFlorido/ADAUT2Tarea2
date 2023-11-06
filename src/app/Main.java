package app;

import util.DatabaseConnection;
import util.MenuUtil;

public class Main {
    public static void main(String[] args) {

        if (DatabaseConnection.getConn()!=null){
            MenuUtil.showMenu();
        }
    }
}
