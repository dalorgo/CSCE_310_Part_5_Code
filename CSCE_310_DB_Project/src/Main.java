import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Main {

	public static void main(String args[]) {
		interfaceLoop();
	}
	
	public static void helpInfo() {
		System.out.println("The following commands are available to use:");
		System.out.println("0 : Find all Pokemon who are type weak to a chosen type.");
		System.out.println("1 : Update the description of a Pokemon.");
		System.out.println("2 : List all Pokemon in database.");
	}
	
	public static void sorry() {
		System.out.println("Sorry, feature not implemented yet.");
	}
	
	public static void interfaceLoop() {
		Scanner reader = new Scanner(System.in);
		String commandNum = "";
		DB_Interface pdb = new DB_Interface();
		pdb.connect_to_db();
		try {
			while (true) {
				System.out.print("Please enter a command number, or \"help\" for assistance: ");
				commandNum = reader.next();
				switch (commandNum) {
				case ("quit"):
					System.out.println("System now quitting...");
					return;
				case ("0"):
					sorry();
					// do something
					break;
				case ("1"):
					pdb.updateDescription();
					break;
				case ("2"):
					pdb.printResultSet(pdb.executeCustomQuery("SELECT Name, NationalId, Description FROM Pokemon ORDER BY NationalId"));
					break;
				case ("help"):
					helpInfo();
					break;
				default:
					System.out.println("Command number \"" + commandNum + "\" is not recognized. Type \"help\" for assistance.");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pdb.close_connection();
		}
		
	}
}
