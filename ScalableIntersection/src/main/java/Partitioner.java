/**
 * Partitions a File in to multiple Files based on the keys
 * Lines with common prefix in the key get written in the same file
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// TODO: create first and second file automatically
// TODO: delete files automatically if join is finished
public class Partitioner {

    public void partitionFile(Path inputPath, Path outputPath, int prefixSize) throws IOException {
        String line = null;
        SimpleWriter writer = new SimpleWriter();
        SimpleReader reader = new SimpleReader();
        try {
            System.out.println("Start Partioning file from path " + inputPath + "...");
            BufferedReader bufferedReader = reader.createBufferedReader(inputPath);
            while ((line = bufferedReader.readLine()) != null) {
                String hashKey = createHashKey(extractKey(line), prefixSize);
                Path filePath = outputPath.resolve(hashKey + ".txt");
                writer.writeNewOrExistitingFile(line,filePath);
            }
            bufferedReader.close();
            System.out.println(inputPath + " was successfully partitioned into " + outputPath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String createHashKey(String key, int prefixSize){
        return key.substring(0,prefixSize);
    }

    private String extractKey(String line){
        return line.substring(1);
    }


    public void createPatitionFileFolder(Path path) {
        new File(path.toString()).mkdir();
    }

    public static List<Path> listFiles(Path path) throws IOException {
        System.out.println("Listing all files!");
        List<Path> files = new ArrayList<>();
        Stream<Path> stream = Files.walk(path);
        stream.forEach(files::add);
        return files;
    }
}
