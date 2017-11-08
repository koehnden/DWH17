
import java.io.*;
import java.nio.file.Path;
import java.util.Set;

public class SimpleWriter {

    public void writeResult(Set<String> resultSet, String fileName, int prefixSize, BufferedWriter resultWriter) throws IOException {
        for (String line : resultSet) {
            String convertedResult = convertResult(line,fileName, prefixSize);
            resultWriter.write(convertedResult);
            resultWriter.newLine();
        }
    }

    private String convertResult(String line, String fileName, int prefixSize){
        StringBuilder builder = new StringBuilder();
        String result = builder
                .append(line.charAt(0))
                .append(fileName)
                .append(line.substring(1))
                .toString();
        return result;
    }

    public BufferedWriter createBufferedWriter(Path path) throws IOException {
        return new BufferedWriter(new FileWriter(path.toFile(), true));
    }
}