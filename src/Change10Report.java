import java.util.ArrayList;

/**
 * Created by Justin on 9/30/14.
 */
public class Change10Report implements Report
{
    final double TEN_PERCENT = 10.0;

    public String processSnapShot(ArrayList<String[]> ss, String lastUpdated)
    {
        String report = lastUpdated + ":" +  System.lineSeparator();
        for(String[] line : ss)
        {
            double change = Double.parseDouble(line[4]);
            if (change >= TEN_PERCENT || change <= -TEN_PERCENT)
            {
                report += line[1] + " " + line[2] + " " + line[4] + System.lineSeparator();
            }

        }
        return report + System.lineSeparator();

    }
}
