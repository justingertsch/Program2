import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Justin on 9/27/2014.
 */
public class SelectionObserver implements Observer
{
    private String selection;
    private PrintWriter out = null;

    public SelectionObserver(Observable o)
    {
        o.addObserver(this);
        try
        {
            out = new PrintWriter("Selections.dat");
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
            this.selection = ls.getSelections();
            writeToFile();
        }
    }

    private void writeToFile()
    {
        this.out.print(this.selection);
        this.out.flush();
    }
}
