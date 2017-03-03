import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 17/01/2017.
 */
public class businessParser {
    public static void businessParser() throws IOException {
        String title = "";
        String year = "";
        String type = "";
        String currency = "";
        String budget = "";
        String country = "";
        int count = 0;
        Path path = Paths.get("C:\\Users\\Sam\\Documents\\Big Data\\Parsers Sam\\business.csv");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.createFile(path);
        CSVWriter businessWriter = new CSVWriter(new FileWriter("C:\\Users\\Sam\\Documents\\Big Data\\Parsers Sam\\business.csv"), ',');
        File file = new File("C:\\Users\\Sam\\Documents\\Big Data\\Parsers Sam\\business.list");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
        Pattern seperatorPattern = Pattern.compile("^[-]*$");
        Pattern mv = Pattern.compile("MV:");
        Pattern bt = Pattern.compile("([^MV]{2}: [^\\s]+) ((.*))$");
        Pattern gr = Pattern.compile("([^MV]{2}: [A-Za-z0-9]{3} [^\\s]+ [([A-Za-z0-9]+)])");
        String line;
        line = br.readLine();
        while (line != null) {
            if (count < 245)
            {
                count++;
                line = br.readLine();
                continue;
            }
            Matcher seperator = seperatorPattern.matcher(line);


            if (seperator.find()) {
                line = br.readLine();
                continue;
            }

            if (line.contains("MV:")) {
                title = line.split(":")[1];
                title = title.split("\\(")[0];
                year = line.split("\\(")[1];
                year = year.split("\\)")[0];
                if (year == "????")
                {
                    year = "";
                }
                line = br.readLine();
                continue;
            }

            Matcher btm = bt.matcher(line);
            Matcher grm = gr.matcher(line);
            if (line.contains("BT:")) {
                if (btm.find()) {
                    type = line.split(":")[0];
                    currency = line.split(":")[1];
                    currency = currency.split("[\\s]+")[1];
                    line = line.replace(currency, "");
                    budget = line.split(":")[1];
                    budget = budget.split("[\\s]+")[1];
                    country = "";
                }
            }
            else if (line.contains("GR:") || line.contains("OW:"))
            {
                if (grm.find()) {
                    type = line.split(":")[0];
                    currency = line.split(":")[1];
                    currency = currency.split("[\\s]+")[1];
                    line = line.replace(currency, "");
                    budget = line.split(":")[1];
                    budget = budget.split("[\\s]+")[1];
                    line = line.replace(budget, "");
                    country = line.split("\\(")[1];
                    country = country.split("\\)")[0];
                }
            }
            else
            {
                line = br.readLine();
                continue;
            }

            String[] tempEntries = new String[6];
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    tempEntries[i] = title;
                }
                if (i == 1) {
                    tempEntries[i] = year;
                }
                if (i == 2) {
                    tempEntries[i] = type;
                }
                if (i == 3) {
                    tempEntries[i] = currency;
                }
                if (i == 4) {
                    tempEntries[i] = budget;
                }
                if (i == 5) {
                    tempEntries[i] = country;
                }
            }
            System.out.println(tempEntries[0]);
            businessWriter.writeNext(tempEntries);
            tempEntries = new String[6];
            type = "";
            currency = "";
            budget = "";
            country = "";
            line = br.readLine();
            continue;
        }

        businessWriter.close();
    }
}