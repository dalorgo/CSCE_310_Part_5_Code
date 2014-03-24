import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;



public class DB_Interface {
	
	public static void main(String args[]) {
		connect_to_db();
	}

	public DB_Interface() {
	}
	
	public static void connect_to_db() {
		String DBLocation = "database-new.cse.tamu.edu"; //The host 
		String DBname = "jdonais-pokemonproject"; //Generally your CS username or username-text like explained above
		String DBUser = "jdonais"; //CS username
		String DBPass = "11Jordan18SQL"; //password setup via CSNet for the MySQL database

		Connection conn = null;
		try
		{
		 String connectionString = "jdbc:mysql://"+DBLocation+"/"+DBname;
		 Class.forName ("com.mysql.jdbc.Driver").newInstance();
		 conn = DriverManager.getConnection(connectionString, DBUser, DBPass);
		 System.out.println ("Database connection established");
		}
		catch (Exception e)
		{
		 System.out.println("Connection Issue: " + e.getMessage());
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection was a go.");
	}
	
}
