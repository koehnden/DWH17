import java.io.*;
import java.util.Set;

public class SimpleWriter {

    public void write(Set<String> resultSet, String filePath){
        try {
            for (String line : resultSet) {
                writeNewOrExistitingFile(line, filePath);
            }
            System.out.println("DONE");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeNewOrExistitingFile(String line, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.createNewFile()){
            writeIntoExistingFile(line,filePath);
        } else {
            writeIntoNewFile(line,filePath);
        }
    }

    public void writeIntoNewFile(String line, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(line);
        writer.close();
    }

    public void writeIntoExistingFile(String line, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
        writer.newLine();
        writer.write(line);
        writer.close();
    }
}