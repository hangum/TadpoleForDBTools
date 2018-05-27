package com.hangum.tadpole.engine.connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TiberoMain {

	public static void main(String[] args) {
		System.out.println("========= start object ==================");
		try {
			Class.forName("com.tmax.tibero.jdbc.TbDriver");
	        Connection conn = DriverManager.getConnection("jdbc:tibero:thin:@192.168.29.173:8629:tibero",
	                                        "sys", "tadpole");
	        
	        Statement         stmt  = conn.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM TEST_TIBERO");
	        while (rs.next()) {
	        	System.out.println("==> " + rs.getString(1));
	        }
	        
	        
		} catch(Exception e) {
			e.printStackTrace();
		}


	}

}
