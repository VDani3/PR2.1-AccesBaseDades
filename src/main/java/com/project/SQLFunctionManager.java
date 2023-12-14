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
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Cavallers','The Knights are one of the four playable factions in For Honor. It is their belief that many, if not all of the ancient ruins were constructed by their ancestors. The Knights had been scattered for centuries but have begun to reunite under a single banner, that of the Iron Legion. There are those still, however, who choose to gather their own ''Legion'', and their alliance with the Iron Legion is shaky at best.  ');");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Vikings', 'The Vikings are the undisputed power of the rivers and seas. In their pursuit to gain the approval of their gods in Valhalla, the Vikings live for battle and glory as they seek out riches and new land, destroying everything in their path to gain it.');");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Samurais', 'After having been driven from their ancestral homes and rebuilding their forces, though strong and mighty, the Samurai still find themselves vastly outnumbered by their new neighbors. Because of this, they know that only through greater cunning, skill and loyalty to each other will their people have a chance to thrive. They may very well be the last of their kind.');");


        //Agafem els id de les taules
        int idCavallers = getFaccioId(conn, "Cavallers");
        int idVikings = getFaccioId(conn, "Vikings");
        int idSamurais = getFaccioId(conn, "Samurais");

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
        res += "\n--------------------------------\n";
        res += String.format(" %-10s% -10s% -10s\n", "Nom", "Atac", "Defensa");
        res += "--------------------------------\n";
    
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT * FROM Personatge WHERE idFaccio = " + id + ";");
            while (rs.next()) {
                res += String.format(" %-10s% -10.2f% -10.2f\n", rs.getString("nom"), rs.getDouble("atac"), rs.getDouble("defensa"));
            }
            res += "--------------------------------";
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

    public  String getTable(Connection conn, int id) {
        String res = "\n";
        String table = "";

        try {
            if (id == 2) {
                table = "Personatge";
                res += "Taula "+table+":\n";
                res += "----------------------------------\n";
                res += "Nom     Atac   Defensa   idFraccio\n";
                res += "----------------------------------\n";
            } else if (id == 1) {
                table = "Faccio";
                res += "Taula "+table+":\n";
                res += "----------------------------\n";
                res += "Nom      Resum\n";
                res += "----------------------------\n";
            } else {
                return "No n'hi han taula amb aquest id";
            }
            ResultSet rs = UtilsSQLite.querySelect(conn,String.format("Select * From %s", table));
            while (rs.next()) {
                if (id == 2) {
                    res += String.format("%s  %s  %s  %s\n", rs.getString("nom"), rs.getInt("atac"), rs.getInt("defensa"), rs.getInt("idFaccio"));
                } else if (id == 1) {
                    res += String.format("%s  %s\n", rs.getString("nom"), rs.getString("resum"));
                }
            }
            res += "----------------------------\n";

            
        } catch (Exception e) {}
        return res;
    }

    public  String getBestCharacter(Connection conn, int id, String stat) {
        String res = "";
    
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn,
                    String.format("SELECT * FROM Personatge WHERE idFaccio = %s ORDER BY %s DESC LIMIT 1;", id, stat));
    
            if (rs.next()) {
                res += String.format("\n  %s  %s  %s", rs.getString("nom"), rs.getInt("atac"), rs.getInt("defensa"));
            } else {
                res += "No s'ha trobat info";
            }
        } catch (Exception e) {}
    
        return res;
    }
}
