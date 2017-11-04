import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class Run {

    public static void main(String [] args) {

        final Path path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/files/");
        final Path firstPath = path.resolve("first");
        final Path secondPath = path.resolve("second");
        final int prefixSize = 2;

        try {
            Partitioner partitioner = new Partitioner();

            // create folder for temporary partition files
            partitioner.createPartitionFileFolder(firstPath);
            partitioner.createPartitionFileFolder(secondPath);

            // partition both files
            Set<String> firstFileSet = partitioner.partitionFile(path.resolve("file1.txt"), firstPath, prefixSize).keySet();
            Set<String> secondFileSet = partitioner.partitionFile(path.resolve("file2.txt"), secondPath, prefixSize).keySet();

            BlockedJoin join = new BlockedJoin();
            join.blockJoin(firstFileSet,secondFileSet,path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

