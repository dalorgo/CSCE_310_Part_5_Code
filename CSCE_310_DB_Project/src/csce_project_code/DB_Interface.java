package csce_project_code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;


public class DB_Interface {
	
	private Connection conn = null;
	
	public DB_Interface() {
	}
	
	// Connects to the database, or reports errors
	public void connect_to_db() {
		String DBLocation = "localhost:3306"; //The host 
		String DBname = "pokemondb";
		String DBUser = "root";
		String DBPass = "mysql"; 

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

		System.out.println("Connection was a go.");
	}
	
	// Closes the current connection, if there is one
	public void close_connection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Updates the description field of a chosen Pokemon
	// If the Pokemon does not exist, function will return
	public Boolean updateDescription(Scanner reader) {
		String pokemonName = "", description = "";
		System.out.print("Please enter the name of the Pokemon to update the description of: ");
		pokemonName = reader.next();
		reader.nextLine();
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
			e.printStackTrace();
		}
		return true;
	}
	
	// Pre-setup function for easy query writing.
	public ResultSet executeCustomQuery(String query) {
		ResultSet rSet = null;
		try {
			rSet = conn.prepareStatement(query).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rSet;
	}
	
	/*
	 *  Prints the ResultSet from a query in table form.
	 */
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
			e.printStackTrace();
		}
	}
	
	/*
	 *  Finds all Pokemon who have a Type advantage against a specified
	 *  Type. If the type does not exist, function will return.
	 */
	public void findTypeAdvantage(Scanner reader) {
		String typeName = "";
		System.out.print("Please enter the type that you wish to find Pokemon strong against: ");
		typeName = reader.next();
		// Check if type does exist
		try {
			PreparedStatement typeChecker = conn.prepareStatement("SELECT * FROM Types WHERE Name = ?;");
			typeChecker.setString(1, typeName);
			ResultSet rVal = typeChecker.executeQuery();
			if (!rVal.first()) {
				System.out.println("ERROR: Invalid Type entered.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String query = "";
		query += "SELECT DISTINCT";
		query += " p.Name";
		query += " FROM";
		query += " Weaknesses w";
		query += " JOIN";
		query += " Types weak_type";
		query += " ON";
		query += " weak_type.GUID = w.PokemonUIDWeak";
		query += " JOIN";
		query += " Types strong_type";
		query += " ON";
		query += " strong_type.GUID = w.PokemonUIDStrong";
		query += " JOIN";
		query += " Type_ pokemon_to_type";
		query += " ON";
		query += " pokemon_to_type.TypeUID = strong_type.GUID";
		query += " JOIN";
		query += " Pokemon p";
		query += " ON";
		query += " p.GUID = pokemon_to_type.PokemonUID";
		query += " WHERE";
		query += " weak_type.Name = ?"; 
		query += " ORDER BY";
		query += " p.NationalId;";
		
		// Find the pokemon
		try {
			PreparedStatement typeChecker = conn.prepareStatement(query);
			typeChecker.setString(1, typeName);
			ResultSet rVal = typeChecker.executeQuery();
			printResultSet(rVal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Just for fun, print a random quote.
	public void sayRandom() {
		printResultSet(executeCustomQuery("SELECT * FROM RandomQuotes ORDER BY RAND() LIMIT 1;"));
	}
	
	// Adds a Pokemon to the Pokemon table, but nothing else...
	public void addPokemon(Scanner reader) {
		String name, desc;
		@SuppressWarnings("unused")
		int id, hp, atk, def, satk, spd, ht, sdef;
		float wt;
//		Scanner reader = new Scanner(System.in);
		System.out.print("Please enter the name of the new Pokemon: ");
		name = reader.next();
		// Check if the name already exists...
		try {
			PreparedStatement typeChecker;
			typeChecker = conn.prepareStatement("SELECT * FROM Pokemon WHERE Name = ?;");
			typeChecker.setString(1, name);
			ResultSet rVal = typeChecker.executeQuery();
			if (rVal.first()) {
				System.out.println("ERROR: Pokemon already exists.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			System.out.print("Please enter the National ID for " + name + ": ");
			id = reader.nextInt();
			System.out.print("Please enter the HP stat: ");
			hp = reader.nextInt();
			System.out.print("Please enter the Attack stat: ");
			atk = reader.nextInt();
			System.out.print("Please enter the Defense stat: ");
			def = reader.nextInt();
			System.out.print("Please enter the Special Attack stat: ");
			satk = reader.nextInt();
			System.out.print("Please enter the Special Defense stat: ");
			sdef = reader.nextInt();
			System.out.print("Please enter the Speed stat: ");
			spd = reader.nextInt();
			System.out.print("Please enter its Height (in inches): ");
			ht = reader.nextInt();
			System.out.print("Please enter its Weight (in pounds): ");
			wt = reader.nextFloat();
			reader.nextLine();
			System.out.print("Please enter a description: ");
			desc = reader.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("ERROR: Invalid input detected for the requested field.");
			return;
		}

		PreparedStatement pokemonInsert;
		try {
			pokemonInsert = conn.prepareStatement("INSERT INTO Pokemon VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
			pokemonInsert.setString(1, UUID.randomUUID().toString());
			pokemonInsert.setInt(2, id);
			pokemonInsert.setString(3, name);
			pokemonInsert.setInt(4, hp);
			pokemonInsert.setInt(5, atk);
			pokemonInsert.setInt(6, def);
			pokemonInsert.setInt(7, spd);
			pokemonInsert.setInt(8, ht);
			pokemonInsert.setFloat(9, wt);
			pokemonInsert.setInt(10, satk);
			pokemonInsert.setInt(11, spd);
			pokemonInsert.setString(12, desc);
			pokemonInsert.executeUpdate();
			printResultSet(executeCustomQuery("SELECT * FROM Pokemon WHERE Name = \'" + name + "\';"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
