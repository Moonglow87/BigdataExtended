package data;

import interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public class Budget implements DataInterface {
    final String Currency;
    final String Amount;

    public Budget(String Currency,String Amount){
        this.Currency = Currency;
        this.Amount = Amount.replace(",", "");
    }

    public String getCurrency(){
        return this.Currency;
    }

    public String getAmount(){
        return this.Amount;
    }

    public List<String[]> getLines(){
        List<String[]> lines = new ArrayList<String[]>();
        lines.add(new String[]{ this.Currency,this.Amount });

        return lines;
    }
}
