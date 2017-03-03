package parsers;
import data.Genre;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 31/01/2017.
 */
public class GenreParser extends BaseParser{
    @Override
    public List<DataInterface> parse(String filepath, String outputname) {
        List<DataInterface> list = new ArrayList<DataInterface>();

        Pattern genrePattern = Pattern.compile("(.*)\\(([\\d{4}]*|\\?{4})\\)\\s*(?:\\(.+\\))?\\t+(.+)");

        File file = new File(filepath);
        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {
            for(String line; (line = br.readLine()) != null; ) {
                Matcher m = genrePattern.matcher(line);

                if(m.matches())
                    list.add(new Genre(m.group(1),m.group(2),m.group(3)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.toCsv(outputname + ".csv",list);

        System.out.println("Genres Count: " + list.size());
        return list;
    }
}
