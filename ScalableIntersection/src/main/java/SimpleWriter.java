import java.io.*;
import java.nio.file.Path;
import java.util.Set;

public class SimpleWriter {

    public void writeResult(Set<String> resultSet, Path filePath){
        System.out.println("Start writing result into " + filePath + "...");
        try {
            for (String line : resultSet) {
                writeNewOrExistitingFile(line, filePath);
                System.out.println(filePath.toString());
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeNewOrExistitingFile(String line, Path filePath) throws IOException {
        File file = new File(filePath.toString());
        if (!file.createNewFile()){
            writeIntoExistingFile(line,filePath);
        } else {
            writeIntoNewFile(line,filePath);
        }
    }

    public void writeIntoNewFile(String line, Path filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        writer.write(line);
        writer.close();
    }

    public void writeIntoExistingFile(String line, Path filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true));
        writer.newLine();
        writer.write(line);
        writer.close();
    }
}