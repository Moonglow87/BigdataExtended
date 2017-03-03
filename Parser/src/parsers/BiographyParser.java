package parsers;

import data.Biography;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 17/01/2017.
 */
public class BiographyParser extends BaseParser {
    public List<DataInterface> parse(String filePath,String outputName){
        List<DataInterface> list = new ArrayList<DataInterface>();

        File file = new File(filePath);
        Biography item = new Biography();
        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {
            Pattern seperatorPattern = Pattern.compile("^[-]*$");
            Pattern db = Pattern.compile("DB: (.*)");
            Pattern name = Pattern.compile("NM: (.*)");
            Pattern rname = Pattern.compile("RN: (.*)");
            Pattern date = Pattern.compile("DB: ([0-9]+)? ?([^\\s]+)? ?(\\d{4})");

            for(String line; (line = br.readLine()) != null; ) {
                Matcher seperator = seperatorPattern.matcher(line);

                if (seperator.matches()) {
                    if(item.getName() != null || item.getNickname() != null) {
                        list.add(item);
                    }

                    item = new Biography();
                }

                Matcher namem = name.matcher(line);
                if(namem.find()){
                    item.setNickname(namem.group(1));
                }

                Matcher rnamem = rname.matcher(line);
                if(rnamem.find()){
                    item.setName(rnamem.group(1));
                }

                Matcher dbm = db.matcher(line);
                if(dbm.find()){
                    List<String> lineparts = Arrays.asList(line.split(","));
                    Matcher datem = date.matcher(line);

                    if(datem.find()){
                        String day = datem.group(1);
                        String month = datem.group(2);
                        String year = datem.group(3);
                        item.setBirthYear(year);
                    }

                    if(lineparts.size() > 0){
                        String country = lineparts.get(lineparts.size()-1);
                        if(!country.matches(".*\\d+.*")) {
                            item.setBirthcountry(country);
                        }
                    }else{
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Bio Count: " + list.size());

        this.toCsv(outputName + ".csv",list);

        return list;
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
