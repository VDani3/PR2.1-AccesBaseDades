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

        ResultSet rs = null;

        // Si no hi ha l'arxiu creat, el crea i li posa dades
        File fDatabase = new File(filePath);
        if (!fDatabase.exists()) {
            initDatabase(filePath);
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
                ArrayList<String> taules = UtilsSQLite.listTables(conn);
                System.out.println("Taules a la base: "+taules);
                sc.nextLine();
                limpiarConsola();

            // Mostrar personatges per faccio
            } else if (opt.equals("2")) {
                System.out.print(getFaccions(conn));
                opt2 = sc.nextLine();
                try {
                    System.out.println(getFaccioCharacters(conn, Integer.parseInt(opt2))); 
                    sc.nextLine();
                    limpiarConsola();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }

            // Mostrar personatges per faccio
            } else if (opt.equals("3")) {
                System.out.print(getFaccions(conn));
                opt2 = sc.nextLine();
                try {
                    System.out.println(getBestCharacter(conn, Integer.parseInt(opt2), "atac"));
                    sc.nextLine();
                    limpiarConsola();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            // Mostrar millor atacant x faccio
            } else if (opt.equals("4")) {
                System.out.print(getFaccions(conn));
                opt2 = sc.nextLine();
                try {
                    System.out.println(getBestCharacter(conn, Integer.parseInt(opt2), "defensa"));
                    sc.nextLine();
                    limpiarConsola();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            // Mostrar millor defensor de x faccio
            } else if (opt.equals("5")) {
                exit = true;
            }
        }

        // Desconnectar
        UtilsSQLite.disconnect(conn);
    }

    static void initDatabase(String filePath) {
        // Connectar (crea la BBDD si no existeix)
        Connection conn = UtilsSQLite.connect(filePath);

        // Esborrar la taula (per si existeix)
        UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Faccio;");
        UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Personatge;");

        // Crear una nova taula
        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Faccio ("
                + "	id integer PRIMARY KEY AUTOINCREMENT,"
                + "	nom text VARCHAR(15),"
                + " resum text VARCHAR(500));");

        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Personatge ("
                + "	id integer PRIMARY KEY AUTOINCREMENT,"
                + "	nom text VARCHAR(15),"
                + "	atac REAL,"
                + "	defensa REAL,"
                + " idFaccio integer,"
                + " FOREIGN KEY (idFaccio) REFERENCES Faccio(id));");

        // Afegir elements a una taula
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Cavallers','The Knights are one of the four playable factions in For Honor. It is their belief that many, if not all of the ancient ruins were constructed by their ancestors. The Knights had been scattered for centuries but have begun to reunite under a single banner, that of the Iron Legion. There are those still, however, who choose to gather their own ''Legion'', and their alliance with the Iron Legion is shaky at best.  ');");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Vikings', 'The Vikings are the undisputed power of the rivers and seas. In their pursuit to gain the approval of their gods in Valhalla, the Vikings live for battle and glory as they seek out riches and new land, destroying everything in their path to gain it.');");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Samurais', 'After having been driven from their ancestral homes and rebuilding their forces, though strong and mighty, the Samurai still find themselves vastly outnumbered by their new neighbors. Because of this, they know that only through greater cunning, skill and loyalty to each other will their people have a chance to thrive. They may very well be the last of their kind.');");


        //Agafem els id de les taules
        int idCavallers = getFaccioId(conn, "Cavallers");
        int idVikings = getFaccioId(conn, "Vikings");
        int idSamurais = getFaccioId(conn, "Samurais");
        maxId = 3;

        //Cavallers
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warden\", 120, 130, %s)", idCavallers));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warmonger\", 130, 130, %s)", idCavallers));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Peacekeeper\", 120, 120, %s)", idCavallers));
        //Vikings
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Valkyrie\", 120, 120, %s)", idVikings));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Berserker\", 130, 140, %s)", idVikings));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Jormungandr\", 130, 140, %s)", idVikings));
        //Samurais
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Shinobi\", 120, 135, %s)", idSamurais));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Aramusha\", 120, 120, %s)", idSamurais));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Kyoshin\", 120, 120, %s)", idSamurais));

        // Desconnectar
        UtilsSQLite.disconnect(conn);
    }

    public static int getFaccioId(Connection conn, String nombreFaccion) {
        int idFaccion = -1; //Per si no es troba (ho trobará si o si, pero bueno)

        try {
            // Obtener el ID de la fracción por nombre
            ResultSet resultSet = UtilsSQLite.querySelect(conn, "SELECT id FROM Faccio WHERE nom = '" + nombreFaccion + "';");

            if (resultSet.next()) {
                idFaccion = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idFaccion;
    }

    public static String getFaccions(Connection conn) {
        String res = "\nFraccio:\n";

        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "Select * FROM Faccio;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                res += String.format("  %s) %s \n", id, name);
            }
            res += "Opcio: ";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String getFaccioCharacters(Connection conn, int id) {
        String res = "\nPersonatges de la Faccio" + id + ":\n";
        res += "  Nom      Atac   Defensa\n";
        res += "--------------------------------\n";
    
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT * FROM Personatge WHERE idFaccio = " + id + ";");
            while (rs.next()) {
                res += String.format("  %s   %s   %s\n", rs.getString("nom"), rs.getDouble("atac"), rs.getDouble("defensa"));
            }
            res += "--------------------------------";
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return res;
    }

    public static String getBestCharacter(Connection conn, int id, String stat) {
        String res = "";
    
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn,
                    String.format("SELECT * FROM Personatge WHERE idFaccio = %s ORDER BY %s DESC LIMIT 1;", id, stat));
    
            if (rs.next()) {
                res += String.format("\n  %s  %s  %s", rs.getString("nom"), rs.getInt("atac"), rs.getInt("defensa"));
            } else {
                res += "No s'ha trobat info";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return res;
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
