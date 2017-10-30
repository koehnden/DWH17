/**
 * Partitions a File in to multiple Files based on the keys
 * Lines with common prefix in the key get written in the same file
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Partitioner {

    public void partitionFile(String inputPath, String outputPath, int prefixSize) throws IOException {
        String line = null;
        SimpleWriter writer = new SimpleWriter();
        SimpleReader reader = new SimpleReader();
        try {
            BufferedReader bufferedReader = reader.createBufferedReader(inputPath);
            while ((line = bufferedReader.readLine()) != null) {
                String hashKey = createHashKey(extractKey(line), prefixSize);
                String filePath = outputPath + hashKey + ".txt";
                writer.writeNewOrExistitingFile(line,filePath);
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String createHashKey(String key, int splitNumber){
        return key.substring(0,splitNumber);
    }

    private String extractKey(String line){
        return line.substring(1);
    }

    public List<String> listFileNames(String folderPath){
        List<String> fileNames = new ArrayList<String>();
        File folder = new File(folderPath);
        for (File file : folder.listFiles()){
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}
