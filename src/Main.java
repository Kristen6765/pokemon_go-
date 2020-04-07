import org.postgresql.Driver;

import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

class connectExample {
        public static void main(String args[]) throws SQLException {


            // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
            String tableName = "";
            int sqlCode=0;      // Variable to hold SQLCODE
            String sqlState="00000";  // Variable to hold SQLSTATE

            if ( args.length > 0 ){
                tableName += args [ 0 ] ;
            }
            else {
                tableName += "example3.tbl";
            }


            // Register the driver.  You must register the driver before you can use it.
            try {
                DriverManager.registerDriver ( new Driver() ) ;
            } catch (Exception cnfe){
                System.out.println("Class not found");
            }


            // This is the url you must use for Postgresql.
            //Note: This url may not valid now !
            String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
            String usernamestring="cs421g36";
            String passwordstring="Dalao2020.Dalao2020@";
            Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
            Statement statement = con.createStatement ( ) ;



            // Querying a table
            try {
                String querySQL = "SELECT my_username from " + "befriends";
                System.out.println (querySQL) ;
                ResultSet rs = statement.executeQuery ( querySQL ) ;
                System.out.println("1");
                while ( rs.next ( ) ) {
                    System.out.println("2");
                    String my_usernmae = rs.getString (1) ;
                    System.out.println ("my_usernmae:  " + my_usernmae);

                }
                System.out.println ("DONE");
            } catch (SQLException e)
            {

                sqlCode = e.getErrorCode(); // Get SQLCODE
               sqlState = e.getSQLState(); // Get SQLSTATE

                // Your code to handle errors comes here;
                // something more meaningful than a print would be good
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            }

            statement.close ( ) ;
            con.close ( ) ;
        }



}

