import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestInsert {

    public static void main(String[] args){
        OracleConnector connector = new OracleConnector();

        try{
            Connection con = connector.getOracleConnection();
            ResultSet rs = selectData(con);
            System.out.println("Has entries? " + rs.next());
            while(rs.next()){
                System.out.println(rs.getMetaData());
                String email = rs.getString("email");
                System.out.println(email);
            }
            con.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }

    }


    private static ResultSet selectData(Connection con) throws SQLException{
        String query = "SELECT email FROM mitarbeiter";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("Fire Query: " + query);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex ) {
            System.out.println("select failed");
            ex.printStackTrace();
        }
        return rs;
    }

}
