import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class Partitioner implements Callable<Set<String>> {
	Path inputPath;
	Path outputPath;
	int prefixSize;

	public Partitioner() {
	}

	public Partitioner(Path inputPath, Path outputPath, int prefixSize) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.prefixSize = prefixSize;
	}

	public Map<String, BufferedWriter> partitionFile(Path inputPath, Path outputPath, int prefixSize)
			throws IOException {
		String line = null;
		SimpleReader reader = new SimpleReader();

		System.out.println("[Info] Start Partioning file from path " + inputPath + "...");
		BufferedReader bufferedReader = reader.createBufferedReader(inputPath);
		Map<String, BufferedWriter> writerMap = createPartitionFiles(outputPath, prefixSize);
		System.out.println("Files created!");

		System.out.println("[Info] Filling the partition files...");
		while ((line = bufferedReader.readLine()) != null) {
			String hashKey = createHashKey(extractKey(line), prefixSize);
			Path filePath = outputPath.resolve(hashKey);
			BufferedWriter currentWriter = getBufferedWriter(writerMap, filePath);
			writerMap.remove(filePath);
			currentWriter.write(createEntry(line, prefixSize));
			currentWriter.newLine();
		}
		System.out.println("[Info]" + inputPath + " was successfully partitioned into " + outputPath);
		System.out.println("[Info] Close all Files...");
		bufferedReader.close();
		closeAllWriters(writerMap);
		return writerMap;
	}

	private BufferedWriter getBufferedWriter(Map<String, BufferedWriter> writerMap, Path filePath) {
		StringBuilder builder = new StringBuilder();
		String fileName = filePath.getFileName();
		return (BufferedWriter) writerMap.get(fileName);
	}

	private String createHashKey(String key, int prefixSize) {
		return key.substring(0, prefixSize);
	}

	private String extractKey(String line) {
		return line.substring(1);
	}

	private String createEntry(String line, int prefixSize) {
		StringBuilder builder = new StringBuilder(line);
		String entry = builder.delete(1, prefixSize + 1).toString();
		return entry;
	}

	public Map<String, BufferedWriter> createPartitionFiles(Path path, int prefixSize) throws IOException {
		System.out.println("Create File for Partitions");
		Map<String, BufferedWriter> writerMap = new HashMap();
		int fileInt = (int) Math.pow(10.0D, prefixSize - 1);
		int maxNumber = createMaxNumber(prefixSize);
		while (fileInt <= maxNumber) {
			String fileName = Integer.toString(fileInt);
			File file = path.resolve(fileName).toFile();
			BufferedWriter newWriter = new BufferedWriter(new FileWriter(file));
			writerMap.put(fileName, newWriter);
			fileInt++;
		}
		return writerMap;
	}

	public void createPartitionFileFolder(Path path) {
		new File(path.toString()).mkdir();
	}

	public int createMaxNumber(int prefixSize) {
		String maxString = String.format("%0" + prefixSize + "d", new Object[] { Integer.valueOf(0) }).replace("0",
				"9");
		return Integer.parseInt(maxString);
	}

	public static void closeAllWriters(Map<String, BufferedWriter> writerMap) throws IOException {
		for (String key : writerMap.keySet()) {
			((BufferedWriter) writerMap.get(key)).close();
		}
	}

	public Set<String> call() throws Exception {
		return partitionFile(inputPath, outputPath, prefixSize).keySet();
	}
}
