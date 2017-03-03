package parsers;

import data.Actor;
import data.Biography;
import data.Role;
import interfaces.DataInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 31/01/2017.
 */
public class ActorParser extends BaseParser{
    private String gender;

    public void setGender(String gender){
        this.gender = gender;
    }

    @Override
    public List<DataInterface> parse(String filepath,String outputName) {

        List<DataInterface> list = new ArrayList<DataInterface>();

        Pattern namePattern = Pattern.compile("(.*?)\\t+(.*?)\\s*(\\((\\d{4}|\\?{4})\\/?([IVXLCDM]*)?\\))\\s*(\\((.*)\\))?\\s*\\[(.*)\\](?>.*<(.*)>)?");

        File file = new File(filepath);
        try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"))) {
            Actor current = new Actor();
            for(String line; (line = br.readLine()) != null; ) {
                Matcher m = namePattern.matcher(line);

                if(line.length() == 0){
                    if(current.getName() != null)
                        list.add(current);

                    current = new Actor();
                }else{
                    if(m.matches()) {
                        if(m.group(1) != null && !m.group(1).equals("")) {
                            current.setName(m.group(1));
                        }else{
                            current.addRole(new Role(
                                m.group(2),
                                m.group(8),
                                m.group(4),
                                m.group(9)
                            ));

                            /*m.group(1);//name
                            m.group(2);//movie titel
                            m.group(4);//year
                            m.group(5);//roman nr
                            m.group(7);//??? info
                            m.group(8);//role name
                            m.group(9);//billing*/
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.toCsv(outputName + ".csv",list);

        System.out.println(outputName + " Count: " + list.size());
        return list;
    }
}
