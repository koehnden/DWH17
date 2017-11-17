import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {

    public static Map<String,String[]> readCsv(String path) throws IOException {
        BufferedReader br = null;
        String line = "";
        Set<String> keySet = new HashSet<>();
        Map<String, String[]> employeeMap = new HashMap<>();
        br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            String[] employee = line.split(";",-1);
            String key = employee[5];
            if (key.isEmpty()) continue;
            employee[4] = convertRooms(employee[4]);
            employeeMap.put(key,employee);
        }
        br.close();
        return employeeMap;
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
}