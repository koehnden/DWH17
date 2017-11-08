import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.System.currentTimeMillis;

/**
 * This Program with prefixSize = 3 runs approximately 4min 15sec and never
 * exceeds 100mb result file is slightly smaller than in the slides 2087877
 * intstead of 2097858 (bug or wrong in the slides)
 */

public class Run {

	public static void main(String[] args) {
		Path firstInputFilePath = null;
		Path secondInputFilePath = null;
		Path destPath = null;
		long startTime = currentTimeMillis();
		try {
			firstInputFilePath = Paths.get(args[0]); // first source
			secondInputFilePath = Paths.get(args[0]); // second source
			destPath = Paths.get(args[0]); // destination
		} catch (ArrayIndexOutOfBoundsException ae) {
			System.out.println("Path not given. Take current folder.");
			firstInputFilePath = Paths.get("");
			secondInputFilePath = Paths.get("");
			destPath = Paths.get("");

			// System.exit(-1);
		}

		final Path firstPath = destPath.resolve("first");
		final Path secondPath = destPath.resolve("second");
		final int prefixSize = 3; // only prefixSize that does not exceed 100mb
		final String file1 = "simplefile1.txt";
		final String file2 = "simplefile2.txt";
		// heap size
		ExecutorService es = Executors.newFixedThreadPool(2);
		try {
			Partitioner partitioner = new Partitioner();
			
			

			// create folder for temporary partition files
			partitioner.createPartitionFileFolder(firstPath);
			partitioner.createPartitionFileFolder(secondPath);

			if(!new File(firstInputFilePath.resolve(file1).toString()).exists()){
				System.out.println("No Soruces in current directory. Goodby!");
				System.exit(-1);
			}
				
			// partition both files - parallel
			Future futFirstFileSet = es
					.submit(new Partitioner(firstInputFilePath.resolve(file1), firstPath, prefixSize));
			Future futSecondFileSet = es
					.submit(new Partitioner(secondInputFilePath.resolve(file2), secondPath, prefixSize));

			Set<String> firstFileSet = (Set<String>) futFirstFileSet.get();
			Set<String> secondFileSet =(Set<String>) futSecondFileSet.get();

			es.shutdown();

			/*
			 * Set<String> firstFileSet = partitioner
			 * .partitionFile(firstInputFilePath.resolve("simplefile1.txt"),
			 * firstPath, prefixSize).keySet(); Set<String> secondFileSet =
			 * partitioner
			 * .partitionFile(secondInputFilePath.resolve("simplefile2.txt"),
			 * secondPath, prefixSize).keySet();
			 */
			BlockedJoin join = new BlockedJoin();
			join.blockJoin(firstFileSet, secondFileSet, destPath, prefixSize);
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("Time to execute join: " + estimatedTime);
			System.exit(0);
		} catch (IOException | ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
