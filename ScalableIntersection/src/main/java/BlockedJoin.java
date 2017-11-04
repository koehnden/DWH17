import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockedJoin extends NaiveJoin {

    public void blockJoin(Set<String> firstFileList, Set<String> secondFileList, Path path) throws IOException {

        System.out.println("[Info] Start Blocked Join...");
        SimpleWriter writer = new SimpleWriter();
        SimpleReader reader = new SimpleReader();

        Path firstPath = path.resolve("first");
        Path secondPath = path.resolve("second");

        BufferedWriter resultWriter = writer.createBufferedWriter(path.resolve("result.txt"));

        for (String firstFileName : firstFileList){
            for (String secondFileName : secondFileList) {
                if (firstFileName.equals(secondFileName)){
                    System.out.println(firstFileName + " second: " + secondFileName );
                    // create paths and read
                    Path firstFilePath = firstPath.resolve(firstFileName);
                    Path secondFilePath = secondPath.resolve(secondFileName);
                    Map<Long, Character> firstFile = reader.read(firstFilePath);
                    Map<Long, Character> secondFile = reader.read(secondFilePath);
                    // join and write result
                    Set<String> resultSet = new HashSet<String>();
                    resultSet.addAll(join(firstFile, secondFile));
                    writer.writeResult(resultSet, resultWriter);
                    // delete files after join
                    cleanUp(firstFilePath,secondFilePath);
                }
            }
        }
        System.out.println("[Info] Closing Result Writer...");
        resultWriter.close();
        System.out.println("[Info] Result in written in " + path.resolve("result.txt"));
        System.out.println("Done!");
    }


    private void cleanUp(Path firstPath, Path secondPath) throws IOException {
        Files.delete(firstPath);
        Files.delete(secondPath);
    }
}
