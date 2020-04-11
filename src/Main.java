import org.postgresql.Driver;

import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

class connectExample {
        private static final String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        private static final String usernamestring="cs421g36";
        private static final String passwordstring="Dalao2020.Dalao2020@";

        private static String my_username = "";

        public static void main(String args[]) throws SQLException {
            System.out.println("Welcome to pokemongo!");
            System.out.println("Do you already have an account? Type 'yes' or 'no'");
            Scanner scr = new Scanner(System.in);
            String newPlayer = scr.nextLine();
            if(newPlayer=="no") {
                int sqlCode = 0;      // Variable to hold SQLCODE
                String sqlState = "00000";  // Variable to hold SQLSTATE
                createAccount(sqlCode, sqlState);
            }

            Scanner userChoice = new Scanner(System.in);
            System.out.println("Please enter your username to log in.");
            String ans = userChoice.nextLine();
            Boolean doesnotexist = checkIfUserExists(ans);

            while (doesnotexist == false) {
                System.out.println("Username does not exist. Please enter your username again");
                ans = userChoice.nextLine();
                doesnotexist = checkIfUserExists(ans);
            }

            my_username = ans;

            try {
                DriverManager.registerDriver ( new Driver() ) ;
            } catch (Exception cnfe){
                System.out.println("Class not found");
            }

            //if player already have an account, start the game
            Boolean play = true;
            while (play) {
                play = playGame();
            }

            userChoice.close();
            System.out.println("Thank you for playing!");
            }

    /**
     * game start here
     * prompt user's choice
     * then call the coresponding method
     */

    public static Boolean playGame() throws SQLException {
        // Register the driver.  You must register the driver before you can use it.
       // System.out.println("Please enter your username to log in.");
        int sqlCode=0;      // Variable to hold SQLCODE
        String sqlState="00000";  // Variable to hold SQLSTATE
        Scanner userChoice = new Scanner(System.in);
       // my_username = userChoice.nextLine();

        System.out.println("now you can: " +
                        "1. quit" +
                "2. add friend " +
                "3. capture pokemon " +
                "4. check your bag "
                );

        System.out.println("now go ahead and select one by type the number coresponding to it " +
                "to start your adventure!" +
                "");


        //prompt from user
        //Scanner userChoice = new Scanner(System.in);
        int choice = userChoice.nextInt();
        //magic door to the method
        switch(choice) {
            case 1:
                // quit
                System.out.println("Do you want to quit? Type 'yes' or 'no'");
                String ans = userChoice.nextLine();
                if (ans.equals("yes"))  {
                    userChoice.close();
                    return false;
                }
                break;
            case 2:
               //add friend
                addFriend(sqlCode,sqlState);
                break;
            case 3:
                // capture pokemon;

                break;
            case 4:
                // check bag
                checkBag(sqlCode,sqlState);
                break;
            case 5:
                break;
            default:
                // code block
                return true;
        }

        userChoice.close();
        return true;
    }

    static Boolean checkIfUserExists(String test_user) throws SQLException {
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;
        int sqlCode;      // Variable to hold SQLCODE
        String sqlState;  // Variable to hold SQLSTATE
        int count = 0;

        try {
            String querySQL = "SELECT COUNT(code) FROM Players WHERE username ='" + test_user + "'";
            //System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //create an unique code not in db yet
            while ( rs.next ( ) ) {
                count = rs.getInt("count") ;
                //System.out.println (count);
            }
            //System.out.println ("DONE");
        } catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }

        // now ask user which user code they are

        if (count == 0) {
            return false;      //user does not exist
        }

        System.out.println("Successfully logged in.");
        int code = 0;

        try {
            String querySQL = "SELECT * FROM Players WHERE username ='" + test_user + "'";
            //System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //create an unique code not in db yet
            while ( rs.next ( ) ) {
                String username = rs.getString("username") ;
                System.out.print("Username: " + username + "\t");
                int level = rs.getInt("level");
                System.out.print("Level: " + level + "\t");
                code = rs.getInt("code");
                System.out.print("Code: " + code);
            }
            System.out.println();
            //System.out.println ("DONE");
        } catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            // System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }


        try {
            String querySQL = "SELECT username FROM Players WHERE code =" + code;
            //System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //create an unique code not in db yet
            System.out.println("You're currently in a group with the following people: ");
            while ( rs.next ( ) ) {
                String friend = rs.getString("username");
                if (friend.equals(test_user) == false) {
                    System.out.print(friend + "\t");
                }
            }
            //System.out.println ("DONE");
            System.out.println();
        } catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            //System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }

        statement.close();
        con.close();

        return true;

    }


    static void  createAccount(int sqlCode,String sqlState) throws SQLException {
        // This is the url you must use for Postgresql.
        //Note: This url may not valid now !
        //String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        //String usernamestring="cs421g36";
        //String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;
        int maxCode=0;
        int maxCurrentID=-1;
        //get all code from Groups
        // Querying a table
        try {
            String querySQL = "SELECT max(code) from groups;";
            System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //create an unique code not in db yet
            while ( rs.next ( ) ) {
                 maxCode = rs.getInt (1) ;
                System.out.println ("maxCode:  " + maxCode);
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

        int newCode= maxCode+1;

        try {
            String insertSQL = "INSERT INTO  groups VALUES ("+newCode+")" ;
            System.out.println (insertSQL) ;
            statement.executeUpdate (insertSQL) ;
            System.out.println ("DONE");
        } catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }


        //get name from user and add name and code to table Players
        System.out.println("Hi, I'll help you to create a new account, please create a new name.");
        Scanner userChoice = new Scanner(System.in);
        String name = userChoice.nextLine();
        //magic door to the method
        try {

            String insertSQL = "INSERT INTO  players VALUES (\'"+name+"\',0,\'"+newCode+"\')" ;
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
        userChoice.close();
    }


    /*
    add friend
     */
    static void  addFriend(int sqlCode,String sqlState) throws SQLException {

        // This is the url you must use for Postgresql.
        //Note: This url may not valid now !
       // String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
       // String usernamestring="cs421g36";
       // String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;



        System.out.println(" ");
        System.out.println("Please type in your friend's name");
        Scanner fName = new Scanner(System.in);
        String friendName = fName.nextLine();
        fName.close();
        //System.out.println("Please type in your name");
        //Scanner uName = new Scanner(System.in);
        //String userName = uName.nextLine();
        String userName = my_username;

        // Inserting Data into the table (befriends)
        try {
            String insertSQL = "INSERT INTO  befriends(friends_username, my_username) VALUES (\'"+friendName+"\',\'"+ userName +"\') " ;
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
            statement.close();
            con.close();
        }



        //print out all friends of user
        System.out.println("now you are friend with: "+friendName);

        statement.close ( ) ;
        con.close ( ) ;
    }

    static void  checkBag(int sqlCode,String sqlState) throws SQLException {
        System.out.println("you you want to check bag? yes/no");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if(choice=="no"){
            return;
        }

            //connect
          //  String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
           // String usernamestring="cs421g36";
           // String passwordstring="Dalao2020.Dalao2020@";
            Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
            Statement statement = con.createStatement ( ) ;


            //System.out.println("please insert your user name");
            //Scanner sc = new Scanner(System.in);
            //String userName = sc.nextLine();
            String userName = my_username;

            // String userName="Alice";
            // Querying a table
            try {
                String querySQL = "SELECT pokename from capturablepokemons where username= '" + userName +"'";
               // String querySQL = "SELECT max(code) from groups;";
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

    static void createParty(int sqlCode,String sqlState) throws SQLException {
        //connect
      //  String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
       // String usernamestring="cs421g36";
       // String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;

        // String userName="Alice";
        // Querying a table
        try {
            String querySQL = "SELECT pokename from capturablepokemons where username= '" + my_username +"'";
            // String querySQL = "SELECT max(code) from groups;";
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


        System.out.println("Do you want to create a new raid party? yes/no");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if(choice=="no"){
            return;
        }
        int members = 1;



    }

    static void capturePokemon(int sqlCode,String sqlState) throws SQLException {
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;



        statement.close ( ) ;
        con.close ( ) ;
    }



}


