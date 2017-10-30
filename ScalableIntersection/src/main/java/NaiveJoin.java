import java.util.*;

public class NaiveJoin {

    public Set<String> join(Map<Long, Character> firstFile, Map<Long, Character> secondFile) {
        Set<String> resultSet = new HashSet<String>();

        for (long firstKey : firstFile.keySet()) {
            for (long secondKey : secondFile.keySet()) {
                if (firstKey == secondKey) {
                    System.out.println("Pairs Matching");
                    resultSet.add(firstFile.get(firstKey).toString() + firstKey);
                    resultSet.add(secondFile.get(secondKey).toString() + secondKey);
                }
            }
        }
        if (resultSet.isEmpty()){
            System.out.println("No matching keys");
        }
        return resultSet;
    }
}
