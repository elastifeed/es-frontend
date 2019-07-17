package de.htw.saar.frontend.service;

import de.htw.saar.frontend.helper.CurrentUser;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.ArtikelMetric;
import de.htw.saar.frontend.model.SearchMetric;

import javax.faces.bean.SessionScoped;
import javax.xml.transform.Result;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * handles the metric data
 * saves to database
 */
@SessionScoped
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
                        + "   artikelid varchar(100) NOT NULL,"
                        + "   username varchar(100) NOT NULL,"
                        + "   date TEXT NOT NULL)";

                stmt = conn.createStatement();
                stmt.execute(sqlCreateArtikelmetric);

                System.out.println("Datenbank init erfolgreich");
                stmt.close();
                conn.close();
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
                String sqlQuery = "INSERT INTO artikelmetric(artikelid, username, date) VALUES ('"+idArtikel+"','"+benutzername+"','"+timestamp+"')";
                Statement stmt = conn.createStatement();
                stmt.execute(sqlQuery);
                stmt.close();
                conn.close();
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
                String sqlQuery = "INSERT INTO searchmetric(search, username, date) VALUES ('"+ searchRequest +"','" + benutzername +"','" + timestamp +"')";
                Statement stmt = conn.createStatement();
                stmt.execute(sqlQuery);
                stmt.close();
                conn.close();
            }

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Gibt die Suchdaten aus der Datenbank zuruek
     * @return
     */
    public ArrayList<SearchMetric> getSearchMetric(){
        try(Connection conn = DriverManager.getConnection(getConnectionString())) {
            if(conn != null) {
                String sqlQuery = "SELECT search, date FROM searchmetric WHERE username = '" + CurrentUser.getInstance().getUser().getUsername() + "' ORDER BY id desc LIMIT 100";
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(sqlQuery);

                ArrayList<SearchMetric> searchMetricArrayList = new ArrayList<>();
                while (resultSet.next()) {
                    String search = resultSet.getString("search");
                    String date = resultSet.getString("date");

                    SearchMetric searchMetric = new SearchMetric(search, date);
                    searchMetricArrayList.add(searchMetric);
                }
                resultSet.close();
                stmt.close();
                conn.close();
                return searchMetricArrayList;
            }
            return null;
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Gibt die Artikeldaten aus der Datenbank zurueck
     * @return
     */
    public ArrayList<ArtikelMetric> getArtikelMetric(){
        try(Connection conn = DriverManager.getConnection(getConnectionString())) {
            if(conn != null) {
                String sqlQuery = "SELECT artikelid, date FROM artikelmetric WHERE username = '" + CurrentUser.getInstance().getUser().getUsername() + "' ORDER BY id desc LIMIT 100";
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(sqlQuery);

                ArrayList<ArtikelMetric> artikelMetricArrayList = new ArrayList<>();
                while (resultSet.next()) {
                    String artikelid = resultSet.getString("artikelid");
                    String date = resultSet.getString("date");

                    ElasticSearchService elasticSearchService = new ElasticSearchService();
                    String artikelTitel = elasticSearchService.getArtikelTitelById(artikelid);

                    ArtikelMetric artikelMetric = new ArtikelMetric(artikelTitel, date);
                    artikelMetricArrayList.add(artikelMetric);
                }
                resultSet.close();
                stmt.close();
                conn.close();
                return artikelMetricArrayList;
            }
            return null;
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
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
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }

}
