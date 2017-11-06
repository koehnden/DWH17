
/**
 * Partitions a File in to multiple Files based on the keys
 * Lines with common prefix in the key get written in the same file
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.jmx.snmp.Timestamp;

import jdk.management.resource.internal.FutureWrapper;
import jdk.management.resource.internal.inst.FileInputStreamRMHooks;

public class Partitioner implements Callable<Set<String>> {
	Path inputPath;
	Path outputPath;
	int prefixSize;
	final int linesToWrite = 10000;

	public Partitioner() {
	}

	public Partitioner(Path inputPath, Path outputPath, int prefixSize) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.prefixSize = prefixSize;
	}

	public Map<String, ParallelWriter> partitionFile(Path inputPath, Path outputPath, int prefixSize)
			throws InterruptedException, ExecutionException {
		String line = null;
		// SimpleReader reader = new SimpleReader();
		BufferedReader in = null;

		System.out.println("[Info] Start Partioning file from path " + inputPath);
		// BufferedReader bufferedReader;
		Map<String, ParallelWriter> writerMap = null;
		ExecutorService es = Executors.newFixedThreadPool(100);
		try {
			in = (BufferedReader) getStreamReader(inputPath);

			writerMap = createPartitionFiles(outputPath, prefixSize);
			System.out.println("Files created! ");
			System.out.println("[Info] Filling the partition files...");

			HashMap<String, LineWriterAsync> lineWriters = new HashMap<String, LineWriterAsync>();
			while ((line = in.readLine()) != null) {
				String hashKey = createHashKey(extractKey(line), prefixSize);
				Path filePath = outputPath.resolve(hashKey);
				String fileName = filePath.getFileName().toString();
				ParallelWriter currentWriter = writerMap.get(fileName + ".txt");
//				if(currentWriter.getSize()<linesToWrite){
					writerMap.get(fileName + ".txt").append((CharSequence) line).newLine();
//				}else{
//					currentWriter.run();
//				}

				//currentWriter.append((CharSequence) line).newLine();
				//currentWriter.run();
			}
			for(Map.Entry<String, ParallelWriter> entry:writerMap.entrySet()){
				entry.getValue().run();
			}

			System.out.println("[Info]" + inputPath + " was successfully partitioned into " + outputPath);
			System.out.println("[Info] Close all Files...");
			in.close();

		} catch (IOException ex) {
			System.out.println("partitioning error: " + ex.getMessage());
			/*
			 * } finally { for (Map.Entry<String, BufferedWriter> entry :
			 * writerMap.entrySet()) { try { entry.getValue().close(); return
			 * writerMap; } catch (IOException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } }
			 */
		}

		return writerMap;
	}

	private String createHashKey(String key, int prefixSize) {
		return key.substring(0, prefixSize);
	}

	private String extractKey(String line) {
		return line.substring(1);
	}

	public Map<String, ParallelWriter> createPartitionFiles(Path path, int prefixSize)
			throws IOException, InterruptedException, ExecutionException {
		System.out.println("Create File for Partitions");
		Map<String, ParallelWriter> writerMap = new HashMap<String, ParallelWriter>();
		int fileInt = (int) Math.pow(10, prefixSize - 1);
		int maxNumber = createMaxNumber(prefixSize);

		ExecutorService es = Executors.newCachedThreadPool();
		while (fileInt <= maxNumber) {
			String fileName = Integer.toString(fileInt) + ".txt";
			// File tmpFile = File.createTempFile(filePath, "txt");
			File file = path.resolve(fileName).toFile();
			// Future futWriter=es.submit(new
			// SimpleWriterCreateAsync(path,fileName));
			ParallelWriter writer = new ParallelWriter(file);
			new Thread(writer).start();
			// BufferedWriter newWriter = new BufferedWriter(new
			// FileWriter(file));
			writerMap.put(fileName, writer);
			fileInt++;
		}

		es.shutdown();
		return writerMap;
	}

	public void createPartitionFileFolder(Path path) {
		new File(path.toString()).mkdir();
	}

	public int createMaxNumber(int prefixSize) {
		String maxString = String.format("%0" + prefixSize + "d", 0).replace("0", "9");
		return Integer.parseInt(maxString);
	}

	public static List<Path> listFiles(Path path) throws IOException {
		System.out.println("Listing all files!");
		List<Path> files = new ArrayList<>();
		Stream<Path> stream = Files.walk(path);
		stream.forEach(files::add);
		return files;
	}

	public Reader getStreamReader(Path path) throws FileNotFoundException {
		// InputStream in = DataInputStream(new BufferedInputStream(new
		// FileInputStream(path.toFile())));
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path.toFile()));
		// InputStream in = new FileInputStream(path.toFile());
		BufferedReader in = new BufferedReader(isr);
		return in;
	}

	@Override
	public Set<String> call() throws Exception {
		// TODO Auto-generated method stub
		return this.partitionFile(this.inputPath, this.outputPath, this.prefixSize).keySet();
	}
}
