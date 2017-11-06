import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SimpleReader {

    public Map<Integer, Character> read(Path path) throws IOException {
        System.out.println("Start reading data from path: " + path + "...");
        String line = null;
        Map<Integer, Character> lineMap = new HashMap<Integer, Character>();

        BufferedReader bufferedReader = createBufferedReader(path);
        while ((line = bufferedReader.readLine()) != null) {
            int key = Integer.parseInt(line.substring(1));
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