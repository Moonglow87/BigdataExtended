/*
package sample;

*/
/**
 * Created by michael on 31/01/2017.
 *//*


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;

class QueryTableModel{

    private ObservableList<ObservableList> data;
*/
/*
    private TableView tableview;
*//*

    private TableColumn col;
    private int colCount;
    private String[] headers;
    private Connection db;
    private ResultSet rs;
    Statement statement;
    String currentURL;

    private String user = "postgres";
    private String pass = "admin";
    private String url = "jdbc:postgresql://localhost:5433/movieDB";

    public QueryTableModel() {
        data = FXCollections.observableArrayList();
        try{
            new org.postgresql.Driver();
        }catch(Exception ee){
            System.out.println("Could not use Driver.");
        }
    }


    public void setHostURL() {
        if (url.equals(currentURL)) {
            // same database, we can leave the current connection open
            return;
        }
        // Oops . . . new connection required
        closeDB();
        initDB(url, user, pass);
        currentURL = url;
    }

*/
/*    public TableView getTableview() {
        return tableview;
    }*//*


    // All the real work happens here; in a real application,
    // we'd probably perform the query in a separate thread.
    public ObservableList<ObservableList> BuildTableModel(String sql) {
        TableView tableview = new TableView();
        try {
            // Execute the query and store the result set and its metadata
            rs = statement.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            colCount = meta.getColumnCount();
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableview.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            */
/********************************
             * Data added to ObservableList *
             ********************************//*

            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
            return data;


        } catch (Exception e) {
            System.out.println("Error on Building Data");
            //e.printStackTrace();
            return null;
        }
    }


    public void initDB(String url, String user, String pass) {
        try {
            db = DriverManager.getConnection(url, user, pass);
            statement = db.createStatement();
        } catch (Exception e) {
            System.out.println("Could not initialize the database.");
            e.printStackTrace();
        }
    }

    public void closeDB() {
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
}

*/
