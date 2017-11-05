import java.util.*;

public class NaiveJoin {

    public Set<String> joinOpt(Map<String, Character> firstFile, Map<String, Character> secondFile) {
        Set<String> resultSet = new HashSet<String>();
        System.out.println("Start Joining Files...");
        for (String firstKey : firstFile.keySet()) {
            for (String secondKey : secondFile.keySet()) {
                if (firstKey.equals(secondKey)) {
                    resultSet.add(firstFile.get(firstKey).toString() + firstKey);
                    resultSet.add(secondFile.get(secondKey).toString() + secondKey);
                }
            }
        }
        return resultSet;
    }

    public Set<String> join(Map<String, Character> firstFile, Map<String, Character> secondFile) {
        Set<String> resultSet = new HashSet<String>();
        System.out.println("Start Joining Files...");
        for (String firstKey : firstFile.keySet()) {
            if (secondFile.containsKey(firstKey)) {
                resultSet.add(firstFile.get(firstKey) + firstKey);
                resultSet.add(secondFile.get(firstKey) + firstKey);
            }
        }
        return resultSet;
    }
}
