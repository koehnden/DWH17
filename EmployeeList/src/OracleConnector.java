import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnector {
    final private static String URL = "jdbc:oracle:thin:";
    final private static String HOST = "@alibaba.informatik.hu-berlin.de";
    final private static String PORT = "1521";
    final private static String USERNAME = "OracleB";
    final private static String PASSWORD = "xxxxx";


    public Connection getOracleConnection() throws SQLException {
        StringBuilder driverURLBuilder =
                new StringBuilder(URL);

        driverURLBuilder.append(HOST + ":");
        driverURLBuilder.append(PORT);
        driverURLBuilder.append(":orcl");

        Connection connection = DriverManager
                .getConnection(driverURLBuilder.toString(), USERNAME, PASSWORD);

        return connection;
    }

    public void registerOracleDriver() {

        System.out.println("-------- Oracle JDBC Connection Testing ------");

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Registered!");
    }
}
