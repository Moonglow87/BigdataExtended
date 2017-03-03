package parsers;

import data.Location;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 31/01/2017.
 */
public class LocationParser extends BaseParser{
    @Override
    public List<DataInterface> parse(String filepath, String outputname) {
        List<DataInterface> list = new ArrayList<DataInterface>();
        //(.*)\((\d{4}|\?{4})\/?([IVXLCDM]*)?\)\s*(\(V\)|\(TV\))?(\d{4}|\?{4})? ?(\{([^(#]+)?([(#]#(.*)\))?\})?(.*)
        File file = new File(filepath);
        Pattern p = Pattern.compile("(.*)\\((\\d{4}|\\?{4})\\/?([IVXLCDM]*)?\\)\\s*(\\(V\\)|\\(TV\\))?(\\d{4}|\\?{4})? ?(\\{([^(#]+)?([(#]#(.*)\\))?\\})?(.*)");

        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {
            for(String line; (line = br.readLine()) != null;) {
                Matcher m = p.matcher(line);

                if (m.find() && m.group(6) == null) {
                    list.add(new Location(
                        m.group(1).replaceAll(Pattern.quote("\""),""),
                        m.group(2),
                        m.group(10)
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.toCsv(outputname + ".csv",list);

        System.out.println("Locations Count: " + list.size());

        return list;
    }
}
