import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;



public class DB_Interface {
	
	private PreparedStatement ps = null;
	private Connection conn = null;
	
	public DB_Interface() {
	}
	
	public void connect_to_db() {
		String DBLocation = "localhost:3306"; //The host 
		String DBname = "pokemondb";
		String DBUser = "root";
		String DBPass = "mysql"; 

		//Connection conn = null;
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
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Connection was a go.");
	}
	
	public void close_connection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean updateDescription() {
		Scanner reader = new Scanner(System.in);
		String pokemonName = "", description = "";
		System.out.print("Please enter the name of the Pokemon to update the description of: ");
		pokemonName = reader.next();
		//if (reader.hasNext()) {
//			System.out.println("Extra stuff found.");
			reader.nextLine();
//			System.out.println("Extra stuff removed.");
//		}
		// Check to see if the name was valid
		try {
			PreparedStatement nameChecker = conn.prepareStatement("SELECT * FROM Pokemon WHERE Name = ?;");
			nameChecker.setString(1, pokemonName);
			ResultSet rVal = nameChecker.executeQuery();
			if (!rVal.first()) {
				System.out.println("ERROR: Invalid Pokemon name entered.");
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("Please enter the new description for this Pokemon: ");
		description = reader.nextLine();
		try {
			PreparedStatement descAdd = conn.prepareStatement("UPDATE Pokemon SET Description = ? WHERE Name = ?;");
			descAdd.setString(1, description);
			descAdd.setString(2, pokemonName);
			descAdd.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public ResultSet executeCustomQuery(String query) {
		ResultSet rSet = null;
		try {
			rSet = conn.prepareStatement(query).executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rSet;
	}
	
	public void printResultSet(ResultSet rsIn) {
		int columnCount = -1;
		try {
			// First get the column count
			columnCount = rsIn.getMetaData().getColumnCount();
			for (int i = 1; i < columnCount + 1; i++) {
				System.out.print("[");
				System.out.print(rsIn.getMetaData().getColumnName(i));
				System.out.print("]");
			}
			System.out.println("");
			while (rsIn.next()) {
				for (int i = 1; i < columnCount + 1; i++) {
					System.out.print("[");
					System.out.print(rsIn.getString(i));
					System.out.print("]");
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
