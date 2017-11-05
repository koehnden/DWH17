import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuickTest {

    public static void main(String [] args) {

        long startTime = System.nanoTime();

        final Path path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/testData/");
        final Path firstPath = path.resolve("first");
        final Path secondPath = path.resolve("second");
        final int prefixSize = 1;

        try {
            Partitioner partitioner = new Partitioner();

            // create folder for temporary partition files
            partitioner.createPartitionFileFolder(firstPath);
            partitioner.createPartitionFileFolder(secondPath);

            // partition both files
            Set<String> firstFileSet = partitioner.partitionFile(path.resolve("test1.txt"), firstPath, prefixSize).keySet();
            Set<String> secondFileSet = partitioner.partitionFile(path.resolve("test2.txt"), secondPath, prefixSize).keySet();

            BlockedJoin join = new BlockedJoin();
            join.blockJoin(firstFileSet,secondFileSet,path, prefixSize);

        } catch (IOException e) {
            e.printStackTrace();
        }

        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time to execute join: " + estimatedTime);
    }
}