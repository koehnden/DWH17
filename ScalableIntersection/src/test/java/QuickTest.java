import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QuickTest {

	static Timestamp startTime = new Timestamp(System.currentTimeMillis());

	public static void main(String[] args) {

		final Path path = Paths.get("C:\\Users\\sirwiks\\workspace\\DWH1");
		final Path firstPath = path.resolve("first");
		final Path secondPath = path.resolve("second");
		final int prefixSize = 1;
		ExecutorService es = Executors.newFixedThreadPool(2);

		try {
			// System.out.println("Start create file folder:\t"+startTime)
			Partitioner partitioner = new Partitioner();

			// create folder for temporary partition files
			partitioner.createPartitionFileFolder(firstPath);
			partitioner.createPartitionFileFolder(secondPath);
			printTimeStamp("[info] create folder took");

			// partition both files
			// Set<String> firstFileSet =
			// partitioner.partitionFile(path.resolve("simplefile1.txt"),
			// firstPath, prefixSize).keySet();
			Future futureFirstFileSet = es
					.submit(new Partitioner(path.resolve("simplefile1.txt"), firstPath, prefixSize));
			Future futureSecondFileSet = es
					.submit(new Partitioner(path.resolve("simplefile2.txt"), secondPath, prefixSize));
			es.shutdown();
			Set<String> firstFileSet = (Set<String>) futureFirstFileSet.get();
			Set<String> firstSecondSet = (Set<String>) futureSecondFileSet.get();

			printTimeStamp("[info] partitioning took");

			// BlockedJoin join = new BlockedJoin();
			// join.blockJoin(firstFileSet,secondFileSet,path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void printTimeStamp(String message) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		System.out.println(message + "\t" + ((currentTime.getSeconds()) - (startTime.getSeconds()) + "s"));
		startTime = currentTime;
	}
}