import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Justin on 9/27/2014.
 */
public class Change10Observer implements Observer
{
    private String change10;
    private PrintWriter out = null;

    public Change10Observer(Observable o)
    {
        o.addObserver(this);
        try
        {
            out = new PrintWriter("Change10.dat");
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
            this.change10 = ls.getChange10();
            writeToFile();
        }
    }

    private void writeToFile()
    {
        this.out.print(this.change10);
        this.out.flush();
    }
}
