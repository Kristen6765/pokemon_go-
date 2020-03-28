import java.sql.*;
import java.util.Properties;

class connectExample {
        public static void main(String args[]) throws SQLException {

            String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
            Properties props = new Properties();
            props.setProperty("user", "cs421g36");
            props.setProperty("password", "Dalao2020.Dalao2020@");

            Connection conn=null;
            try {
                conn = DriverManager.getConnection(url, props);

                /**
                 String url = "jdbc:postgresql://localhost/test";
                 Properties props = new Properties();
                 props.setProperty("user","fred");
                 props.setProperty("password","secret");
                 props.setProperty("ssl","true");
                 Connection conn = DriverManager.getConnection(url, props);
                 String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
                 Connection conn = DriverManager.getConnection(url);

                 **/

            } catch(SQLException e) {

                System.out.println(e);
            } finally {
                conn.close();
            }

        }

    }

