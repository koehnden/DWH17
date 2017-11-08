import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static java.lang.System.currentTimeMillis;

/**
 * This Program with prefixSize = 3 runs approximately 4min 15sec and never exceeds 100mb
 * result file is slightly smaller than in the slides 2087877 intstead of 2097858 (bug or wrong in the slides)
 */

public class Run {

    public static void main(String [] args) {

        long startTime = currentTimeMillis();
        
        final Path firstInputFilePath = Paths.get(args[0]);
        final Path secondInputFilePath = Paths.get(args[1]) ;
        final Path destPath = Paths.get(args[2]);
        final Path firstPath = destPath.resolve("first");
        final Path secondPath = destPath.resolve("second");
        final int prefixSize = 3;  // only prefixSize that does not exceed 100mb heap size

        try {
            Partitioner partitioner = new Partitioner();

            // create folder for temporary partition files
            partitioner.createPartitionFileFolder(firstPath);
            partitioner.createPartitionFileFolder(secondPath);

            // partition both files
            Set<String> firstFileSet = partitioner.partitionFile(firstInputFilePath.resolve("file1.txt"), firstPath, prefixSize).keySet();
            Set<String> secondFileSet = partitioner.partitionFile(secondInputFilePath.resolve("file2.txt"), secondPath, prefixSize).keySet();
            
            BlockedJoin join = new BlockedJoin();
            join.blockJoin(firstFileSet,secondFileSet,destPath,prefixSize);

        } catch (IOException e) {
            e.printStackTrace();
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time to execute join: " + estimatedTime);
    }
}

