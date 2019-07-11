package de.htw.saar.frontend.service;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * handles the metric data
 * saves to database
 */
public class MetricDataService
{
    public static final String dbName = "esfrontendDB";
    public static final String basePath = "jdbc:sqlite:";


    /**
     * Erstellt die metric datenbank sollte sie nicht existieren
     */
    public void createDatabaseIfNotExist()
    {
        try(Connection conn = DriverManager.getConnection(getConnectionString())) {
            if(conn != null) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Datenbank wurde erstellt oder es wurde eine Verbindung zu einer bestehenden hergestellt");
                System.out.println("Der Name des Drivers ist: " + metaData.getDriverName());
                System.out.println("------------> Success!");
                System.out.println("Erstelle Tabellen if not exist: sqlCreateSearchmetric");
                String sqlCreateSearchmetric = "CREATE TABLE IF NOT EXISTS searchmetric"
                        + "  (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                        + "   search varchar(500) NOT NULL,"
                        + "   username varchar(100) NOT NULL,"
                        + "   date TEXT NOT NULL)";

                Statement stmt = conn.createStatement();
                stmt.execute(sqlCreateSearchmetric);
                System.out.println("Erstelle Tabellen if not exist: sqlCreateArtikelmetric");
                String sqlCreateArtikelmetric = "CREATE TABLE IF NOT EXISTS artikelmetric"
                        + "  (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                        + "   artikelid varchar(36) NOT NULL,"
                        + "   username varchar(100) NOT NULL,"
                        + "   date TEXT NOT NULL)";

                stmt = conn.createStatement();
                stmt.execute(sqlCreateArtikelmetric);

                System.out.println("Datenbank init erfolgreich");
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Fügt neue Daten in die Datenbank ein die die Artikel metrik betreffen
     */
    public void addArtikelMetrik(String idArtikel, String benutzername)
    {
        if(!isValid(idArtikel) || !isValid(benutzername)) {
            return;
        }

        String timestamp = getCurrentTimestamp();
        if(timestamp == null) {
            return;
        }
        if(timestamp.length() < 1) {
            return;
        }

        try(Connection conn = DriverManager.getConnection(getConnectionString())) {
            if(conn != null) {
                String sqlQuery = "INSERT INTO artikelmetric(artikelid, username, date) VALUES (" + idArtikel + ", " + benutzername + ", " + timestamp + ")";
                Statement stmt = conn.createStatement();
                stmt.execute(sqlQuery);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }



    /**
     * Fügt neue Daten in die Datenbank ein die die Artikel-Suche metrik betreffen
     */
    public void addSearchMetric(String searchRequest, String benutzername)
    {
        if(!isValid(searchRequest) || !isValid(benutzername)) {
            return;
        }

        String timestamp = getCurrentTimestamp();
        if(!isValid(timestamp)) {
            return;
        }

        try(Connection conn = DriverManager.getConnection(getConnectionString())) {
            if(conn != null) {
                String sqlQuery = "INSERT INTO searchmetric(search, username, date) VALUES (" + searchRequest + ", " + benutzername + ", " + timestamp + ")";
                Statement stmt = conn.createStatement();
                stmt.execute(sqlQuery);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Prüft den parameter auf not null und not empty
     * @param a
     * @return
     */
    private boolean isValid(String a)
    {
        if(a == null) {
            return false;
        }

        if(a.length() < 1) {
            return false;
        }

        return true;
    }

    /**
     * Gibt den connection string zur sqlite3 db zurück
     * @return
     */
    private String getConnectionString()
    {
        return basePath + dbName;
    }

    /**
     * gibt den aktuellen timestamp zurück
     * @return
     */
    private String getCurrentTimestamp()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

}
