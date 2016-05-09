/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Original: (MIT License)
 * 		https://code.google.com/p/framework-pegasus/source/browse/Pegasus/trunk/src/ar/pegasus/framework/util/jdbc/NamedParameterStatement.java?r=8
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.paremeter.lang;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * This class wraps around a {@link PreparedStatement} and allows the 
 * programmer to set parameters by name instead
 * of by index.  This eliminates any confusion as to which parameter index 
 * represents what.  This also means that
 * rearranging the SQL statement or adding a parameter doesn't involve 
 * renumbering your indices.
 * Code such as this:
 *
 * <PRE>
 * Connection con=getConnection();
 * String query="select * from my_table where name=? or address=?";
 * PreparedStatement p=con.prepareStatement(query);
 * p.setString(1, "bob");
 * p.setString(2, "123 terrace ct");
 * ResultSet rs=p.executeQuery();
 * </PRE>
 * 
 * can be replaced with:
 *
 * <PRE>
 * Connection con=getConnection();
 * String query="select * from my_table where name=:name or address=:address";
 * NamedParameterStatement p=new NamedParameterStatement(con, query);
 * p.setString("name", "bob");
 * p.setString("address", "123 terrace ct");
 * ResultSet rs=p.executeQuery();
 * </PRE>
 *
*/
public class OracleStyleSQLNamedParameterUtil {
	
    /** Maps parameter names to arrays of ints which are the parameter indices. */
    private Map<String, int[]> mapNameToIndex = new HashMap<String, int[]>();
    private Map<Integer, String> mapIndexToName = new HashMap<Integer, String>();

    public OracleStyleSQLNamedParameterUtil() {}
    
    /**
     * Parses a query with named parameters.  The parameter-index mappings are 
     * put into the map, and the
     * parsed query is returned.  DO NOT CALL FROM CLIENT CODE.  This 
     * method is non-private so JUnit code can
     * test it.
     * @param query    query to parse
     * @return the parsed query
     */
    public String parse(String query) {
    	
        // I was originally using regular expressions, but they didn't work well 
    	// for ignoring parameter-like strings inside quotes.
    	Map<String, List<Integer>> paramMapAux = new HashMap<String, List<Integer>>();
        int length=query.length();
        StringBuffer parsedQuery=new StringBuffer(length);
        boolean inSingleQuote=false;
        boolean inDoubleQuote=false;
        int index=1;

        for(int i=0;i<length;i++) {
            char c=query.charAt(i);
            if(inSingleQuote) {
                if(c=='\'') {
                    inSingleQuote=false;
                }
            } else if(inDoubleQuote) {
                if(c=='"') {
                    inDoubleQuote=false;
                }
            } else {
                if(c=='\'') {
                    inSingleQuote=true;
                } else if(c=='"') {
                    inDoubleQuote=true;
                } else if(c==':' && i+1<length &&
                        Character.isJavaIdentifierStart(query.charAt(i+1))) {
                	
                	if(query.charAt(i-1) != ':') {
	                    int j=i+2;
	                    while(j<length && Character.isJavaIdentifierPart(query.charAt(j))) {
	                        j++;
	                    }
	                    String name=query.substring(i+1,j);
	                    c='?'; // replace the parameter with a question mark
	                    i+=name.length(); // skip past the end if the parameter
	
	                    List<Integer> indexList= paramMapAux.get(name);
	                    if(indexList==null) {
	                        indexList=new LinkedList<Integer>();
	                        paramMapAux.put(name, indexList);
	                    }
	                    indexList.add(index);
	
	                    index++;
                	}
                }
            }
            parsedQuery.append(c);
        }

//        // replace the lists of Integer objects with arrays of ints
//        for(Map.Entry<String, List<Integer>> entry : paramMapAux.entrySet()) {
//            List<Integer> list=entry.getValue();
//            int[] indexes=new int[list.size()];
//            int i=0;
//            for(Integer x : list) {
//                indexes[i++]=x;
//            }
//            mapNameToIndex.put(entry.getKey(), indexes) ;
//        }
        
        for(Map.Entry<String, List<Integer>> entry : paramMapAux.entrySet()) {
            List<Integer> list=entry.getValue();
            
            for(Integer x : list) {
            	mapIndexToName.put(x, entry.getKey());
            }
        }

        return parsedQuery.toString();
    }

//    /**
//     * Returns the indexes for a parameter.
//     * @param name parameter name
//     * @return parameter indexes
//     * @throws IllegalArgumentException if the parameter does not exist
//     */
//    public int[] getIndexes(String name) {
//        int[] indexes=mapNameToIndex.get(name);
//        if(indexes==null) {
//            throw new IllegalArgumentException("Parameter not found: "+name);
//        }
//        return indexes;
//    }

    /**
     * Returns the index to param names
     * @param intIndex
     * @return
     */
    public String getIndex(Integer intIndex) {
    	return mapIndexToName.get(intIndex);
    }
    
//    /**
//     * Returns the indexMap
//     * @return
//     */
//    public Map<String, int[]> getIndexMap() {
//		return mapNameToIndex;
//	}
    
    public Map<Integer, String> getMapIndexToName() {
		return mapIndexToName;
	}

}