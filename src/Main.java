import org.postgresql.Driver;

import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

class connectExample {
        public static void main(String args[]) throws SQLException {
            System.out.println("wWlcome to pokemongo!");
            System.out.println("Are you a new user? If yes plase press 0 to create a new account.");
            Scanner scr = new Scanner(System.in);
            int newPlayer = scr.nextInt();
            if(newPlayer==0){
                int sqlCode=0;      // Variable to hold SQLCODE
                String sqlState="00000";  // Variable to hold SQLSTATE
                createAccount(sqlCode,sqlState);
            }

            //if player already have an account, start the game
            initializeGame();


            }

    /**
     * game start here
     * prompt user's choice
     * then call the coresponding method
     */
    public static void initializeGame() throws SQLException {
        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver ( new Driver() ) ;
        } catch (Exception cnfe){
            System.out.println("Class not found");
        }
        int sqlCode=0;      // Variable to hold SQLCODE
        String sqlState="00000";  // Variable to hold SQLSTATE

        System.out.println("now you can: 1. capture pokemon" +
                "2. add friend " +
                "3. add a group " +
                "4. check your bag " +
                "5. raid a boss ");

        System.out.println("now go ahead and select one by type the number coresponding to it " +
                "to start your adventure!" +
                "");


        //promot from user
        Scanner userChoice = new Scanner(System.in);
        int choice = userChoice.nextInt();
        //magic door to the method
        switch(choice) {
            case 1:
                // code block
                break;
            case 2:
               //add friend
                addFriend(sqlCode,sqlState);
                break;
            case 3:
                // code block
                addGroup(sqlCode,sqlState);
                break;
            case 4:
                // check bag
                checkBag(sqlCode,sqlState);
                break;
            case 5:
                // code block
                break;
            default:
                // code block
        }




    }
    static void  createAccount(int sqlCode,String sqlState) throws SQLException {
        // This is the url you must use for Postgresql.
        //Note: This url may not valid now !
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        String usernamestring="cs421g36";
        String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;

        int maxCurrentID=-1;
        //get all code from Groups
        // Querying a table
        try {
            String querySQL = "SELECT max(code) from groups";
            System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //generate a new id that does not in the db
            while ( rs.next ( ) ) {
                 maxCurrentID = rs.getInt (0) ;
                System.out.println ("max is :  " + maxCurrentID);

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
       int newCode= maxCurrentID+1;

        //add this code to table group first
        // Inserting Data into the table (befriends)
        try {

            String insertSQL = "INSERT INTO  groups VALUES ("+newCode+")" ;
            System.out.println ( insertSQL ) ;
            statement.executeUpdate ( insertSQL ) ;
            System.out.println ( "DONE" ) ;
        }catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }




        //get name from user and add name and code to table Players
        System.out.println("Hi, I'll help you to create a new account, please create a new name.");

    }


    /*
    add friend
     */
    static void  addFriend(int sqlCode,String sqlState) throws SQLException {

        // This is the url you must use for Postgresql.
        //Note: This url may not valid now !
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        String usernamestring="cs421g36";
        String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;



        System.out.println(" ");
        System.out.println("Please type in your friend's name");
        Scanner fName = new Scanner(System.in);
        String friendName = fName.nextLine();
        System.out.println("Please type in your name");
        Scanner uName = new Scanner(System.in);
        String userName = uName.nextLine();

        // Inserting Data into the table (befriends)
        try {

            //Drew Ima2
//Wendy Ima
            String insertSQL = "INSERT INTO  befriends(friends_username, my_username) VALUES ("+friendName+","+ userName +") " ;
            System.out.println ( insertSQL ) ;
            statement.executeUpdate ( insertSQL ) ;
            System.out.println ( "DONE" ) ;


        }catch (SQLException e)
        {
            System.out.println("122");
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }



        //print out all friends of user
        System.out.println("now you are friend with: ");
        // Querying a table
            try {
                System.out.println(userName);
                String querySQL = "SELECT friends_username from " + "befriends where username= '" + userName +"'";

                statement=con.prepareStatement(querySQL);

                ((PreparedStatement) statement).setString(1, friendName);

                System.out.println (querySQL) ;

                ResultSet rs = statement.executeQuery ( querySQL ) ;
               // System.out.println("1");
                while ( rs.next ( ) ) {
                //    System.out.println("2");
                    String friends_username = rs.getString (0) ;
                    System.out.println ("my_usernmae:  " + friends_username);

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

    static void  checkBag(int sqlCode,String sqlState) throws SQLException {
        System.out.println("you you want to chech bag? yes/no");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if(choice=="no"){
            return;
        }

            //connect
            String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
            String usernamestring="cs421g36";
            String passwordstring="Dalao2020.Dalao2020@";
            Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
            Statement statement = con.createStatement ( ) ;


            System.out.println("please insert your user name");
            Scanner sc = new Scanner(System.in);
            String userName = sc.nextLine();
            // String userName="Alice";
            // Querying a table
            try {
                String querySQL = "SELECT pokename from " + "capturablepokemons where username= '" + userName +"'";

                System.out.println (querySQL) ;
                ResultSet rs = statement.executeQuery ( querySQL ) ;

                //print all pokenames the user has
                while ( rs.next ( ) ) {
                    String pokename = rs.getString (1) ;
                    System.out.println ("pokename:  " + pokename);

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

            return;

    }

    /**
     * if group contain less than 20 members, the user canbe added to the group
     * @param sqlCode
     * @param sqlState
     * @throws SQLException
     */
    static void  addGroup(int sqlCode,String sqlState) throws SQLException {
        // This is the url you must use for Postgresql.
        //Note: This url may not valid now !
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        String usernamestring="cs421g36";
        String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;




    }

}


