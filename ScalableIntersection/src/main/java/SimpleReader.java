import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SimpleReader {

    public Map<Long, Character> read(String path) throws IOException {

        String line = null;
        Map<Long, Character> result =
                new HashMap<Long, Character>();

        try {
            BufferedReader bufferedReader = createBufferedReader(path);

            while ((line = bufferedReader.readLine()) != null) {
                Long key = Long.parseLong(line.substring(1));
                Character value = line.charAt(0);
                result.put(key,value);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + path + "'");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public BufferedReader createBufferedReader(String path) throws IOException {
        FileReader fileReader =
                new FileReader(path);
        return new BufferedReader(fileReader);
    }
}