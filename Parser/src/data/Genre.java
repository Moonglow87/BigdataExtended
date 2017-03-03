package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 31/01/2017.
 */
public class Genre implements DataInterface{
    final String name;
    final String year;
    final String genre;

    public Genre(String name, String year, String genre){
        this.name = name;
        this.year = year;
        this.genre = genre;
    }

    public String getName(){
        return this.name;
    }
    public String getGenre(){ return this.genre; }

    @Override
    public List<String[]> getLines() {
        List<String[]> lines = new ArrayList<String[]>();

        lines.add(new String[]{ this.name,this.year,this.genre });

        return lines;
    }
}
