/**
 * Partitions a File in to multiple Files based on the keys
 * Lines with common prefix in the key get written in the same file
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

//TODO: use StringBuilder
public class Partitioner {

    public Map<String, BufferedWriter> partitionFile(Path inputPath, Path outputPath, int prefixSize) throws IOException {
        String line = null;
        SimpleReader reader = new SimpleReader();

        System.out.println("[Info] Start Partioning file from path " + inputPath + "...");
        BufferedReader bufferedReader = reader.createBufferedReader(inputPath);
        Map<String, BufferedWriter> writerMap = createPartitionFiles(outputPath,prefixSize);
        System.out.println("Files created!");

        System.out.println("[Info] Filling the partition files...");
        while ((line = bufferedReader.readLine()) != null) {
            String hashKey = createHashKey(extractKey(line), prefixSize);
            Path filePath = outputPath.resolve(hashKey);
            BufferedWriter currentWriter = getBufferedWriter(writerMap, filePath);
            writerMap.remove(filePath);
            currentWriter.write(createEntry(line,prefixSize));
            currentWriter.newLine();
        }
        System.out.println("[Info]" + inputPath + " was successfully partitioned into " + outputPath);
        System.out.println("[Info] Close all Files...");
        bufferedReader.close();
        closeAllWriters(writerMap);
        return writerMap;
    }

    private BufferedWriter getBufferedWriter(Map<String, BufferedWriter> writerMap, Path filePath){
        StringBuilder builder = new StringBuilder();
        String fileName = builder
                .append(filePath.getFileName())
                .toString();
        return writerMap.get(fileName);
    }

    private String createHashKey(String key, int prefixSize){
        return key.substring(0,prefixSize);
    }

    private String extractKey(String line){
        return line.substring(1);
    }

    private String createEntry(String line, int prefixSize){
        StringBuilder builder = new StringBuilder(line);
        String entry = builder
                .delete(1,prefixSize)
                .toString();
        return entry;
    }


    public Map<String, BufferedWriter> createPartitionFiles(Path path, int prefixSize) throws IOException{
        System.out.println("Create File for Partitions");
        Map<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
        int fileInt = (int) Math.pow(10, prefixSize-1);
        int maxNumber = createMaxNumber(prefixSize);
        while(fileInt <= maxNumber){
            String fileName = Integer.toString(fileInt);
            File file = path.resolve(fileName).toFile();
            BufferedWriter newWriter = new BufferedWriter(new FileWriter(file));
            writerMap.put(fileName, newWriter);
            fileInt++;
        }
        return writerMap;
    }

    public void createPartitionFileFolder(Path path) {
        new File(path.toString()).mkdir();
    }

    public int createMaxNumber(int prefixSize){
        String maxString = String.format("%0" + prefixSize + "d", 0).replace("0","9");
        return Integer.parseInt(maxString);
    }

    public static void closeAllWriters(Map<String, BufferedWriter> writerMap) throws IOException {
        for (String key : writerMap.keySet()){
            writerMap.get(key).close();
        }
    }
}
