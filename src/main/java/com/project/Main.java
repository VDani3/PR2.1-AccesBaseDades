package com.project;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static int maxId = 0;

    public static void main(String[] args) throws SQLException {
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "database.db";
        String menu = "\n\nMenu: \n  1)Mostrar una taula \n  2)Mostrar personatges per faccio \n  3)Millor atacant \n  4)Millor defensor \n  5)Sortir \nOpcio: ";
        String opt;
        String opt2;
        boolean exit = false;
        Scanner sc = new Scanner(System.in);
        SQLFunctionManager sqlManager = new SQLFunctionManager();

        ResultSet rs = null;

        // Si no hi ha l'arxiu creat, el crea i li posa dades
        File fDatabase = new File(filePath);
        if (!fDatabase.exists()) {
            sqlManager.initDatabase(filePath);
        }

        // Connectar (crea la BBDD si no existeix)
        Connection conn = UtilsSQLite.connect(filePath);

        // Menu
        while (!exit) {
            limpiarConsola();
            System.out.print(menu);
            opt = sc.nextLine();

            // Mostrar tots els personatges
            if (opt.equals("1")) {
                System.out.println(sqlManager.getTables(conn));
                opt2 = sc.nextLine();

                try{
                    System.out.print(sqlManager.getTable(conn, Integer.parseInt(opt2)));
                } catch (Exception e){}

                sc.nextLine();
                limpiarConsola();

            // Mostrar personatges per faccio
            } else if (opt.equals("2")) {
                System.out.print(sqlManager.getFaccions(conn));
                opt2 = sc.nextLine();
                try {
                    System.out.println(sqlManager.getFaccioCharacters(conn, Integer.parseInt(opt2))); 
                    sc.nextLine();
                    limpiarConsola();
                    
                } catch (Exception e) {}

            // Mostrar personatges per faccio
            } else if (opt.equals("3")) {
                System.out.print(sqlManager.getFaccions(conn));
                opt2 = sc.nextLine();
                try {
                    System.out.println(sqlManager.getBestCharacter(conn, Integer.parseInt(opt2), "atac"));
                    sc.nextLine();
                    limpiarConsola();
                } catch (Exception e) {}

            // Mostrar millor atacant x faccio
            } else if (opt.equals("4")) {
                System.out.print(sqlManager.getFaccions(conn));
                opt2 = sc.nextLine();
                try {
                    System.out.println(sqlManager.getBestCharacter(conn, Integer.parseInt(opt2), "defensa"));
                    sc.nextLine();
                    limpiarConsola();
                } catch (Exception e) {}

            // Mostrar millor defensor de x faccio
            } else if (opt.equals("5")) {
                exit = true;
            }
        }

        // Desconnectar
        UtilsSQLite.disconnect(conn);
    }

    

    public static void limpiarConsola() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Para Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Para Unix/Linux/MacOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {}
    } 
}
