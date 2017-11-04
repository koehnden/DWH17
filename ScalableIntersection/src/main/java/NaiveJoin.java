import java.util.*;

public class NaiveJoin {

    public Set<String> join(Map<Long, Character> firstFile, Map<Long, Character> secondFile) {
        Set<String> resultSet = new HashSet<String>();
        System.out.println("Start Joining Files...");
        for (long firstKey : firstFile.keySet()) {
            for (long secondKey : secondFile.keySet()) {
                if (firstKey == secondKey) {
                    resultSet.add(firstFile.get(firstKey).toString() + firstKey);
                    resultSet.add(secondFile.get(secondKey).toString() + secondKey);
                }
            }
        }
        return resultSet;
    }
}
