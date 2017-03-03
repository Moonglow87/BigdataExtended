package interfaces;

import java.util.List;

/**
 * Created by Daniel on 30/01/2017.
 */
public interface parserInterface {
    public List<DataInterface> parse(String filepath,String outputname);
    public void toCsv(String filename,List<? extends DataInterface> items);
}
