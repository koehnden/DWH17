import java.io.IOException;
import java.sql.*;
import java.util.*;

public class InsertEmployeeList {

    final static String csvPath = "mitarbeiter.csv";

    public static void main(String[] args){
        OracleConnector connector = new OracleConnector();
        connector.registerOracleDriver();
        Connection con = null;

        try {
            con = connector.getOracleConnection();
            dropTable(con);
            createEmployeeTable(con);
            List<String[]> employeeList = CSVReader.readCsv(csvPath);
            insertEmployees(employeeList, con);
        } catch (IOException ex){
            ex.printStackTrace();

        } catch (SQLException ex ) {
            ex.printStackTrace();
        }
    }


    private static void insertEmployees(List<String[]> employeeList, Connection con) throws SQLException {

        String sqlStmt = new StringBuilder("INSERT INTO mitarbeiter")
                .append(" (titel, vorname, nachname, lehrstuhl, raum, email, telefon, webseite) ")
                .append("VALUES (?")
                .append(String.join("", Collections.nCopies(7, ",?")))
                .append(") ")
                .toString();

        PreparedStatement insertEmployee = con.prepareStatement(sqlStmt);
        System.out.println("Fire Query: " + sqlStmt);

        Iterator<String[]> iter = employeeList.iterator();
        while(iter.hasNext()){
            String[] employee = iter.next();
            System.out.println("insert employee: " + employee[5]);
            for (int i = 1; i <= employee.length; i++){
                insertEmployee.setString(i,employee[i-1]);
            }
            insertEmployee.executeUpdate();
        }
        insertEmployee.close();
        System.out.println("All tuples successfully inserted");
        con.close();
    }

    private static void createEmployeeTable(Connection con) throws SQLException {
        String createStmt = new StringBuilder("CREATE TABLE mitarbeiter (")
                .append("titel VARCHAR2(50), ")
                .append("vorname VARCHAR2(20) NOT NULL, ")
                .append("nachname VARCHAR2(20) NOT NULL, ")
                .append("lehrstuhl VARCHAR2(80), ")
                .append("raum VARCHAR2(20), ")
                .append("email VARCHAR2(50) NOT NULL, ")
                .append("telefon VARCHAR2(20), ")
                .append("webseite VARCHAR2(150), ")
                .append("CONSTRAINT mitarbeiter_pk PRIMARY KEY (email)")
                .append(")")
                .toString();

        Statement stmt = null;

        try {
            System.out.println("Fire Query: " + createStmt);
            stmt = con.createStatement();
            stmt.executeQuery(createStmt);
        } catch (SQLException ex ) {
            System.out.println("create table failed");
            ex.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    private static boolean tableExists(String table, Connection con) throws SQLException {
        System.out.println("Check if table exists");
        DatabaseMetaData meta = con.getMetaData();
        ResultSet rs = meta.getTables(null, null, table, null);
        return rs.next();
    }

    private static void dropTable(Connection con) throws SQLException {
        Statement stmt = con.createStatement();;
        stmt.executeUpdate("DROP TABLE mitarbeiter ");
    }
}
