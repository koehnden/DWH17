import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;

public class SimpleWriterCreateAsync implements Callable <BufferedWriter>{
	Path path;
	String filename;
	public SimpleWriterCreateAsync(){};
	public SimpleWriterCreateAsync(Path path, String filename){
		this.path=path;
		this.filename=filename;
	};
    public void writeResult(Set<String> resultSet, BufferedWriter resultWriter) throws IOException{
        for (String line : resultSet) {
            resultWriter.write(line);
            resultWriter.newLine();
        }
    }

    public BufferedWriter createBufferedWriter() throws IOException {
        return new BufferedWriter(new FileWriter(path.resolve(filename).toFile(), true));
        
    }

	@Override
	public BufferedWriter call() throws Exception {		
		return this.createBufferedWriter();
	}
}