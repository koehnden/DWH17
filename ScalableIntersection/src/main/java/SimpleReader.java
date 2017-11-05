import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SimpleReader {

    public Map<String, Character> read(Path path) throws IOException {
        System.out.println("Start reading data from path: " + path + "...");
        String line = null;
        Map<String, Character> lineMap = new HashMap<String, Character>();

        BufferedReader bufferedReader = createBufferedReader(path);
        while ((line = bufferedReader.readLine()) != null) {
            String key = line.substring(1);
            Character value = line.charAt(0);
            lineMap.put(key,value);
        }
        bufferedReader.close();
        return lineMap;
    }


    public BufferedReader createBufferedReader(Path path) throws IOException {
        FileReader fileReader = new FileReader(path.toFile());
        return new BufferedReader(fileReader);
    }
}