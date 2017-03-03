import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.opencsv.CSVWriter;

/**
 * Created by Daniel on 17/01/2017.
 */
public class runningTimesParser {

    public static void runningTimesParser() throws IOException {
        String title = "";
        String year = "";
        String runningTime = "";
        Path path = Paths.get("C:\\Users\\Sam\\Documents\\Big Data\\Parsers Sam\\running-times.csv");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.createFile(path);
        CSVWriter rtWriter = new CSVWriter(new FileWriter("C:\\Users\\Sam\\Documents\\Big Data\\Parsers Sam\\running-times.csv"), ',');
        File file = new File("C:\\Users\\Sam\\Documents\\Big Data\\Parsers Sam\\running-times.list");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
        int count = 0;
        String line;
        line = br.readLine();
        while (line != null && !line.contains("-------------------")) {
            while (count < 14) {
                count++;
                line = br.readLine();
                continue;
            }
            if (line.contains("{") && line.contains("}")) {
                title = "";
                year = "";
                runningTime = "";
                line = br.readLine();
                continue;
            }
            runningTime = line;
            if (runningTime.contains(":")) {
                runningTime = runningTime.split(":")[1];
            }
            else {
                runningTime = runningTime.split("\\t+")[1];
            }
            if (!runningTime.contains("(")) {
                runningTime = runningTime.split("\\n")[0];
            }
            else
            {
                runningTime = runningTime.split("\\(")[0];
            }
            title = line;
            title = title.split("[\\t]+")[0];
            year = title;
            int ii = 0;
            for (int i = 0; i < title.length(); i++)
            {
                if (title.toCharArray()[i] == '(')
                {
                    ii++;
                }
            }
            if (ii > 2)
            {
                title = title.split("\\(")[ii-1];
            }
            else {
                title = title.split("\\([^A-Za-z]{4}\\)")[0];
            }
            year = year.split("\\(")[ii];
            year = year.split("\\)")[0];
            if (year.contains("????")) {
                year = "";
            }
            String[] tempEntries = new String[3];
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    tempEntries[i] = title;
                }
                if (i == 1) {
                    tempEntries[i] = year;
                }
                if (i == 2) {
                    tempEntries[i] = runningTime;
                }
            }
            if (tempEntries[0].contains("\""))
            {
                tempEntries[0] = tempEntries[0].replaceAll("\"", "");
            }
            rtWriter.writeNext(tempEntries);
            System.out.println(tempEntries[0]);
            tempEntries = new String[3];
            title = "";
            year = "";
            runningTime = "";
            line = br.readLine();
            continue;
        }
        rtWriter.close();
    }
}
