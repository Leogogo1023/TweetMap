import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Rds {
	static String url = "*";
	static String usrDB = "*";
	static String password = "*";
	static String dbName = "tweetData";
	static Vector<Vector<String>> result = new Vector<Vector<String>>();
	static Statement stmt = Rds.connectDB(url,usrDB,password,dbName);;

	@SuppressWarnings("unchecked")
	public static Vector<Vector<String>> getResult(){
		Vector<Vector<String>> rslt = new Vector<Vector<String>>();
		Vector<String> entry = new Vector<String>();
		if(stmt!=null){
			String sql = "SELECT * FROM Tweets;";
			try {
				ResultSet rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();

				int rowCount = 0;
				rs.last();                		//Moves the cursor to the last row in this ResultSet object.
				rowCount = rs.getRow(); 
				rs.first();
				if(rs.next()){
					for(int i = 1;i<=rowCount;i++){
						rs.absolute(i);	        	// Moves the cursor to the given row number in this ResultSet object.

						for(int j = 1; j<=columnsNumber; j++){							
							entry.add(rs.getString(j));
						}
						rslt.add( (Vector<String>) entry.clone());
						entry.clear();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}    
		//this.printRslt(rslt);
		return rslt;
	}
	@SuppressWarnings("unchecked")
	public static Vector<Vector<String>> getResult(String keyWord){
		Vector<Vector<String>> rslt = new Vector<Vector<String>>();
		Vector<String> entry = new Vector<String>();
		Vector<Vector<String>> allRslt = Rds.getResult();
		for(int i = 0;i<allRslt.size();i++){
			String twtCtnt = allRslt.get(i).get(2);
			if(twtCtnt.toLowerCase().contains(keyWord.toLowerCase())){
				for(int j = 0;j < allRslt.get(i).size();j++){
					entry.add(allRslt.get(i).get(j));
				}    			
				rslt.add((Vector<String>) entry.clone());
				entry.clear();
			}
		}
		return rslt;
	}

	@SuppressWarnings("unchecked")
	public static Vector<Vector<String>> getDateData(String date){
		Vector<Vector<String>> rslt = new Vector<Vector<String>>();
		Vector<String> entry = new Vector<String>();
		if(stmt!=null){
			String sql =null;
			if (date.equals("hour"))
				sql =  "select * from Tweets where Time between now() - INTERVAL 360 MINUTE and now();";
			if (date.equals("day"))
				sql =  "select * from Tweets where Time between now() - INTERVAL 1 DAY and now();"; 
			if (date.equals("more"))
				sql =  "select * from Tweets;"; 
			try {
				ResultSet rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();

				int rowCount = 0;
				rs.last();                		//Moves the cursor to the last row in this ResultSet object.
				rowCount = rs.getRow(); 
				rs.first();
				if(rs.next()){
					for(int i = 1;i<=rowCount;i++){
						rs.absolute(i);	        	// Moves the cursor to the given row number in this ResultSet object.

						for(int j = 1; j<=columnsNumber; j++){							
							entry.add(rs.getString(j));
						}
						rslt.add( (Vector<String>) entry.clone());
						entry.clear();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}    
		return rslt;
	}
	public static Statement connectDB(String url,String usr,String password,String dbName){
		String driver = "com.mysql.jdbc.Driver";
		Statement statement = null;
		Connection conn = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}        	
		try {
			conn = DriverManager.getConnection(url, usr, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		try {
			if(!conn.isClosed()){			
				System.out.println("Connected!");
				statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statement;
	}

}
