package parsers;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import interfaces.DataInterface;
import interfaces.parserInterface;

/**
 * Created by Daniel on 24/01/2017.
 */
public abstract class BaseParser implements parserInterface {
    public void toCsv(String filename,List<? extends DataInterface> items){
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filename), ',');
            for (DataInterface item : items) {
                for(String[] line : item.getLines()){
                    writer.writeNext(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}