package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public class MovieData implements DataInterface
{
    private final String title;
    private final String year;

    public MovieData( final String title, final String year )
    {
        this.title = title;
        this.year = year;
    }

    public String getTitle()
    {
        return title;
    }

    public String getYear()
    {
        return year;
    }

    public List<String[]> getLines() {
        List<String[]> lines = new ArrayList<String[]>();
        lines.add(new String[]{ this.title,this.year });

        return lines;
    }
}