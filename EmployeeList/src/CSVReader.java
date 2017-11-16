import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String[]> readCsv(String path) throws IOException {
        BufferedReader br = null;
        String line = "";
        List<String[]> employeeList = new ArrayList<String[]>();
        br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            String[] employee = line.split(";",-1);
            if (employee[5].isEmpty()) continue;
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
}