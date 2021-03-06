/**
 * Created by michael on 03/01/2017.
 */
package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
    @FXML private Button button;
    @FXML private ComboBox<String> comboBox;

    @FXML private TabPane tabPane;

    @FXML private Tab tableTab;
        @FXML private TableView table;
    @FXML private Tab chartTab;
        @FXML private PieChart pieChart;
        @FXML private BarChart barChart;
    @FXML private Tab mapTab;
    @FXML private ScrollPane mapPane;

    //private ArrayList<QuestionQuery> tableQuestions;



    //private QueryTableModel qtm;
    private ObservableList<ObservableList> data;
    private ObservableList pieChartData;



    private TableColumn col;
    private int colCount;

    private ResultSet rs;


    public void OnButtonClick(){
        System.out.println("OnButtonClick");
        System.out.println(tabPane.getSelectionModel().getSelectedIndex());
        if(tabPane.getSelectionModel().getSelectedIndex()==0){
            try{
                getTableColums(QuestionQuery.tableQuestions.get(comboBox.getSelectionModel().getSelectedIndex()).query);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(tabPane.getSelectionModel().getSelectedIndex()==1){
            try{
                //getTableColums(QuestionQuery.tableQuestions.get(comboBox.getSelectionModel().getSelectedIndex()).query);
                if(comboBox.getSelectionModel().getSelectedIndex() == 0)
                    getBarChartData(QuestionQuery.chartQuestions.get(comboBox.getSelectionModel().getSelectedIndex()).query);
                if(comboBox.getSelectionModel().getSelectedIndex() > 0)
                    getPieChartData(QuestionQuery.chartQuestions.get(comboBox.getSelectionModel().getSelectedIndex()).query);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void TableTabActive(){
        comboBox.setPromptText("Select Query");
        comboBox.getItems().clear();
        if(tableTab.isSelected()) {
            System.out.println("TableTabActive");
            //QuestionQuery.InitTableQuestions();
            for (QuestionQuery question: QuestionQuery.tableQuestions)
                comboBox.getItems().add(question.question);
        }
    }

    public void ChartTabActive(){
        comboBox.setPromptText("Select Query");
        comboBox.getItems().clear();
        if(chartTab.isSelected()){
            System.out.println("ChartTabActive");
            //QuestionQuery.InitChartQuestions();
            //comboBox.getItems().setAll(chartQuestions);;
            for (QuestionQuery question: QuestionQuery.chartQuestions)
                comboBox.getItems().add(question.question);
        }
    }

    public void MapTabActive(){
        JSONObject obj = new JSONObject();
        JSONArray markers = new JSONArray();

        //loop door data format op deze manier de data
        JSONObject marker = new JSONObject();
        marker.put("label","Test");
        marker.put("content","Content");
        marker.put("location","41.3308,-94.0138");
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
            //comboBox.getItems().setAll(comboBoxMapContent);
        }
    }


    public void InitUI(){
    }

    public void clearTable(){
        table.getItems().clear();
        table.getColumns().clear();
    }

    /********************************
     * Todo: Move this another Class?*
     ********************************/
    public void getTableColums(String sqlstring){
        try{
            clearTable();
            data = FXCollections.observableArrayList();
            DBConnection.initDB();
            rs = DBConnection.statement.executeQuery(sqlstring);
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
                    //Iterate Column and replace nulls with emptystring
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
            DBConnection.closeDB();
        }
        catch (Exception e){
            System.out.println("Error on Building Data");
            e.printStackTrace();
            DBConnection.closeDB();
        }
    }

    private void getPieChartData(String sql){
        try{
            barChart.setVisible(false);
            pieChart.setVisible(true);
            pieChart.getData().clear();
            DBConnection.initDB();
            rs = DBConnection.statement.executeQuery(sql);
            pieChartData = FXCollections.observableArrayList();
            while(rs.next()){
                //adding data on piechart data
                pieChartData.add(new PieChart.Data(rs.getString(2),rs.getDouble(1)));
            }
            pieChart.getData().addAll(pieChartData);
        }catch(Exception e){
            System.out.println("Error on DB connection");
            DBConnection.closeDB();
        }

    }


    private void getBarChartData(String sql) {
        try{
            pieChart.setVisible(false);
            barChart.setVisible(true);
            DBConnection.initDB();
            rs = DBConnection.statement.executeQuery(sql);
            XYChart.Series serie = new XYChart.Series();
            serie.setName("Nederland");  //TODO: Change it Dynammically now only usable for 1 Country "Netherlands"
            while (rs.next())
            {
                //adding data on barchart Data
                serie.getData().add(new XYChart.Data(rs.getString(2), Integer.parseInt(rs.getString(1))));
            }
            barChart.getData().retainAll();
            barChart.getData().add(serie);
        }
        catch (Exception e)
        {
            System.out.println("Error on Building Data");
            DBConnection.closeDB();

        }
    }
}
