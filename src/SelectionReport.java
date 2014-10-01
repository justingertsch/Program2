import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Justin on 9/30/14.
 */
public class SelectionReport implements Report
{
    private String[] selectionTicker = {"ALL","BA","BC","GBEL","KFT","MCD","TR","WAG"};

    public String processSnapShot(ArrayList<String[]> ss, String lastUpdated)
    {
        String report = lastUpdated + ":" + System.lineSeparator();
        for(String[] line : ss)
        {
            if (Arrays.asList(this.selectionTicker).contains(line[1]))
            {
                StringBuilder strbuild = new StringBuilder();
                for (String s : line)
                    strbuild.append(s).append(" ");
                strbuild.deleteCharAt(strbuild.length() - 1);
                strbuild.append(System.lineSeparator());
                report += strbuild.toString();
            }

        }
        return report + System.lineSeparator();
    }
}
