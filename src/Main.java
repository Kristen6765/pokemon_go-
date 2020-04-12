import org.postgresql.Driver;

import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

class connectExample {
    private static final String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
    private static final String usernamestring="cs421g36";
    private static final String passwordstring="Dalao2020.Dalao2020@";
    private static Scanner playing = new Scanner(System.in);

    private static String my_username = "";
    private static int player_code = 0;
    private static int playersingroup = 1;
    private static ArrayList<String> groupmembers = new ArrayList<String>();

    public static void main(String args[]) throws SQLException {
        System.out.println("Welcome to pokemongo!");
        System.out.println("Do you already have an account? Type 'yes' or 'no'");
        String newPlayer = playing.nextLine();

      //  System.out.println(newPlayer);
        if(newPlayer.equals("no")) {
            int sqlCode = 0;      // Variable to hold SQLCODE
            String sqlState = "00000";  // Variable to hold SQLSTATE
            System.out.println("call createA");
            createAccount(sqlCode, sqlState);
        }

        System.out.println("Please enter your username to log in.");
        String ans = playing.nextLine();
        Boolean doesnotexist = checkIfUserExists(ans);

        while (doesnotexist == false) {
            System.out.println("Username does not exist. Please enter your username again");
            ans = playing.nextLine();
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
        // my_username = userChoice.nextLine();

        System.out.println("-------------------------------------------------------------------------");

        System.out.println("Now you can: " +
                "1. quit" + "\t" +
                "2. add friend " + "\t" +
                "3. capture pokemon " + "\t" +
                "4. check your bag " + "\t" +
                "5. add friends to your group"
        );

        System.out.println("Go ahead and select one by type the number corresponding to it " +
                "to start your adventure!" +
                "");

        //prompt from user
        //Scanner userChoice = new Scanner(System.in);
        int choice = playing.nextInt();
        //magic door to the method
        playing.nextLine();
        switch(choice) {
            case 1:
                // quit
                System.out.println("Do you want to quit? Type 'yes' or 'no'");
                String ans = playing.nextLine();
                if (ans.equals("yes"))  {
                    playing.close();
                    return false;
                }
                break;
            case 2:
                //add friend
                addFriend(sqlCode,sqlState);
                break;
            case 3:
                // capture pokemon;
                capturePokemon(sqlCode, sqlState);
                break;
            case 4:
                // check bag
                checkBag(sqlCode,sqlState);
                break;
            case 5:
                createParty(sqlCode, sqlState);
                break;
            default:
                // code block
                return true;
        }

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
                player_code = rs.getInt("code");
                System.out.print("Code: " + player_code);
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

// hello
        try {
            String querySQL = "SELECT username FROM Players WHERE code =" + player_code;
            //System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //create an unique code not in db yet
            System.out.print("You're currently in a group with the following people: ");
            while ( rs.next ( ) ) {
                String friend = rs.getString("username");
                if (friend.equals(test_user) == false) {
                    playersingroup += 1;
                    groupmembers.add(friend);
                }
            }
            System.out.print(groupmembers.toString());
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
           // System.out.println (querySQL) ;
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            //create an unique code not in db yet
            while ( rs.next ( ) ) {
                maxCode = rs.getInt (1) ;
               // System.out.println ("maxCode:  " + maxCode);
            }
          //  System.out.println ("DONE");
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
          //  System.out.println (insertSQL) ;
            statement.executeUpdate (insertSQL) ;
          //  System.out.println ("DONE");
        } catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }


        //get name from user and add name and code to table Players
        System.out.println("Hi, I'll help you to create a new account, please create a new name.");
        String name = playing.nextLine();
        //magic door to the method
        try {

            String insertSQL = "INSERT INTO  players VALUES (\'"+name+"\',0,\'"+newCode+"\')" ;
         //   System.out.println ( insertSQL ) ;
            statement.executeUpdate ( insertSQL ) ;
            System.out.println ( "DONE"+ name+"you have a new account with groupID "+maxCode) ;
        }catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }

    }


    /*
    add friend
     */
    static void  addFriend(int sqlCode,String sqlState) throws SQLException {

        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;

        System.out.println(" ");
        System.out.println("Please type in your friend's name");
        Scanner fName = new Scanner(System.in);
        String friendName = fName.nextLine();


        String userName = my_username;

        // Inserting Data into the table (befriends)
        try {
            String insertSQL = "INSERT INTO  befriends(friends_username, my_username) VALUES (\'"+friendName+"\',\'"+ userName +"\') " ;
            //System.out.println ( insertSQL ) ;
            statement.executeUpdate ( insertSQL ) ;
            // System.out.println ( "DONE" ) ;

        }catch (SQLException e)
        {
            //System.out.println("122");
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }



        //print out all friends of user
        System.out.println("Now you are friends with: "+friendName);

        statement.close ( ) ;
        con.close ( ) ;
    }

    static void  checkBag(int sqlCode,String sqlState) throws SQLException {
        System.out.println("you you want to check bag? yes/no");
        String choice = playing.nextLine();
        if(choice=="no"){
            return;
        }
        //connect
        //  String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        // String usernamestring="cs421g36";
        // String passwordstring="Dalao2020.Dalao2020@";
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;

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
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;

        // playersingroup holds the current number of players in your group
        // max number of players in a group is 20
        System.out.println("Enter the next username of the friend you want to add to your group, " +
                "or enter 0 if you're done");
        while (playing.hasNext()) {
            if (playing.hasNextInt()) {
                break;
            }
            if (playersingroup >= 20) {
                System.out.println("You already have 20 people in your group. Can't add anymore.");
                break;
            }
            String friendname = playing.nextLine();

            if (groupmembers.contains(friendname)) {
                System.out.println(friendname + " is already in your group!");
                continue;
            }

            try {
                //
                String querySQL = "SELECT COUNT(*) FROM befriends where friends_username='" + friendname + "'" + "AND my_username='" + my_username + "'";
                // System.out.println(querySQL);
                ResultSet rs = statement.executeQuery(querySQL);

                //print all pokenames the user has
                int count = 0;
                while (rs.next()) {
                    count = rs.getInt("count");
                }
                if (count == 0) {
                    // friend username not valid or not friends, can't add to group
                    System.out.println("Unable to add " + friendname + " to group because you're not friends yet.");
                    continue;
                } else {
                    System.out.println("Successfully added " + friendname + " to your group.");
                }

            } catch (SQLException e) {

                sqlCode = e.getErrorCode(); // Get SQLCODE
                sqlState = e.getSQLState(); // Get SQLSTATE
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            }

            // now update your friend's group code to yours
            try {
                String updateSQL = "UPDATE Players SET code=" + player_code + " WHERE username='" + friendname + "'" ;
                statement.executeUpdate ( updateSQL ) ;
                playersingroup += 1;
                groupmembers.add(friendname);
            }catch (SQLException e)
            {
                sqlCode = e.getErrorCode(); // Get SQLCODE
                sqlState = e.getSQLState(); // Get SQLSTATE
                statement.close();
                con.close();
            }
            System.out.println("Enter the next username of the friend you want to add to your group, " +
                    "or enter 0 if you're done");
        }

        System.out.println("Now you are in a group with the following people: ");
        System.out.println(groupmembers.toString());

        statement.close ( ) ;
        con.close ( ) ;

        playing.next();
    }


    static void capturePokemon(int sqlCode,String sqlState) throws SQLException {
        Connection con = DriverManager.getConnection (url,usernamestring, passwordstring) ;
        Statement statement = con.createStatement ( ) ;
        String pokename = "";
        String type = "";
        Random random = new Random();

        try {
            String querySQL = "SELECT * FROM pokemons OFFSET floor(random() * (SELECT COUNT(*) FROM pokemons)) LIMIT 1";
            ResultSet rs = statement.executeQuery ( querySQL ) ;
            while ( rs.next ( ) ) {
                pokename = rs.getString ("pokename") ;
                type = rs.getString("typename");
            }
        } catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            //  System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
        }

        System.out.println("A wild " + pokename + " has appeared! " + "It's a " + type + " pokemon.");
        int cp = random.nextInt(3000);
        System.out.println("Its cp is: " + cp);

        System.out.println("Type 'throw' to throw your pokeball to catch it!");
        String temp = playing.nextLine();

        while (temp.equals("throw") == false) {
            System.out.println("You didn't throw your pokeball properly!");
            temp = playing.nextLine();
        }
        // pokemon with higher cp are harder to catch
        System.out.println("(╯°□°)╯︵◓");
        int pokeball = random.nextInt(5000);

        if (pokeball < cp) {
            System.out.println("Catch unsuccessful. Better luck next time!");
            return;
        }
        System.out.println("Catch successful!");

        try {
            String insertSQL = "INSERT INTO capturablepokemons (cp,username,capturedtime,pokename) VALUES (" + cp + ",'" +
                    my_username +
                    "',localtimestamp(0),'" +
                    pokename +
                    "');" ;
            //System.out.println ( insertSQL ) ;
            statement.executeUpdate ( insertSQL ) ;
            //System.out.println ( "DONE" ) ;
            System.out.println("Your new pokemon:");
            System.out.println(my_username + ": " + "\t" + pokename + " cp: " + type);
        }catch (SQLException e)
        {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            //System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            statement.close();
            con.close();
        }

        statement.close ( ) ;
        con.close ( ) ;
    }



}

