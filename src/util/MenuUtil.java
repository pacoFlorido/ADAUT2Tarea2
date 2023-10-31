package util;

import model.Login;
import model.Runner;
import model.Team;
import mysql.LoginHandleDB;
import mysql.RunnerHandleDB;
import mysql.TeamHandleDB;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuUtil {
    static Scanner sc = new Scanner(System.in);
    static LoginHandleDB loginHandleDB = new LoginHandleDB();
    static TeamHandleDB teamHandleDB = new TeamHandleDB();
    static RunnerHandleDB runnerHandleDB = new RunnerHandleDB();

    public static void showMenu(){
        System.out.println(Colores.ANSI_BLUE + "\n### BIENVENIDO A MARATONLIVE ###");
        int ejecucionLogin = menuLogin();

        if (ejecucionLogin!=0){
            menuSeleccionTabla();
        }
    }

    private static int iniciarSesion() throws SQLException {
        String username;
        String password;
        boolean iniciado = false;
        int intentos = 4;

        do {
            System.out.print(Colores.ANSI_YELLOW + "Usuario: ");
            username = sc.nextLine();
            System.out.print("Contraseña: ");
            password = sc.nextLine();
            if (loginHandleDB.exists(username,password)){
                System.out.println("Bienvenido " + username);
                iniciado = true;
            } else {
                System.out.println("Usuario o contraseña incorrecto, vuelve a intentarlo");
                intentos--;
                System.out.println("Intentos restantes " + intentos);
            }
        } while(intentos>0 && !iniciado);
        return intentos;
    }
    public static void registro() throws SQLException {
        String username;
        String password;
        boolean registrado = false;

        do {
            System.out.print(Colores.ANSI_YELLOW + "Usuario: ");
            username = sc.nextLine();
            System.out.print("Contraseña: ");
            password = sc.nextLine();
            if (!loginHandleDB.exists(username)){
                Login newLog = new Login(username, password);
                try {
                    loginHandleDB.insert(newLog);
                    registrado = true;
                    System.out.println("Bienvenido " + username);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("El usuario introducido ya existe, prueba con otro.");
            }
        } while (!registrado);
    }
    public static int menuLogin(){
        String opcionLogin;
        int intentos = 1;

        do {
            System.out.println("Presiona '1' para REGISTRARTE");
            System.out.println("Presiona '2' para INICIAR SESIÓN");
            System.out.println("Presiona '0' para SALIR");
            System.out.print("Opción: ");
            opcionLogin = sc.nextLine();

            switch (opcionLogin) {
                case "0":
                    System.out.println("Saliendo");
                    break;
                case "1":
                    try {
                        registro();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        intentos = iniciarSesion();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Opción incorrecta, vuelve a intentarlo.");
                    break;
            }
        } while (!opcionLogin.equals("1") && !opcionLogin.equals("2") && !opcionLogin.equals("0"));
        if (opcionLogin.equals("0") | intentos == 0)
            return 0;
        return 1;
    }
    public static void menuEquipo() throws SQLException {
        String tabla = "EQUIPO";
        String opcion;
        int id;
        do {
            System.out.println(Colores.ANSI_GREEN + "\n### MENU UTILIZAR TABLA " + tabla+ " ###");
            System.out.println("1-Añadir "+ tabla);
            System.out.println("2-Modificar "+ tabla);
            System.out.println("3-Eliminar "+ tabla);
            System.out.println("4-Mostrar todo el contenido de la tabla " + tabla);
            System.out.println("5-Mostrar todos los corredores de un " + tabla);
            System.out.println("6-VOLVER AL MENU ANTERIOR");
            System.out.print("Opción: ");
            opcion = sc.nextLine();

            switch (opcion){
                case "1":
                    System.out.print("Introduce el nombre del nuevo equipo: ");
                    teamHandleDB.insert(new Team(sc.nextLine()));
                    System.out.println("Equipo añadido");
                    break;
                case "2":
                    System.out.print("Introduce el id del equipo a modificar: ");
                    try {
                        id = sc.nextInt();
                        sc.nextLine();
                        if (teamHandleDB.exists(id)){
                            System.out.print("Introduce el nuevo nombre del equipo: ");
                            String name = sc.nextLine();
                            teamHandleDB.update(id,new Team(name));
                            System.out.println("Equipo actualizado");
                        } else System.out.println("Equipo con el id=" + id + " no encontrado, volviendo al menu.");
                    } catch (InputMismatchException e){
                        System.out.println("Debes introducir un id de equipo válido");
                    }
                    break;
                case "3":
                    System.out.print("Introduce el id del equipo a eliminar: ");
                    try {
                        id = sc.nextInt();
                        sc.nextLine();
                        if (teamHandleDB.exists(id)) {
                            teamHandleDB.delete(id);
                            System.out.println("Equipo eliminado");
                        } else System.out.println("Equipo con el id=" + id + " no encontrado, volviendo al menu.");
                    } catch (InputMismatchException e){
                        System.out.println("Debes introducir un id de equipo válido");
                    }
                    break;
                case "4":
                    System.out.println("#############################");
                    teamHandleDB.getTeams().forEach(System.out::println);
                    System.out.println("#############################");
                    break;
                case "5":
                    System.out.print("Introduce el nombre del equipo del cual quieres ver los corredores: ");
                    String tname = sc.nextLine();
                    if (teamHandleDB.exists(tname)){
                        System.out.println("#################################################");
                        teamHandleDB.getRunnersEquipo(tname);
                        System.out.println("#################################################");
                    } else {
                        System.out.println("Equipo no encontrado, volviendo al menu.");
                    }
                    break;
                case "6":
                    break;
                default:
                    System.out.println("Opción incorrecta, vuelve a intentarlo.");
                    break;
            }
        } while (!opcion.equals("6"));
    }
    public static void menuCorredor() throws SQLException {
        String tabla = "CORREDOR";
        int dorsal;
        String opcion;
        String name, tName;
        int finalPosition;
        do {
            System.out.println(Colores.ANSI_RED + "\n### MENU UTILIZAR TABLA " + tabla + " ###");
            System.out.println("1-Añadir "+ tabla);
            System.out.println("2-Modificar "+ tabla);
            System.out.println("3-Eliminar "+ tabla);
            System.out.println("4-Mostrar todo el contenido de la tabla " + tabla);
            System.out.println("5-VOLVER AL MENU ANTERIOR");
            System.out.print("Opción: ");
            opcion = sc.nextLine();


            switch (opcion){
                case "1":
                    System.out.print("Introduce el nombre del nuevo corredor: ");
                    name = sc.nextLine();
                    System.out.print("Introduce la posición final en la que llegó: ");
                    try {
                        finalPosition = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Introduce el nombre del equipo al cual pertenece el nuevo corredor: ");
                        tName = sc.nextLine();
                        if (teamHandleDB.exists(tName)) {
                            runnerHandleDB.insert(new Runner(name, finalPosition, teamHandleDB.getTeam(tName).getId()));
                            System.out.println(tabla + " añadido.");
                        } else {
                            System.out.println("No existe equipo con el nombre " + tName);
                            System.out.println("Volviendo al menu");
                        }
                    } catch (InputMismatchException e){
                        System.out.println("Debes introducir una posición válida. (1, 2, 3, 12...)");
                    }
                    break;
                case "2":
                    System.out.print("Introduce el dorsal del corredor a modificar: ");
                    try {
                        dorsal = sc.nextInt();
                        sc.nextLine();
                        if (runnerHandleDB.exists(dorsal)) {
                            System.out.print("Introduce el nombre del nuevo equipo del corredor: ");
                            tName = sc.nextLine();
                            if (teamHandleDB.exists(tName)) {
                                runnerHandleDB.update(dorsal, teamHandleDB.getTeam(tName).getId());
                            } else {
                                System.out.println("Nombre de equipo no encontrado");
                                System.out.println("Volviendo al menu");
                            }
                        } else {
                            System.out.println("Corredor no encontrado");
                            System.out.println("Volviendo al menu");
                        }
                    } catch (InputMismatchException e){
                        System.out.println("Debes introducir un dorsal válido(1, 2, 3, 123...)");
                    }
                    break;
                case "3":
                    System.out.print("Introduce el dorsal del corredor a eliminar: ");
                    dorsal = sc.nextInt();
                    sc.nextLine();
                    if (runnerHandleDB.exists(dorsal)){
                        runnerHandleDB.delete(dorsal);
                    } else {
                        System.out.println("Corredor no encontrado");
                        System.out.println("Volviendo al menu");
                    }
                    break;
                case "4":
                    System.out.println("#############################");
                    runnerHandleDB.getRunners().forEach(System.out::println);
                    System.out.println("#############################");
                    break;
                case "5":
                    break;
                default:
                    System.out.println("Opción incorrecta, vuelve a intentarlo.");
                    break;
            }
        } while (!opcion.equals("5"));
    }
    public static void menuSeleccionTabla(){
        String opcion;
        do {
            System.out.println(Colores.ANSI_CYAN + "\n### SELECCIONA LA TABLA A UTILIZAR ###");
            System.out.println("1-Tabla Equipos");
            System.out.println("2-Tabla Corredores");
            System.out.println("3-SALIR PROGRAMA");
            System.out.print("Opción: ");
            opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    try {
                        menuEquipo();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        menuCorredor();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    System.out.println("Saliendo");
                    break;
                default:
                    System.out.println("Opcion incorrecta, vuelve a intentarlo.");
                    break;
            }
        } while(!opcion.equals("3"));
    }
}
