import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuickTest {
    public static void main(String [] args) {
        
        final Path path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/testData/");
        final Path firstPath = path.resolve("first");
        System.out.println(firstPath);
        final Path secondPath = path.resolve("second");
        final int prefixSize = 2;

        try {
            Partitioner partitioner = new Partitioner();

            // create folder for temporary partition files
            partitioner.createPatitionFileFolder(firstPath);
            partitioner.createPatitionFileFolder(secondPath);

            // partition both files
            partitioner.partitionFile(path.resolve("test1.txt"), firstPath, prefixSize);
            partitioner.partitionFile(path.resolve("test2.txt"), secondPath, prefixSize);

            Path resultPath = path.resolve("result.txt");
            BlockedJoin join = new BlockedJoin();
            join.blockJoin(firstPath,secondPath,resultPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}