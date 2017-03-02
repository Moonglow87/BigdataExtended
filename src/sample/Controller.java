/**
 * Created by michael on 31/01/2017.
 */
package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import javafx.application.Platform;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import java.sql.*;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller  {

    //JavaFX Elements
    @FXML private TextField queryField;
    @FXML private Button button;
    @FXML private ComboBox<String> comboBox;

    @FXML private TabPane tabPane;

    @FXML private Tab tableTab;
    @FXML private TableView table;
    @FXML private Tab chartTab;
    @FXML private Tab mapTab;
    @FXML private ScrollPane mapPane;
    @FXML private Tab rChartTab;
    @FXML private Tab tableTab2;


    //private QueryTableModel qtm;
    private ObservableList<ObservableList> data;


    private TableColumn col;
    private int colCount;

    private ResultSet rs;

    private String sql = "SELECT * " +
            "FROM actors_in_movies AS one " +
            "INNER JOIN movies AS two " +
            "ON one.movieid = two.movieid " +
            "INNER JOIN actors AS three " +
            "ON one.actorid = three.actorid " +
            "WHERE three.actorname LIKE '%Braakhekke%'";

    private String sql2 = "SELECT one.movietitle, one.moviereleasedate " +
            "FROM movies AS one " +
            "WHERE one.movierating > 8.4 " +
            "ORDER BY one.moviereleasedate ASC " +
            "LIMIT 100";

    private String sql3 = "SELECT * " +
            "FROM actorinmovie " +
            "WHERE actorname LIKE '%Braakhekke%'";




    //CombBox Data
    private String[] comboBoxTableContent = {
            "Welke film die in *LAND* is opgenomen heeft het meeste opgebracht in de bioscoop?", //0
            "Welke films spelen in meer landen?", //2
            "In welke films speelde *ACTEUR*?", //3
            "Welke acteur of actrice speelt het meest in de slechtst gewaardeerde films? (alle films onder rating van *RATING*)", //4
            "Wat is de kortste film met een waardering van *RATING* of hoger?", //5
            "Welke films zijn opgenomen in *LAND*?"}; //6

    private String[] comboBoxMapContent = {
            "Welke films spelen in meer landen?", //1
            "In welke films speelde *ACTEUR*?", //2
            "Welke acteur of actrice speelt het meest in de slechtst gewaardeerde films? (alle films onder rating van *RATING*)", //3
            "Wat is de kortste film met een waardering van *RATING* of hoger?", //4
            "Welke films zijn opgenomen in *LAND*?"}; //5

    private String[] comboBoxChartContent = {
            "Welke film die in *LAND* is opgenomen heeft het meeste opgebracht in de bioscoop?", //0
            "Welke films spelen in meer landen?", //2
            "Welke acteur of actrice speelt het meest in de slechtst gewaardeerde films? (alle films onder rating van *RATING*)", //3
            "Wat is de kortste film met een waardering van *RATING* of hoger?"}; //4

    public void OnButtonClick(){
        System.out.println("OnButtonClick");
        System.out.println(tabPane.getSelectionModel().getSelectedIndex());
        if(tabPane.getSelectionModel().getSelectedIndex()==4){
            try{
            getTableColums(sql3);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void TableTabActive(){
        comboBox.setPromptText("Select Query");
        if(tableTab.isSelected() || tableTab2.isSelected()) {
            System.out.println("TableTabActive");
            comboBox.getItems().setAll(comboBoxTableContent);
        }
    }

    public void ChartTabActive(){
        if(chartTab.isSelected()){
            System.out.println("ChartTabActive");
            comboBox.getItems().setAll(comboBoxChartContent);
        }
    }

    public void MapTabActive(){


        JSONObject obj = new JSONObject();
        JSONArray markers = new JSONArray();

        //loop door data format op deze manier de data
        JSONObject marker = new JSONObject();
        marker.put("label","Test");
        marker.put("content","Content");
        marker.put("location","Mexico City, Distrito Federal, Mexico");
        markers.add(marker);
        //endloop

        obj.put("markers",markers);
        String jsonstring = obj.toJSONString();

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        URL url = getClass().getResource("../html/index.html");
        File file = new File(url.getPath());
        String content = null;
        try {
            content = new String (Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern p = Pattern.compile("\\{\\{.*\\}\\}");
        Matcher m = p.matcher(content);

        content = m.replaceFirst(jsonstring);

        webEngine.loadContent(content);

        mapPane.setContent(webView);

        if(mapTab.isSelected()) {
            System.out.println("MapTabActive");
            comboBox.getItems().setAll(comboBoxMapContent);
        }
    }

    public void RChartTabActive(){
        if(rChartTab.isSelected())
            System.out.println("RChartTabActive");
    }

    public void InitUI(){
    }

    public void getTableColums(String sqlstring){
        try{
            data = FXCollections.observableArrayList();
            DatabaseConnection.initDB();
            rs = DatabaseConnection.statement.executeQuery(sqlstring);
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
                table.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }
            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    if(rs.getString(i) == null)
                        row.add("");
                    else
                        row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);
            }

            //FINALLY ADDED TO TableView

            table.setItems(data);
            DatabaseConnection.closeDB();
        }
        catch (Exception e){
            System.out.println("Error on Building Data");
            e.printStackTrace();
            DatabaseConnection.closeDB();
        }
    }
}
