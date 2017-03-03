package parsers;

import data.Business;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 17/01/2017.
 */
public class BusinessParser extends BaseParser {
    public List<DataInterface> parse(String filePath,String outputName){
        List<DataInterface> list = new ArrayList<DataInterface>();

        File file = new File(filePath);
        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {
            Business item = new Business();

            Pattern seperatorPattern = Pattern.compile("^[-]*$");
            Pattern mv = Pattern.compile("MV: (.*)\\((\\d{4}|\\?{4})([IVXLCDM]*)?\\)\\s*(\\(V\\)|\\(TV\\))?\\t*(\\d{4})?$");
            Pattern bt = Pattern.compile("BT: ([^\\s]+) ((.*))$");
            Pattern gr = Pattern.compile("GR: ([^\\s]+) ([^\\s]+)");
            Pattern wg = Pattern.compile("WG: ([^\\s]+) ([^\\s]+)");
            Pattern ow = Pattern.compile("OW: ([^\\s]+) ([^\\s]+)");

            for(String line; (line = br.readLine()) != null; ) {
                Matcher seperator = seperatorPattern.matcher(line);


                if (seperator.matches()) {
                    if(item.getTitle() != null) {
                        list.add(item);
                        item = new Business();
                    }
                }

                Matcher mvm = mv.matcher(line);
                if(mvm.find()){
                    try {
                        String year = mvm.group(2);
                        item.setTitleYear(mvm.group(1), (tryParseInt(year) ? year:""));
                    }catch(Exception ex){
                        System.out.println(line);
                    }
                }

                Matcher btm = bt.matcher(line);
                if(btm.find()){
                    item.setBudget(btm.group(1),btm.group(2));
                }

                Matcher grm = gr.matcher(line);
                if(grm.find()){
                    item.addGross(grm.group(1),grm.group(2));
                }

                Matcher wgm = wg.matcher(line);
                if(wgm.find()){
                    item.addWeekendGross(wgm.group(1),wgm.group(2));
                }

                Matcher owm = ow.matcher(line);
                if(owm.find()){
                    item.setOpeningWeekend(owm.group(1),owm.group(2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.toCsv(outputName + ".csv",list);

        System.out.println("Business Count: " + list.size());

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
