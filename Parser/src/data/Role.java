package data;

/**
 * Created by Daniel on 31/01/2017.
 */
public class Role {
    private final String title;
    private final String role;
    private final String year;
    private final String billing;

    public Role(final String title, final String role, String year, String billing)
    {
        this.title = title;
        this.role = role;
        this.year = year;
        this.billing = billing;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getRole()
    {
        return this.role;
    }

    public String getYear() { return this.year; }

    public String getBilling(){ return this.billing; }
}
