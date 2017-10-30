import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuickTest {
    public static void main(String [] args) {
        final String path = "C:/Users/dennis.koehn/Uni/DWH/testData/";
        final String firstPath = path + "first/";
        final String secondPath = path + "second/";
        final int prefixSize = 2;

        try {
            // partition both files
            Partitioner partitioner = new Partitioner();
            partitioner.partitionFile(path + "test1.txt", firstPath, prefixSize);
            partitioner.partitionFile(path +"test2.txt", secondPath, prefixSize);

            // get all file names in "first" and "second" folder
            List<String> firstFileList = partitioner.listFileNames(firstPath);
            List<String> secondFileList = partitioner.listFileNames(secondPath);

            // join partitions if filename matches
            NaiveJoin joinMethod = new NaiveJoin();
            SimpleWriter writer = new SimpleWriter();
            SimpleReader reader = new SimpleReader();

            for (String firstFileName : firstFileList){
                for (String secondFileName : secondFileList) {
                    if (firstFileName.equals(secondFileName)){
                        Map<Long, Character> firstFile = reader.read(firstPath + firstFileName);
                        Map<Long, Character> secondFile = reader.read(secondPath + secondFileName);
                        Set<String> resultSet = joinMethod.join(firstFile, secondFile);
                        writer.write(resultSet, path + "result.txt");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}