import java.sql.Connection;
import java.sql.SQLException;

public class TestOracleConnection {

    public static void main(String[] args) {

        OracleConnector connector = new OracleConnector();
        connector.registerOracleDriver();

        Connection con = null;

        try {
            con = connector.getOracleConnection();

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (con != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }

        try {
            System.out.println("Close connection!");
            con.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        if (con != null) {
            System.out.println("Connection successfully closed!");
        } else {
            System.out.println("Failed to close connection!");
        }
    }
}
