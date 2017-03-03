package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public class RunningTime implements DataInterface {
    final String title;
    final int year;
    final String romannumber;
    final String episodename;
    final String episode;
    final String county;
    final int length;

    public RunningTime(String title,int year,String romannumber,String episodename,String episode,String county,int length){
        this.title = title;
        this.year = year;
        this.romannumber = romannumber;
        this.episodename = episodename;
        this.episode = episode;
        this.county = county;
        this.length = length;
    }

    public String getTitle(){
        return this.title;
    }

    public int getYear(){
        return this.year;
    }

    public String getRomannumber(){
        return this.romannumber;
    }

    public String getEpisodename(){
        return this.episodename;
    }

    public String getEpisode(){
        return this.episode;
    }

    public String getCounty(){
        return this.county;
    }

    public int getLength(){
        return this.length;
    }

    public List<String[]> getLines() {
        List<String[]> lines = new ArrayList<String[]>();
        lines.add(new String[]{ this.title,""+this.year,this.romannumber,this.episodename,this.county,""+this.length });

        return lines;
    }
}
