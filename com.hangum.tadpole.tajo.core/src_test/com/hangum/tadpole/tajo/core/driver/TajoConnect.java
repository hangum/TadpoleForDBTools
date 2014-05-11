package com.hangum.tadpole.tajo.core.driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class TajoConnect {
	protected static void printMetaData(DatabaseMetaData dmd) throws Exception {
		
		System.out.println(dmd.getDatabaseProductName() + " " + dmd.getDatabaseProductVersion() );
		System.out.println(dmd.getDriverName() + " " + dmd.getDriverVersion());
		System.out.println("");

		System.out.println("\t getIdentifierQuoteString : " +  dmd.getIdentifierQuoteString() );
		System.out.println("\t storesLowerCaseIdentifiers : " +  dmd.storesLowerCaseIdentifiers() );
		System.out.println("\t storesLowerCaseQuotedIdentifiers : " +  dmd.storesLowerCaseQuotedIdentifiers() );
		System.out.println("\t storesMixedCaseIdentifiers : " +  dmd.storesMixedCaseIdentifiers() );
		System.out.println("\t storesMixedCaseQuotedIdentifiers : " +  dmd.storesMixedCaseQuotedIdentifiers() );
		System.out.println("\t storesUpperCaseIdentifiers : " +  dmd.storesUpperCaseIdentifiers() );
		System.out.println("\t storesUpperCaseQuotedIdentifiers : " +  dmd.storesUpperCaseQuotedIdentifiers() );
		
		System.out.println("\t SQL keywords : " + dmd.getSQLKeywords());
		System.out.println("-------------------------------------------------------");
		
	}

	public static void main(String args[]) {
		Connection c = null;
		try {
			Class.forName("org.apache.tajo.jdbc.TajoDriver");
			c = DriverManager.getConnection("jdbc:tajo://127.0.0.1:26002/default");
			
			printMetaData(c.getMetaData());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

}
