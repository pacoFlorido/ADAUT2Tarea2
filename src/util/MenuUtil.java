package util;

import model.Login;
import model.Runner;
import model.Team;
import mysql.GeneralHandleDB;
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
    static GeneralHandleDB generalHandleDB = new GeneralHandleDB();

    public static void showMenu(){
        System.out.println(Colores.ANSI_BLUE + "\n### BIENVENIDO A MARATONLIVE ###");
        int ejecucionLogin = menuLogin();

        if (ejecucionLogin!=0){
            menuSeleccionTabla();
        }
    }

    public static void menuSeleccionTabla(){
        String opcion;
        do {
            System.out.println(Colores.ANSI_CYAN + "\n### SELECCIONA LA TABLA A UTILIZAR O CREA UN CORREDOR Y UN EQUIPO NUEVO ###");
            System.out.println("\t1-Tabla Equipos");
            System.out.println("\t2-Tabla Corredores");
            System.out.println("\t3-Crear un equipo y un corredor perteneciente a ese equipo");
            System.out.println("\t4-Eliminar todos los datos de la Base de Datos");
            System.out.println("\t5-SALIR PROGRAMA");
            System.out.print("Opción: ");
            opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    menuEquipo();
                    break;
                case "2":
                    menuCorredor();
                    break;
                case "3":
                    System.out.print("Introduce el nombre del nuevo Equipo:");
                    String tName = sc.nextLine();
                    System.out.print("Introduce el nombre del nuevo corredor que pertenecerá al equipo que acabas de crear:");
                    String rName = sc.nextLine();
                    System.out.print("Introduce la posición final en la que quedó el corredor:");
                    int finalPos;
                    try{
                        finalPos = sc.nextInt();
                        sc.nextLine();
                        runnerHandleDB.insertRunnerWithNewTeam(tName,rName,finalPos);
                    } catch (InputMismatchException e){
                        sc.nextLine();
                        System.out.println("Error introduciendo la posición del corredor, cancelando inserción");
                    } catch (SQLException e) {
                        System.out.println("Error insertando el equipo o el corredor");
                    }
                    break;
                case "4":
                    System.out.println("Eliminando datos de la base de datos");
                    try {
                        generalHandleDB.deleteAllFromDb();
                        System.out.println("Eliminación completada");
                    } catch (SQLException e) {
                        System.out.println("Error eliminando todos los datos de la base de datos");
                    }
                    break;
                case "5":
                    break;
                default:
                    System.out.println("Opcion incorrecta, vuelve a intentarlo.");
                    break;
            }
        } while(!opcion.equals("5"));
    }
    private static int iniciarSesion() throws SQLException {
        String username;
        String password;
        boolean iniciado = false;
        int intentos = 4;

        do {
            System.out.print(Colores.ANSI_YELLOW + "\n\tUsuario: ");
            username = sc.nextLine();
            System.out.print("\tContraseña: ");
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
            System.out.print(Colores.ANSI_YELLOW + "\n\tUsuario: ");
            username = sc.nextLine();
            System.out.print("\tContraseña: ");
            password = sc.nextLine();
            if (!loginHandleDB.exists(username)){
                Login newLog = new Login(username, password);
                try {
                    loginHandleDB.insert(newLog);
                    registrado = true;
                    System.out.println("Bienvenido " + username);
                } catch (SQLException e) {
                    System.out.println("Error en la inserción de ");;
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
                        System.out.println("Error en el registro");
                    }
                    break;
                case "2":
                    try {
                        intentos = iniciarSesion();
                    } catch (SQLException e) {
                        System.out.println("Error en el inicio de sesión");
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
    public static void menuEquipo() {
        String tabla = "EQUIPO";
        String opcion;
        int id;
        do {
            System.out.println(Colores.ANSI_GREEN + "\n### MENU UTILIZAR TABLA " + tabla+ " ###");
            System.out.println("\t1-Añadir "+ tabla);
            System.out.println("\t2-Modificar "+ tabla);
            System.out.println("\t3-Eliminar "+ tabla);
            System.out.println("\t4-Mostrar todo el contenido de la tabla " + tabla);
            System.out.println("\t5-Mostrar todos los corredores de un " + tabla);
            System.out.println("\t6-Mostrar todos los EQUIPOS con 5 o más corredores");
            System.out.println("\t7-VOLVER AL MENU ANTERIOR");
            System.out.print("Opción: ");
            opcion = sc.nextLine();

            switch (opcion){
                case "1":
                    System.out.print("Introduce el nombre del nuevo equipo: ");
                    try{
                        teamHandleDB.insert(new Team(sc.nextLine()));
                        System.out.println("Equipo añadido");
                    } catch (SQLException e){
                        System.out.println("Error añadiendo el equipo.");
                    }
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
                    } catch (SQLException e) {
                        System.out.println("Error actualizando Equipo.");
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
                    } catch (SQLException e) {
                        System.out.println("Error eliminando equipo.");
                    }
                    break;
                case "4":
                    System.out.println(Colores.ANSI_PURPLE + "#############################");
                    teamHandleDB.getTeams().forEach(System.out::println);
                    System.out.println("#############################");
                    break;
                case "5":
                    System.out.print("Introduce el nombre del equipo del cual quieres ver los corredores: ");
                    String tname = sc.nextLine();
                    try {
                        if (teamHandleDB.exists(tname)){
                            System.out.println(Colores.ANSI_PURPLE + "#################################################");
                            teamHandleDB.getRunnersEquipo(tname);
                            System.out.println("#################################################");
                        } else {
                            System.out.println("Equipo no encontrado, volviendo al menu.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error mostrando los corredores de un equipo.");
                    }
                    break;
                case "6":
                    System.out.println(Colores.ANSI_PURPLE + "############################################");
                    teamHandleDB.getTeamsWithMoreThanFiveRunners();
                    System.out.println("############################################");
                    break;
                case "7":
                    break;
                default:
                    System.out.println("Opción incorrecta, vuelve a intentarlo.");
                    break;
            }
        } while (!opcion.equals("7"));
    }
    public static void menuCorredor(){
        String tabla = "CORREDOR";
        int dorsal;
        String opcion;
        String name, tName;
        int finalPosition;
        do {
            System.out.println(Colores.ANSI_RED + "\n### MENU UTILIZAR TABLA " + tabla + " ###");
            System.out.println("\t1-Añadir "+ tabla);
            System.out.println("\t2-Modificar "+ tabla);
            System.out.println("\t3-Eliminar "+ tabla);
            System.out.println("\t4-Mostrar todo el contenido de la tabla " + tabla);
            System.out.println("\t5-VOLVER AL MENU ANTERIOR");
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
                    } catch (SQLException e) {
                        System.out.println("Error añadiendo corredor.");
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
                    } catch (SQLException e) {
                        System.out.println("Error modificando corredor.");
                    }
                    break;
                case "3":
                    System.out.print("Introduce el dorsal del corredor a eliminar: ");
                    dorsal = sc.nextInt();
                    sc.nextLine();
                    try {
                        if (runnerHandleDB.exists(dorsal)){
                            runnerHandleDB.delete(dorsal);
                        } else {
                            System.out.println("Corredor no encontrado");
                            System.out.println("Volviendo al menu");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error eliminando corredor.");
                    }
                    break;
                case "4":
                    System.out.println(Colores.ANSI_PURPLE + "#############################");
                    if (runnerHandleDB.getRunners().size() > 0){
                        runnerHandleDB.getRunners().forEach(System.out::println);
                    } else {
                        System.out.println("Todavía no hay corredores");
                    }
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
}