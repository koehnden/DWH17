import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Experiment {

    public static void main(String [] args) {
        final Path path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/testData/");

        try{
            Map<String, BufferedWriter> writerMap = createPartitionFiles(path,2);
            writerMap.get("89.txt").write("TEST STRING");
            closeAllWriters(writerMap);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static Map<String, BufferedWriter> createPartitionFiles(Path path, int prefixSize) throws IOException{
        Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
        int i = (int) Math.pow(10,prefixSize-1);
        int maxNumber = createMaxNumber(prefixSize);
        while(i <= maxNumber){
            String fileName = Integer.toString(i) + ".txt";
            //File tmpFile = File.createTempFile(filePath, "txt");
            // File file = new File(path.resolve(fileName).toString());
            File file = path.resolve(fileName).toFile();
            writerMap.put(fileName, new BufferedWriter(new FileWriter(file)));
            i++;
        }
        return writerMap;
    }

    public static int createMaxNumber(int prefixSize){
        String maxString = String.format("%0" + prefixSize + "d", 0).replace("0","9");
        return Integer.parseInt(maxString);
    }


    public static void closeAllWriters(Map<String, BufferedWriter> writerMap) throws IOException {
        for (String key : writerMap.keySet()){
            writerMap.get(key).close();
        }
    }
}
