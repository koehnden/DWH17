import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CSVReader {

    public static List<String[]> readCsv(String path) throws IOException {
        BufferedReader br = null;
        String line = "";
        Set<String> keySet = new HashSet<>();
        List<String[]> employeeList = new ArrayList<String[]>();
        br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            String[] employee = line.split(";",-1);
            // filter out duplicates
            String key = employee[5];
            if (key.isEmpty() || keySet.contains(key)) continue;
            keySet.add(key);
            employee[4] = convertRooms(employee[4]);
            employeeList.add(employee);
        }
        br.close();
        return employeeList;
    }

    private static String convertRooms(String room){
        String formattedRoom = null;
        if (!room.isEmpty()){
            formattedRoom = room.substring(0,6)+ '.' + room.substring(7);
        } else {
            formattedRoom = room;
        }
        return formattedRoom;
    }

    private static void filerOutDuplicates(String currentKey, Set<String> keySet){

    }
}