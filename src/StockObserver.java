import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Justin on 9/27/2014.
 */
public class StockObserver implements Observer
{
    private ArrayList<String[]> snapShot = null;
    private String processedReport = null;
    private String lastUpdated = null;
    private PrintWriter out = null;
    private Report report = null;

    public StockObserver(Observable o, Report r, String reportName)
    {
        o.addObserver(this);
        this.report = r;
        try
        {
            out = new PrintWriter(reportName+".dat");
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
            this.snapShot = ls.getSnapShot();
            this.lastUpdated = ls.getLastUpdated();
            this.processedReport = this.report.processSnapShot(this.snapShot, this.lastUpdated);
            writeToFile();
        }
    }

    private void writeToFile()
    {
        this.out.print(this.processedReport);
        this.out.flush();
    }
}
