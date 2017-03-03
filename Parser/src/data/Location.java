package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 31/01/2017.
 */
public class Location implements DataInterface {
    private String title;
    private String year;
    private String location;

    public Location( final String title, final String year ,String location)
    {
        this.title = title;
        this.year = year;
        this.location = location;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setYear(String year){
        this.year = year;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getTitle()
    {
        return title;
    }

    public String getYear()
    {
        return year;
    }

    public String getLocation() { return location; }

    public List<String[]> getLines() {
        List<String[]> lines = new ArrayList<String[]>();
        lines.add(new String[]{ this.title,this.year ,this.location});
        return lines;
    }
}
