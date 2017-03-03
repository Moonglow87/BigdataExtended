package parsers;

import data.MovieData;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danie on 1/10/2017.
 */
public class MovieParser extends BaseParser {

    public List<DataInterface> parse(String filePath,String outputName){
        List<DataInterface> list = new ArrayList<DataInterface>();

        Pattern moviepattern = Pattern.compile("(.*)\\((\\d{4}|\\?{4})([IVXLCDM]*)?\\)\\s*(\\(V\\)|\\(TV\\))?(\\d{4}|\\?{4})?$");

        File file = new File(filePath);
        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {

            for(String line; (line = br.readLine()) != null; ) {
                Matcher moviematcher = moviepattern.matcher(line);

                if(moviematcher.find() && moviematcher.group(1) !=null && moviematcher.group(2) != null) {
                    String name = moviematcher.group(1);
                    String year = moviematcher.group(2);

                    name = name.replaceAll(Pattern.quote("\""),"");

                    MovieData item = new MovieData(name,year);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.toCsv(outputName + ".csv",list);

        System.out.println("Movies Count: " + list.size());

        return list;
    }
}