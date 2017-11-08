import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NaiveJoin {

	// TODO: potenzieller Bug hier
	public Set<String> join(Map<Integer, List<Character>> firstFile, Map<Integer, List<Character>> secondFile) {
		Set<String> resultSet = new HashSet<>();
		System.out.println("Start Joining Files...");

		for (int firstKey : firstFile.keySet()) {

			if (secondFile.containsKey(firstKey)) {
				String stringKey = Integer.toString(firstKey);

				if (stringKey.length() < 7)
					stringKey = String.format("%0" + (7 - stringKey.length()) + "d", 0) + stringKey;
				/*
				 * Future<Set<String>> futResFirstFile = es.submit(new
				 * ScanForKey(stringKey, firstKey, firstFile));
				 * Future<Set<String>> futResSecondFile = es.submit(new
				 * ScanForKey(stringKey, firstKey, secondFile)); try {
				 * resultSet.addAll(futResFirstFile.get());
				 * resultSet.addAll(futResSecondFile.get()); es.shutdown(); }
				 * catch (InterruptedException | ExecutionException e) {
				 * e.printStackTrace(); }
				 **/

				for (Character character : firstFile.get(firstKey))
					resultSet.add(character + stringKey);

				for (Character character : secondFile.get(firstKey))
					resultSet.add(character + stringKey);

			}
		}
		return resultSet;
	}

	private class ScanForKey implements Callable<Set<String>> {
		int fileKey;
		String currentKey;
		Map<Integer, List<Character>> file;

		public ScanForKey(String currentKey, int firstKey, Map<Integer, List<Character>> file) {
			this.currentKey = currentKey;
			this.fileKey = firstKey;
			this.file = file;
		}

		private Set<String> scan(String currentKey, int fileKey, Map<Integer, List<Character>> file) {
			Set<String> resultSet = new HashSet<>();
			for (Character character : file.get(fileKey))
				resultSet.add(character + currentKey);
			return resultSet;
		}

		@Override
		public Set<String> call() throws Exception {

			return this.scan(currentKey, fileKey, file);
		}

	}
}
