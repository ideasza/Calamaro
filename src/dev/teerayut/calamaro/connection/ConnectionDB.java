package dev.teerayut.calamaro.connection;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class ConnectionDB {

	private Statement stm;
	private Connection conn;
	private ResultSet resultSet;
	private PreparedStatement pstmt;
	
	public Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"/* + Config.DB_PATH + Config.DB_FILE*/);
			//conn.setAutoCommit(true);
			/*DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while(rs.next()) {
				System.out.println(rs.getString(3));
			}*/
	    } catch (SQLException | ClassNotFoundException ex){
		    System.err.println("SQLException connection: " + ex.getMessage());
		}
		 return conn;
	}
	
	public boolean checkConnection() {
		String con = "";
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" /*+ Config.DB_PATH*/);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			if (rs != null) {
				System.out.println(rs.getString(3));
				return true;
			} else {
				return false;
			}
	    } catch (SQLException | ClassNotFoundException ex){
		    System.err.println("SQLException check connection : " + ex.getMessage());
		}
		return false;
	}
	 
	 public ResultSet dbQuery(String statement) {
		try {
			conn = connect();
			stm = conn.createStatement();
			resultSet = stm.executeQuery(statement);
		} catch (SQLException e) {
			resultSet = null;
			dbCloseTransaction(stm, conn);
		}
		
		return resultSet;
	 }
	 
	 public String dbPrepareInsert(String statement ) {
		 String result = "";
		 try {
			conn = connect();
			conn.setAutoCommit(false);
			PreparedStatement preparedStatement = conn.prepareStatement(statement);
			if (preparedStatement.execute()) {
				result = "T;";
				conn.commit();
		    }else{
		        result = "F;";
		    }
			
		 } catch (SQLException e) {
			result = "F;"+e.toString();
			dbCloseTransaction(stm, conn);
			System.err.println("SQLException: " + e.getMessage());
		}
		 return result;
	 }
	 
	 public String dbInsertAndLastKey(String statement) {
		 String result = "";
		 try {
			 conn = connect();
			 pstmt = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			 resultSet = pstmt.executeQuery();
			 try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
		        if (generatedKeys.next()) {
		           result =  String.valueOf(generatedKeys.getInt(1))+";";
		        }
		     } catch (SQLException e) {
		        result = "0;"+e.toString();
			 }
			} catch (SQLException e) {
				result = "0;"+e.toString();
				resultSet = null;
				dbCloseTransaction(stm, conn);
			}
		 return result;
	 }
	 
	 public PreparedStatement dbInsert(String statement) {
		 try {
			 conn = connect();
			 pstmt = conn.prepareStatement(statement);
			 conn.setAutoCommit(true);
		 } catch (Exception e) {
			 pstmt = null;
			 dbCloseTransaction(stm, conn);
		 }
		 return pstmt;
	 }
	 
	 public PreparedStatement dbUpdate(String statement){
		 try {
			 conn = connect();
			 pstmt = conn.prepareStatement(statement);
		 } catch (Exception e) {
			 pstmt = null;
			 dbCloseTransaction(stm, conn);
		 }
		 return pstmt;
	 }
	 
	 private void dbCloseTransaction(Statement _stmt,Connection _conn){
	    try {
	        if (_stmt != null)
	            stm.close();
	    }  catch (SQLException e) {}
	        try {
	            if (_conn != null)
	            	conn.close();
	        } catch (SQLException e) {}  
	    }
	 
	 public void closeAllTransaction() {
		 try {
		        if (stm != null)
		            stm.close();
		        else if (pstmt != null)
		        	pstmt.close();
		    }  catch (SQLException e) {
		        try {
		            if (conn != null)
		            	conn.close();
		        } catch (SQLException ex) {}  
		    }
	 }
}
