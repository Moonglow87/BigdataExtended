package data;

import interfaces.DataInterface;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Business implements DataInterface {
    String title;
    String year;
    Budget budget;
    Budget openingWeekend;
    List<Budget> gross = new ArrayList<Budget>();
    List<Budget> weekendgross = new ArrayList<Budget>();

    public void setTitleYear(String title,String year){
        this.title = title;
        this.year = year;
    }

    public void setBudget(String currency,String budget){
        this.budget = new Budget(currency,budget);
    }

    public void addGross(String currency,String budget){
        this.gross.add(new Budget(currency,budget));
    }

    public void addWeekendGross(String currency,String budget){
        this.gross.add(new Budget(currency,budget));
    }

    public void setOpeningWeekend(String currency,String budget){
        this.openingWeekend = new Budget(currency,budget);
    }

    public List<Budget> getGross(){
        return this.gross;
    }

    public List<Budget> getWeekendgross(){
        return this.weekendgross;
    }

    public String getTitle(){
        return this.title;
    }

    public String getYear(){
        return this.year;
    }

    public Budget getBudget(){
        return this.budget;
    }

    public Budget getMaxGross(){
        if(this.gross == null)
            return null;

        Budget max = null;

        for(Budget item : gross) {
            System.out.println(item.getAmount());
            BigInteger maxint = null;

            if (max != null) {
                maxint = new BigInteger(max.getAmount());
            }

            BigInteger itemint = null;
            if (item.getAmount().matches("[0-9]+")) {
                itemint = new BigInteger(item.getAmount());
            }

            if (max == null || itemint.compareTo(maxint) == 1)
                max = item;
        }

        return max;
    }

    public List<String[]> getLines() {
        List<String[]> lines = new ArrayList<String[]>();

        if(this.gross.size() > 0) {
            for (Budget item : this.gross) {
                lines.add(new String[]{
                        this.title,
                        this.year,
                        (this.getMaxGross()!=null) ? this.getMaxGross().getCurrency() : null,
                        (this.getMaxGross()!=null) ? this.getMaxGross().getAmount() : null,
                        (this.budget !=null) ? this.budget.getCurrency() : null,
                        (this.budget !=null) ? this.budget.getAmount() : null,
                        item.getCurrency(),
                        item.getAmount()
                });
            }
        }else{
            lines.add(new String[]{
                this.title,
                this.year,
                (this.getMaxGross()!=null) ? this.getMaxGross().getCurrency() : null,
                (this.getMaxGross()!=null) ? this.getMaxGross().getAmount() : null,
                (this.budget !=null) ? this.budget.getCurrency() : null,
                (this.budget !=null) ? this.budget.getAmount() : null,
                null,
                null
            });
        }

        return lines;
    }
}
