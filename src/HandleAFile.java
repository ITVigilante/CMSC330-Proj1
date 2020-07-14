import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class HandleAFile {
    public String returnFileContents(String file)
    {
        String jointRecords = "";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            //Get contents line by line
            while ((line = reader.readLine()) != null)
            {
                jointRecords = jointRecords + line + " ";
            }
            reader.close();
            return jointRecords;
        }
        catch (FileNotFoundException e)
        {
            System.err.format("Cannot find the file: \"'%s'\".", file);
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.format("A critical error has occurred while opening \"'%s'\".", file);
            e.printStackTrace();

        }
        return null;
    }
}
