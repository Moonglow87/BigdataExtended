package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by michael on 02/03/2017.
 */
public class DBConnection {

    private static Connection db;
    static Statement statement;

    public static void initDB() {
        try {
            initDBDriver();
            String user = "postgres";
            String pass = "admin!";
            String url = "jdbc:postgresql://localhost:5432/stagingmovieDB";
            db = DriverManager.getConnection(url, user, pass);
            statement = db.createStatement();
        } catch (Exception e) {
            System.out.println("Could not initialize the database.");
            e.printStackTrace();
        }
    }

    public static void closeDB() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (Exception e) {
            System.out.println("Could not close the current connection.");
            e.printStackTrace();
        }
    }

    public static void initDBDriver() {
        try{
            new org.postgresql.Driver();
        }catch(Exception ee){
            System.out.println("Could not use Driver.");
        }
    }
}