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

public class QuickTest {

    public static void main(String [] args) {

        int repetitions = 5;


        int i = 1;
        long totalTime = 0;

        while(i <= 5){
            long startTime = System.nanoTime();
            final Path path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/files/");
            final Path firstPath = path.resolve("first");
            final Path secondPath = path.resolve("second");
            final int prefixSize = 3;  // only prefixSize that does not exceed 100mb heap size

            try {
                Partitioner partitioner = new Partitioner();

                // create folder for temporary partition files
                partitioner.createPartitionFileFolder(firstPath);
                partitioner.createPartitionFileFolder(secondPath);

                // partition both files
                Set<String> firstFileSet = partitioner.partitionFile(path.resolve("file1_small.txt"), firstPath, prefixSize).keySet();
                Set<String> secondFileSet = partitioner.partitionFile(path.resolve("file2_small.txt"), secondPath, prefixSize).keySet();

                BlockedJoin join = new BlockedJoin();
                join.blockJoin(firstFileSet,secondFileSet,path,prefixSize);

            } catch (IOException e) {
                e.printStackTrace();
            }

            long estimatedTime = System.nanoTime() - startTime;
            totalTime += estimatedTime;
            i++;

            System.out.println("Time to execute join: " + estimatedTime);
            System.out.println("Best Time measured: 12072307959");
            System.out.println("Is new Program faster:" + (estimatedTime < 12072307959L));
        }
        System.out.println("------ Average Time to execute join: " + totalTime/i);
    }


}
