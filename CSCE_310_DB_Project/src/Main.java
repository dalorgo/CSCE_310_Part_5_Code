import java.sql.ResultSet;
import java.sql.SQLException;


public class Main {

	public static void main(String args[]) {
		DB_Interface pdb = new DB_Interface();
		pdb.connect_to_db();
		
		ResultSet pokemons = null;
		pokemons = pdb.executeCustomQuery("SELECT Name, NationalId, Description FROM Pokemon ORDER BY NationalId");
		
		
		try {
			while (pokemons.next()) {
				System.out.print(pokemons.getString("Name"));
				System.out.print(" - ");
				System.out.print(pokemons.getString("NationalId"));
				System.out.print(" - ");
				System.out.println(pokemons.getString("Description"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pdb.close_connection();
	}
}
