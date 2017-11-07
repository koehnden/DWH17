import java.util.*;

public class NaiveJoin {

    // TODO: potenzieller Bug hier
    public Set<String> join(Map<Integer, List<Character>> firstFile, Map<Integer, List<Character>> secondFile) {
        Set<String> resultSet = new HashSet<>();
        System.out.println("Start Joining Files...");
        for (int firstKey : firstFile.keySet()) {
            if (secondFile.containsKey(firstKey)) {
                String stringKey = Integer.toString(firstKey);
               	
                if(stringKey.length() < 7) stringKey = String.format("%0" + (7 - stringKey.length()) + "d", 0) + stringKey;

                for(Character character : firstFile.get(firstKey) )
                	resultSet.add(character + stringKey);
                
                for(Character character : secondFile.get(firstKey) )
                	resultSet.add(character + stringKey);
            }
        }
        return resultSet;
    }
}
