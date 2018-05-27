package com.hangum.tadpole.engine.connect;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class AbstractDriverInfo {

	protected static void printMetaData(DatabaseMetaData dmd) throws Exception {
		
		ResultSet rsCatalogs = dmd.getCatalogs();
		System.out.println("catalog list is ");
		while(rsCatalogs.next()) {
			System.out.println("==> schema : " + rsCatalogs.getString(0));
		}
		
		System.out.println("user name is " + dmd.getUserName());
		System.out.println(dmd.getDatabaseProductName() + " " + dmd.getDatabaseProductVersion() );
		System.out.println(dmd.getDriverName() + " " + dmd.getDriverVersion());
		System.out.println("");
		
		System.out.println("version : " + dmd.getDatabaseMajorVersion() + ":" + dmd.getDatabaseMinorVersion());
		
		System.out.println("version : " + dmd.getDatabaseProductVersion());

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
}
