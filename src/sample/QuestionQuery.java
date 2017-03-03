package sample;

import java.util.ArrayList;

/**
 * Created by michael on 13/02/2017.
 */
public class QuestionQuery {
    public String question = "";
    public String query = "";
    public String defaultvalue = "";

    public static ArrayList<QuestionQuery> tableQuestions;
    public static ArrayList<QuestionQuery> chartQuestions;

    public QuestionQuery(String question, String query, String value) {
        this.question = question;
        this.query = query;
        this.defaultvalue = value;
    }

    public QuestionQuery(String question, String query) {
        this.question = question;
        this.query = query;
    }

    public static void InitChartQuestions(){
        chartQuestions = new ArrayList<>();
        chartQuestions.add(new QuestionQuery("Geef het aantal films dat in Nederland gemaakt is weer in de tijd.",
                "SELECT count(*) as counted, releasedate FROM movies\n" +
                        "INNER JOIN movielocation ON movies.movietitle = movielocation.movietitle\n" +
                        "AND movies.releasedate = movielocation.moviereleasedate\n" +
                        "AND movies.isserie = movielocation.isserie\n" +
                        "WHERE locationname = 'nl' AND movies.isserie = FALSE GROUP BY releasedate\n" +
                        "ORDER BY counted DESC"));
        chartQuestions.add(new QuestionQuery("Wat is het meest voorkomende film genre",
                "SELECT counted, genretype FROM (\n" +
                        " SELECT count(*) AS counted, genretype FROM moviegenre\n" +
                        "WHERE isserie = FALSE\n" +
                        "GROUP BY genretype\n" +
                        ") AS count;"));
        chartQuestions.add(new QuestionQuery("Geef visueel aan waar films worden gemaakt.",
                "SELECT count(*) as counted, country.countryname FROM movielocation\n" +
                        "INNER JOIN country ON country.countryid = movielocation.locationname\n" +
                        "WHERE movielocation.isserie = FALSE GROUP BY countryname\n" +
                        "ORDER BY counted DESC;"));
        chartQuestions.add(new QuestionQuery("Wat is het meest voorkomende genre met het woord 'beer' in de titel",
                "SELECT counted, genretype from (\n" +
                        " SELECT count(*) AS counted, genretype FROM moviegenre\n" +
                        "WHERE isserie = FALSE\n" +
                        "     AND movietitle LIKE '%beer%'\n" +
                        "GROUP BY genretype ORDER BY counted DESC\n" +
                        ") AS count LIMIT 10"));
    }

    public static void InitTableQuestions() {
        tableQuestions = new ArrayList<>();
        tableQuestions.add(new QuestionQuery("Welke acteur heeft de meeste dubbelrollen",
                "SELECT count(*) as dubbelrollen, actorname FROM actorinmovie\n" +
                        "WHERE actorplays LIKE '%/%' GROUP BY actorname ORDER BY dubbelrollen DESC\n" +
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
        tableQuestions.add(new QuestionQuery("Wat is het meest voorkomende genre met het woord 'beer' in de titel",
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
                "SELECT DISTINCT actorinmovie.* FROM actorinmovie\n" +
                        "INNER JOIN moviedirector\n" +
                        "   ON moviedirector.movietitle = actorinmovie.movietitle\n" +
                        " AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate\n" +
                        " AND moviedirector.isserie = actorinmovie.isserie\n" +
                        "WHERE directorname != 'Allen, Woody'\n" +
                        "     AND actorname = 'Allen, Woody'\n" +
                        "     AND actorinmovie.isserie = FALSE;"));
    }
}

