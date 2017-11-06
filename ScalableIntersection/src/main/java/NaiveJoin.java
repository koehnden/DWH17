import java.util.*;

public class NaiveJoin {

    // TODO: potenzieller Bug hier
    public Set<String> join(Map<Integer, Character> firstFile, Map<Integer, Character> secondFile) {
        Set<String> resultSet = new HashSet<>();
        System.out.println("Start Joining Files...");
        for (int firstKey : firstFile.keySet()) {
            if (secondFile.containsKey(firstKey)) {
                String stringKey = Integer.toString(firstKey);

                resultSet.add(firstFile.get(firstKey) + stringKey);
                resultSet.add(secondFile.get(firstKey) + stringKey);
            }
        }
        return resultSet;
    }
}
