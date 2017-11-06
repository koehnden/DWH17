import java.io.*;
import java.nio.file.Path;
import java.util.Set;

public class SimpleWriter {

    public void writeResult(Set<String> resultSet, BufferedWriter resultWriter) throws IOException{
        for (String line : resultSet) {
            resultWriter.write(line);
            resultWriter.newLine();
        }
    }

    public BufferedWriter createBufferedWriter(Path path) throws IOException {
        return new BufferedWriter(new FileWriter(path.toFile(), true),100000);
    }
}