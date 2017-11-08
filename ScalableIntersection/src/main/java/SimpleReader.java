import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class SimpleReader implements java.util.concurrent.Callable<Map<Integer, List<Character>>>
{
  Path path;
  
  public SimpleReader() {}
  
  public SimpleReader(Path path)
  {
    this.path = path;
  }
  
  public Map<Integer, List<Character>> read(Path path) throws IOException {
    System.out.println("Start reading data from path: " + path + "...");
    String line = null;
    Map<Integer, List<Character>> lineMap = new java.util.HashMap();
    
    BufferedReader bufferedReader = createBufferedReader(path);
    while ((line = bufferedReader.readLine()) != null) {
      int key = Integer.parseInt(line.substring(1));
      Character value = Character.valueOf(line.charAt(0));
      
      List<Character> list = null;
      
      if (lineMap.containsKey(Integer.valueOf(key))) {
        list = (List)lineMap.get(Integer.valueOf(key));
        if (!list.contains(value)) list.add(value);
      } else {
        list = new java.util.LinkedList();
        list.add(value);
      }
      lineMap.put(Integer.valueOf(key), list);
    }
    bufferedReader.close();
    return lineMap;
  }
  
  public BufferedReader createBufferedReader(Path path) throws IOException
  {
    FileReader fileReader = new FileReader(path.toFile());
    return new BufferedReader(fileReader);
  }
  

  public Map<Integer, List<Character>> call()
    throws Exception
  {
    return read(path);
  }
}
