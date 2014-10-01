import java.util.ArrayList;

/**
 * Created by Justin on 9/30/14.
 */
public class AverageReport implements Report
{
    public String processSnapShot(ArrayList<String[]> ss, String lastUpdated)
    {

            int CompanyCount = 0;
            double Total = 0;

            for(String[] line : ss)
            {
                CompanyCount++;
                Total += Double.parseDouble(line[2]);

            }

            return lastUpdated + ", Average price: " + (Total / CompanyCount) + System.lineSeparator() + System.lineSeparator();
    }
}
