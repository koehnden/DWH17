import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleReader {

    public Map<Integer, List<Character>> read(Path path) throws IOException {
        System.out.println("Start reading data from path: " + path + "...");
        String line = null;
        Map<Integer, List<Character>> lineMap = new HashMap<Integer, List<Character>>();

        BufferedReader bufferedReader = createBufferedReader(path);
        while ((line = bufferedReader.readLine()) != null) {
            int key = Integer.parseInt(line.substring(1));
            Character value = line.charAt(0);
            
            List<Character> list = null;
            
            if(lineMap.containsKey(key)) {
            	list = lineMap.get(key);
            	if(list.contains(value) == false) list.add(value);
            } else {
            	list = new LinkedList<Character>();
            	list.add(value);
            }
            lineMap.put(key,list);
        }
        bufferedReader.close();
        return lineMap;
    }


    public BufferedReader createBufferedReader(Path path) throws IOException {
        FileReader fileReader = new FileReader(path.toFile());
        return new BufferedReader(fileReader);
    }
}