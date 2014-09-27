import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Justin on 9/27/2014.
 */
public class AverageObserver implements Observer
{
    private String average;
    private PrintWriter out = null;

    public AverageObserver(Observable o)
    {
        o.addObserver(this);
        try
        {
            out = new PrintWriter("Average.dat");
        }
        catch (FileNotFoundException ex)
        {
            System.out.print("File was not found and could not be created");
            System.exit(0);
        }

    }

    public void update(Observable o, Object arg)
    {
        if( o instanceof LocalStocks)
        {
            LocalStocks ls = (LocalStocks) o;
            this.average = ls.getAverage();
            writeToFile();
        }
    }

    private void writeToFile()
    {
        this.out.print(this.average);
        this.out.flush();
    }
}
