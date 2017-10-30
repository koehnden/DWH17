import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockedJoin extends NaiveJoin {

    public void blockJoin(Path firstPath, Path secondPath, Path resultPath) throws IOException {

        System.out.println("Start Blocked Join...");

        SimpleWriter writer = new SimpleWriter();
        SimpleReader reader = new SimpleReader();

        // get all file names in "first" and "second" folder
        List<Path> firstFileList = Partitioner.listFiles(firstPath);
        List<Path> secondFileList = Partitioner.listFiles(secondPath);

        for (Path firstFilePath : firstFileList){
            for (Path secondFilePath : secondFileList) {
                if (firstFilePath.getFileName().equals(secondFilePath.getFileName())){
                    Map<Long, Character> firstFile = reader.read(firstPath.resolve(firstFilePath));
                    Map<Long, Character> secondFile = reader.read(secondPath.resolve(secondFilePath));
                    Set<String> resultSet = join(firstFile, secondFile);
                    writer.writeResult(resultSet, resultPath);
                    deleteJoinedFiles(firstFilePath,secondFilePath);
                }
            }
        }
        cleanUp(firstPath,secondPath);
    }


    private void cleanUp(Path firstPath, Path secondPath) throws IOException {
        Files.delete(firstPath);
        Files.delete(secondPath);
    }


    private void deleteJoinedFiles(Path firstFile, Path secondFile) throws IOException {
        Files.delete(firstFile);
        Files.delete(secondFile);
    }
}
