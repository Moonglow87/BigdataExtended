import parsers.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException {
        RunningTimeParser runningtime = new RunningTimeParser();
        runningtime.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\running-times.list","runningtime");

        BusinessParser bparser = new BusinessParser();
        bparser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\business.list","business");

        BiographyParser bioparser = new BiographyParser();
        bioparser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\biographies.list","bio");

        MovieParser movieParser = new MovieParser();
        movieParser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\movies.list","movies");

        ActorParser actorsParser = new ActorParser();
        actorsParser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\actors.list","actors");
        actorsParser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\actresses.list","actresses");

        LocationParser locationParser = new LocationParser();
        locationParser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\locations.list","locations");

        GenreParser genreParser = new GenreParser();
        genreParser.parse("C:\\Users\\Daniel\\Desktop\\imdbfiles\\genres.list","genres");
    }
}


