package com.project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLFunctionManager {
    void initDatabase(String filePath) {
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
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Cavallers','The Knights had been scattered for centuries but have begun to reunite under a single banner, that of the Iron Legion.');");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Vikings', 'The Vikings are the undisputed power of the rivers and seas.');");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Samurais', 'After having been driven from their ancestral homes and rebuilding their forces, though strong and mighty, the Samurai still find themselves vastly outnumbered by their new neighbors.');");


        //Agafem els id de les taules
        int idCavallers = getFaccioId(conn, "Cavallers");
        int idVikings = getFaccioId(conn, "Vikings");
        int idSamurais = getFaccioId(conn, "Samurais");

        //Cavallers
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warden\", 120, 135, %s)", idCavallers));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warmonger\", 130, 130, %s)", idCavallers));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Peacekeeper\", 120, 120, %s)", idCavallers));
        //Vikings
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Valkyrie\", 120, 120, %s)", idVikings));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Berserker\", 130, 140, %s)", idVikings));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Jormungandr\", 135, 140, %s)", idVikings));
        //Samurais
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Shinobi\", 120, 135, %s)", idSamurais));
        UtilsSQLite.queryUpdate(conn, String.format("INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Aramusha\", 125, 120, %s)", idSamurais));
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
        } catch (SQLException e) {}

        return idFaccion;
    }

    public  String getFaccions(Connection conn) {
        String res = "\nFaccio:\n";

        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "Select * FROM Faccio;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                res += String.format("  %s) %s \n", id, name);
            }
            res += "Opcio: ";
        } catch (Exception e) {}

        return res;
    }

    public String getFaccioCharacters(Connection conn, int id) {
        String res = "\nPersonatges de la Faccio " + id + ":\n";
        res += "\n----------------------------------------\n";
        res += String.format(" %-13s  %-13s  %-13s\n", "Nom", "Atac", "Defensa");
        res += "----------------------------------------\n";
    
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT * FROM Personatge WHERE idFaccio = " + id + ";");
            while (rs.next()) {
                res += String.format(" %-13s  %-13.2f  %-13.2f\n", rs.getString("nom"), rs.getDouble("atac"), rs.getDouble("defensa"));
            }
            res += "----------------------------------------";
        } catch (Exception e) {}
    
        return res;
    }
    
    

    public  String getTables(Connection conn) {
        String res = "\n";
        ArrayList<String> t = UtilsSQLite.listTables(conn);

        for (int i = 0; i < t.size(); i++) {
            res += String.format(" %s) %s\n", i+1, t.get(i));
        }
        res += "Opcio: ";
        return res;
    }

    public String getTable(Connection conn, int id) {
        String res = "\n";
        String table = "";
    
        try {
            if (id == 2) {
                table = "Personatge";
                res += "Taula " + table + ":\n";
                res += "-----------------------------------------------------------------\n";
                res += String.format("%-14s  %-14s  %-14s  %-14s\n", "Nom", "Atac", "Defensa", "idFraccio");
                res += "-----------------------------------------------------------------\n";
            } else if (id == 1) {
                table = "Faccio";
                res += "Taula " + table + ":\n";
                res += "-----------------------------------------------------------------\n";
                res += String.format("%-14s  %-14s\n", "Nom", "Resum");
                res += "-----------------------------------------------------------------\n";
            } else {
                return "No n'hi han taula amb aquest id";
            }
            ResultSet rs = UtilsSQLite.querySelect(conn, String.format("Select * From %s", table));
            while (rs.next()) {
                if (id == 2) {
                    res += String.format("%-14s  %-14d  %-14d  %-14d\n", rs.getString("nom"), rs.getInt("atac"), rs.getInt("defensa"), rs.getInt("idFaccio"));
                } else if (id == 1) {
                    res += String.format("%-14s  %-30s\n", rs.getString("nom"), rs.getString("resum"));
                }
            }
            res += "-----------------------------------------------------------------\n";
    
        } catch (Exception e) {
        }
        return res;
    }
    
    

    public  String getBestCharacter(Connection conn, int id, String stat) {
        String res = "";
        res += "\n----------------------------------------\n";
        res += String.format(" %-13s  %-13s  %-13s\n", "Nom", "Atac", "Defensa");
        res += "----------------------------------------\n";
    
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn,
                    String.format("SELECT * FROM Personatge WHERE idFaccio = %s ORDER BY %s DESC LIMIT 1;", id, stat));
    
            if (rs.next()) {
                res += String.format(" %-13s  %-13s  %-13s\n", rs.getString("nom"), rs.getInt("atac"), rs.getInt("defensa"));
                res += "----------------------------------------\n";
            } else {
                res += "No s'ha trobat info";
            }
        } catch (Exception e) {}
    
        return res;
    }
}
