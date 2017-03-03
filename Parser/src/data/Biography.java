package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public class Biography implements DataInterface {
    String name;
    String nickname;
    String birthcountry;
    String birthYear;

    public void setName(String name){
        this.name = name;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setBirthcountry(String country){
        this.birthcountry = country;
    }

    public void setBirthYear(String birthYear){
        this.birthYear = birthYear;
    }

    public String getName(){
        return this.name;
    }

    public String getNickname(){
        return this.nickname;
    }

    public String getBirthcountry(){
        return this.birthcountry;
    }

    public String getBirthYear(){
        return this.birthYear;
    }

    public List<String[]> getLines(){
        List<String[]> lines = new ArrayList<String[]>();

        lines.add(new String[]{ this.name,this.nickname,this.birthYear,this.birthcountry });

        return lines;
    }
}
