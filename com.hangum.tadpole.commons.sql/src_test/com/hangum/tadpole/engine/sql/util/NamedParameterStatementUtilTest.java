package com.hangum.tadpole.engine.sql.util;

import java.util.Map;
import java.util.Set;

import com.hangum.tadpole.engine.sql.paremeter.lang.OracleStyleSQLNamedParameterUtil;

public class NamedParameterStatementUtilTest {
	public static void main(String[] args) {
		
		try {
			String query = "select * from my_table where name = :name or address = ?address";
//			String query = "select * from my_table where name = ? or address = ?";
			
			OracleStyleSQLNamedParameterUtil p = new OracleStyleSQLNamedParameterUtil();
			p.parse(query);
			
//			Map<String, int[]> mapIndex = p.getIndexMap();
//			if(mapIndex.size() == 0) return;
//			
//			Set<String> keys = mapIndex.keySet();
//			for (String string : keys) {
//				System.out.println("key is " + string);
//			}
//			System.out.println(mapIndex.size());
			Map<Integer, String> map = p.getMapIndexToName();
			for(int i=0; i<map.size(); i++){
				System.out.println(p.getIndex(i+1));
			}
			
			
			System.out.println("==========================");
//			int[] intIndexPos = p.getIndexes("name");
//			System.out.println(intIndexPos[0]);
//			
//			int[] intAddressPos = p.getIndexes("address");
//			System.out.println(intAddressPos[0]);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
