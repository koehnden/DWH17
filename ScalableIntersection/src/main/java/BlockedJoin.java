import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BlockedJoin extends NaiveJoin
{
  public BlockedJoin() {}
  
  public void blockJoin(Set<String> firstFileList, Set<String> secondFileList, Path path, int prefixSize) throws IOException
  {
    System.out.println("[Info] Start Blocked Join...");
    SimpleWriter writer = new SimpleWriter();
    
    ExecutorService es = java.util.concurrent.Executors.newFixedThreadPool(2);
    Path firstPath = path.resolve("first");
    Path secondPath = path.resolve("second");
    BufferedWriter resultWriter = writer.createBufferedWriterOverwrite(path.resolve("result.txt"));
    try
    {
      Set<String> resultSet = new java.util.HashSet();
      for (String firstFileName : firstFileList)
      {
        if (secondFileList.contains(firstFileName))
        {

          Path firstFilePath = firstPath.resolve(firstFileName);
          Path secondFilePath = secondPath.resolve(firstFileName);
          Future<Map<Integer, List<Character>>> futFirstFile = es.submit(new SimpleReader(firstFilePath));
          Future<Map<Integer, List<Character>>> futSecondFile = es.submit(new SimpleReader(secondFilePath));
          
          Map<Integer, List<Character>> firstFile = (Map)futFirstFile.get();
          Map<Integer, List<Character>> secondFile = (Map)futSecondFile.get();
          
          resultSet.addAll(join(firstFile, secondFile));
          writer.writeResult(resultSet, firstFileName, prefixSize, resultWriter);
          
          resultSet.clear();
          cleanUp(firstFilePath, secondFilePath);
        }
      }
    } catch (InterruptedException|ExecutionException e) {
      e.printStackTrace();
    } finally {
      resultWriter.close();
    }
    System.out.println("[Info] Closing Result Writer...");
    
    System.out.println("[Info] Result in written in " + path.resolve("result.txt"));
    System.out.println("Done!");
  }
  
  private void cleanUp(Path firstPath, Path secondPath) throws IOException {
    Files.delete(firstPath);
    Files.delete(secondPath);
  }
}
