import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;


/**
 * Created by Justin on 9/27/2014.
 */
public class LocalStocks extends Observable
{

    private String lastUpdated;
    private ArrayList<String[]> snapShot = new ArrayList<String[]>();
    private boolean firstLine = true;
    Scanner fin = null;

    public LocalStocks(String file)
    {

        try
        {
            fin = new Scanner(new File(file));
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File open failed.");
            System.exit(0);
        }

    }

    public void setSnapshot()
    {
       String line;
       this.firstLine = true;
       this.snapShot.clear();

       while(fin.hasNextLine())
       {
           line = fin.nextLine();
           if(line.trim().length() == 0)
                break;

           if(firstLine)
           {
               this.lastUpdated = line;
               this.firstLine = false;
           }
           else
               this.snapShot.add(checkTicker(line));
       }
       stocksChanged();
    }

    public void stocksChanged()
    {
        setChanged();
        notifyObservers();
    }

    public ArrayList<String[]> getSnapShot()
    {
        return this.snapShot;
    }

    public String getLastUpdated()
    {
       return this.lastUpdated;
    }



    /* private helper methods */

    private String[] checkTicker(String line)
    {
        String[] formattedLine = formatTicker(line);
        // should always have a length of nine if everything went right
        assert true : formattedLine.length == 9;
        return formattedLine;

    }

    private String[] formatTicker(String line)
    {
        String[] parts = line.trim().split(" +");

        for(int i = 0; i < parts.length; i++)
        {
            // check for first numeric.
            if(isNumeric(parts[i]))
            {
                String company = "";
                // everything from start up til one before the first numeric is the company name
                for(int j = 0; j <= (i - 2); j++)
                    company += parts[j]+" ";
                company = company.trim();

                ArrayList<String> returnArray = new ArrayList<>();
                returnArray.add(company);
                returnArray.add(parts[i-1]);
                for(int j = i; j < parts.length; j++)
                    returnArray.add(parts[j]);

                return returnArray.toArray(new String[returnArray.size()]);
            }
        }
        // Shouldn't have gotten here
        System.out.println("Improperly formatted ticker");
        System.exit(0);
        return parts;
    }


    private boolean isNumeric(String str)
    {
        try
        {
            double num = Double.parseDouble(str);
        }
        catch(NumberFormatException ex)
        {
            return false;
        }
        return true;
    }

    public static void main(String[] args)
    {
        LocalStocks ls = new LocalStocks("Ticker.dat");
        StockObserver av = new StockObserver(ls, new AverageReport(), "Average");
        ls.setSnapshot();
        StockObserver ch = new StockObserver(ls, new Change10Report(), "Change10");
        StockObserver sl = new StockObserver(ls, new SelectionReport(), "Selection");
        ls.setSnapshot();
        ls.setSnapshot();
        ls.deleteObserver(sl);
        ls.setSnapshot();
        ls.deleteObserver(ch);
        ls.setSnapshot();
        ls.deleteObserver(av);

    }

}
