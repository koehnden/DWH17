import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSize {

    public static void main(String [] args) {

        final Path path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/files/");

        // check if result is correct
        SimpleReader resultReader = new SimpleReader();

        try{
            String line = null;
            int counter = 0;
            BufferedReader bufferedReader = resultReader.createBufferedReader(path.resolve("result.txt"));
            while ((line = bufferedReader.readLine()) != null) {
                counter++;
            }

            System.out.println("Actual Size: " + counter);
            System.out.println("Expected Size: 2097858");
            System.out.println("Result Correct? " + (counter == 2097858));
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
