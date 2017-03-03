package parsers;

import data.RunningTime;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 27-Feb-17.
 */
public class RunningTimeParser extends BaseParser {
    @Override
    public List<DataInterface> parse(String filepath, String outputname) {
        List<DataInterface> list = new ArrayList<DataInterface>();

        Pattern p = Pattern.compile("(.*) \\((\\d{4}|\\?{4})\\/?([IVXLCDM]*)?\\)( \\{([^(#]+)?([(#]#(.*)\\))?\\})?\\s*(.+?(?=:))?:?(\\d+)");

        File file = new File(filepath);
        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {
            for(String line; (line = br.readLine()) != null;) {
                try {
                    Matcher m = p.matcher(line);

                    if(m.find()) {
                        RunningTime item = new RunningTime(
                                m.group(1),
                                Integer.parseInt(m.group(2)),
                                m.group(3),
                                m.group(5),
                                m.group(7),
                                m.group(8),
                                Integer.parseInt(m.group(9)
                                ));

                        list.add(item);

                    }else{
                        //System.out.println("Non Match");
                        //System.out.println(line);
                    }
                }catch(Exception ex){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.toCsv(outputname + ".csv",list);

        System.out.println("running-time Count: " + list.size());

        return list;
    }
}
