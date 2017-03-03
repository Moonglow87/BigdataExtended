package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 31/01/2017.
 */
public class Actor implements DataInterface {
    private String name;
    private List<Role> movieRoles = new ArrayList<Role>();
    private String gender;

    public Actor() {}

    public Actor( final String name) {
        this.name = name;
    }

    public Actor( String name, List<Role> movieRoles, String gender)
    {
        this.movieRoles = movieRoles;
        this.name = name;
        this.gender = gender;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addRole(Role role){
        movieRoles.add(role);
    }

    public String getName()
    {
        return name;
    }

    public List<Role> getMovieRoles()
    {
        return movieRoles;
    }

    @Override
    public List<String[]> getLines() {
        List<String[]> lines = new ArrayList<String[]>();

        if(this.movieRoles.size() == 0)
            lines.add(new String[]{this.name});

        for (Role role: this.movieRoles) {
            lines.add(new String[]{ this.name,role.getTitle(),role.getYear(),role.getRole(),role.getBilling() });
        }

        return lines;
    }
}
