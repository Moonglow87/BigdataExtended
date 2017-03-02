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
import java.util.ArrayList;
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

    private ArrayList<QuestionQuery> tableQuestions;



    //private QueryTableModel qtm;
    private ObservableList<ObservableList> data;


    private TableColumn col;
    private int colCount;

    private ResultSet rs;


    public void OnButtonClick(){
        System.out.println("OnButtonClick");
        System.out.println(tabPane.getSelectionModel().getSelectedIndex());
        if(tabPane.getSelectionModel().getSelectedIndex()==0){
            try{
            getTableColums(tableQuestions.get(comboBox.getSelectionModel().getSelectedIndex()).query);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void TableTabActive(){
        comboBox.setPromptText("Select Query");
        if(tableTab.isSelected()) {
            System.out.println("TableTabActive");
            InitTableQuestions();
            for (QuestionQuery question: tableQuestions)
                comboBox.getItems().add(question.question);
        }
    }

    public void ChartTabActive(){
        if(chartTab.isSelected()){
            System.out.println("ChartTabActive");
            //comboBox.getItems().setAll(comboBoxChartContent);
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
            //comboBox.getItems().setAll(comboBoxMapContent);
        }
    }


    public void InitUI(){
    }

    public void clearTable(){
        table.getItems().clear();
        table.getColumns().clear();
    }

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

    private void InitTableQuestions() {
        tableQuestions = new ArrayList<>();
        tableQuestions.add(new QuestionQuery("Welke acteur (m/v) heeft de meeste dubbelrollen",
                "SELECT count(*) as counted, actorname FROM actorinmovie\n" +
                        "WHERE actorplays LIKE '%/%' GROUP BY actorname ORDER BY counted DESC\n" +
                        "LIMIT 10;"));
        tableQuestions.add(new QuestionQuery("In welke films speelde Joop Braakhekke",
                "SELECT * FROM actorinmovie\n" +
                        "WHERE actorname = 'Braakhekke, Joop' \n" +
                        "AND isserie = FALSE;"));
        tableQuestions.add(new QuestionQuery("Welke acteur (m/v) speelt het meest in de slechtst gewaardeerde films? !!Duurt Lang!!",
                "SELECT one.actorname, sum(case when two.rating < 3.5 and two.isserie = FALSE then 1 else 0  end) as BadMovies\n" +
                        "FROM actorinmovie AS one\n" +
                        "INNER JOIN movies AS two\n" +
                        "   ON one.movietitle = two.movietitle\n" +
                        "   AND one.moviereleasedate = two.releasedate\n" +
                        "GROUP BY one.actorname\n" +
                        "ORDER BY BadMovies DESC\n" +
                        "LIMIT 100;"));
        tableQuestions.add(new QuestionQuery("Welke regisseur heeft de meeste films met Jim Carrey in de hoofdrol geregisseerd",
                "SELECT count(*) AS counted, directorname FROM moviedirector\n" +
                        "INNER JOIN actorinmovie\n" +
                        "ON moviedirector.movietitle = actorinmovie.movietitle\n" +
                        " AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate\n" +
                        " AND moviedirector.isserie = actorinmovie.isserie\n" +
                        "WHERE actorinmovie.actorname = 'Carrey, Jim'\n" +
                        "    AND actorinmovie.priority = 1\n" +
                        "GROUP BY directorname\n" +
                        "ORDER BY counted DESC\n" +
                        "LIMIT 10;"));
        tableQuestions.add(new QuestionQuery("In welk jaar tussen 1990 en nu zijn de meeste films met het woord ‘beer’ in de titel geproduceerd",
                "SELECT counted, releasedate from (\n" +
                        " SELECT count(*) AS counted, releasedate FROM movies\n" +
                        "WHERE isserie = FALSE\n" +
                        "     AND movietitle LIKE '%beer%'\n" +
                        "GROUP BY releasedate ORDER BY counted DESC\n" +
                        ") AS count LIMIT 10"));
        tableQuestions.add(new QuestionQuery("^... En wat is het meest voorkomende genre",
                "SELECT counted, genretype from (\n" +
                        " SELECT count(*) AS counted, genretype FROM moviegenre\n" +
                        "WHERE isserie = FALSE\n" +
                        "     AND movietitle LIKE '%beer%'\n" +
                        "GROUP BY genretype ORDER BY counted DESC\n" +
                        ") AS count LIMIT 10"));
        tableQuestions.add(new QuestionQuery("Wat is de kortste film met een waardering van 8.5 of hoger",
                "SELECT *\n" +
                        "FROM movies\n" +
                        "WHERE rating > 8.5 AND duration > 1\n" +
                        "      AND rating NOTNULL\n" +
                        "      AND movies.isserie = FALSE\n" +
                        "ORDER BY duration ASC;"));
        tableQuestions.add(new QuestionQuery("Hoeveel films heeft Woody Allen gemaakt",
                "SELECT count(*)\n" +
                        "FROM moviedirector\n" +
                        "WHERE directorname = 'Allen, Woody';"));
        tableQuestions.add(new QuestionQuery("In hoeveel daarvan speelde Woody Allen zelf mee",
                "SELECT count(*) FROM actorinmovie\n" +
                        "INNER JOIN moviedirector\n" +
                        "   ON moviedirector.movietitle = actorinmovie.movietitle\n" +
                        " AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate\n" +
                        " AND moviedirector.isserie = actorinmovie.isserie\n" +
                        "WHERE directorname = 'Allen, Woody'\n" +
                        "     AND actorname = 'Allen, Woody'\n" +
                        "     AND actorinmovie.isserie = FALSE;"));
        tableQuestions.add(new QuestionQuery("Zijn er ook films waarin Woody Allen wel speelde maar niet regisseerde",
                "SELECT actorinmovie.* FROM actorinmovie\n" +
                        "INNER JOIN moviedirector\n" +
                        "   ON moviedirector.movietitle = actorinmovie.movietitle\n" +
                        " AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate\n" +
                        " AND moviedirector.isserie = actorinmovie.isserie\n" +
                        "WHERE directorname != 'Allen, Woody'\n" +
                        "     AND actorname = 'Allen, Woody'\n" +
                        "     AND actorinmovie.isserie = FALSE;"));
    }
}
