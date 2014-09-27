import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Scanner;


/**
 * Created by Justin on 9/27/2014.
 */
public class LocalStocks extends Observable
{
    private String averageStr;
    private int averageCompanyCount = 0;
    private double averageTotal = 0;
    private String change10Str;
    private String selectionsStr;
    private String lastUpdated;
    private boolean firstLine = true;
    private String[] selectionTicker = {"ALL","BA","BC","GBEL","KFT","MCD","TR","WAG"};
    final double TEN_PERCENT = 10.0;
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
       while(fin.hasNextLine())
       {
           line = fin.nextLine();
           if(line.trim().length() == 0)
                break;

           if(firstLine)
                this.lastUpdated = System.lineSeparator() + line;

           checkTicker(line);
       }
       stocksChanged();
    }

    public void stocksChanged()
    {
        setChanged();
        notifyObservers();
    }

    public String getAverage()
    {
        return this.averageStr;
    }

    public String getChange10()
    {
       return this.change10Str;
    }

    public String getSelections()
    {
        return this.selectionsStr;
    }


    /* private helper methods */

    private void checkTicker(String line)
    {
        String[] formattedLine = null;
        if(!this.firstLine)
        {
            formattedLine = formatTicker(line);
            // should always have a length of nine if everything went right
            assert true : formattedLine.length == 9;

        }
        setAverage(formattedLine);
        setChange10(formattedLine);
        setSelection(formattedLine);

        if(this.firstLine)
        {
            this.firstLine = false;
        }
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

    private void setAverage(String[] formattedLine)
    {
        if(this.firstLine)
        {
            this.averageCompanyCount = 0;
            this.averageTotal = 0;
        }
        else
        {
            this.averageCompanyCount++;
            this.averageTotal += Double.parseDouble(formattedLine[2]);
            this.averageStr = this.lastUpdated + ", Average price: " + (this.averageTotal / this.averageCompanyCount) + System.lineSeparator();
        }
    }

    private void setChange10(String[] formattedLine)
    {
        if(this.firstLine)
        {
            this.change10Str = this.lastUpdated + ":" + System.lineSeparator();
        }
        else
        {
            double change = Double.parseDouble(formattedLine[4]);
            if (change >= TEN_PERCENT || change <= -TEN_PERCENT)
            {
                this.change10Str += formattedLine[1] + " " + formattedLine[2] + " " + formattedLine[4] + System.lineSeparator();
            }
        }
    }

    private void setSelection(String[] formattedLine)
    {
        if(this.firstLine)
        {
            this.selectionsStr = this.lastUpdated + ":" + System.lineSeparator();
        }
        else
        {
            if (Arrays.asList(this.selectionTicker).contains(formattedLine[1]))
            {
                StringBuilder strbuild = new StringBuilder();
                for (String s : formattedLine)
                    strbuild.append(s).append(" ");
                strbuild.deleteCharAt(strbuild.length() - 1);
                strbuild.append(System.lineSeparator());
                this.selectionsStr += strbuild.toString();
            }
        }
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
        AverageObserver av = new AverageObserver(ls);
        ls.setSnapshot();
        Change10Observer ch = new Change10Observer(ls);
        SelectionObserver sl = new SelectionObserver(ls);
        ls.setSnapshot();
        ls.setSnapshot();
        ls.deleteObserver(sl);
        ls.setSnapshot();
        ls.deleteObserver(ch);
        ls.setSnapshot();
        ls.deleteObserver(av);

    }

}
