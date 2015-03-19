/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.tables;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;

abstract public class DefaultViewerSorter extends BasicViewerSorter {

	private static final Logger logger = Logger.getLogger(DefaultViewerSorter.class);
			
	public enum COLUMN_TYPE { Date, String, Long, Double, IP };
	
	protected int getPrefix(Viewer viewer) {
		int prefix = 1;
		
		int sortDir = ((TableViewer)viewer).getTable().getSortDirection();
		if (sortDir == SWT.DOWN) {
			prefix = -1;
		}
		
		return prefix;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TableViewer tableViewer = (TableViewer)viewer;
		ITableLabelProvider tlprov = (ITableLabelProvider)tableViewer.getLabelProvider();
		
		String name1 = tlprov.getColumnText(e1, propertyIndex);
		String name2 = tlprov.getColumnText(e2, propertyIndex);
		
		int prefix = getPrefix(viewer);
		
		if ( isEmpty(name1) ) {
			if ( isEmpty(name2) ) {
				return 0;
			} else {
				return prefix;
			}
		} else {
			if ( isEmpty(name2) ) {
				return prefix*-1;
			} else {
				COLUMN_TYPE type = getColumnType(propertyIndex);
				switch(type) {
				case Date : 
					try {
						Date date1 = dateToStr(name1);
						Date date2 = dateToStr(name2);
						
						Long long1 = date1.getTime();
						Long long2 = date2.getTime();
						
						return prefix * long1.compareTo(long2);
					} catch(Exception e) {
						logger.error("Date column sort", e);
					}
					
				case String :
					return prefix * name1.compareTo(name2);
				case Long :
					try {
						Long long1 = Long.parseLong(name1);
						Long long2 = Long.parseLong(name2);
						
						return prefix * long1.compareTo(long2);
					} catch(NumberFormatException nfe) {
						logger.error("", nfe);
					}
					
					break;
				case Double :
					try {
						Double double1 = Double.parseDouble(name1);
						Double double2 = Double.parseDouble(name2);
						
						return prefix * double1.compareTo(double2);
					} catch(NumberFormatException nfe) {
						logger.error("", nfe);
					}
					
					break;
				case IP :
					if ( "".equals(name1) || "".equals(name2) ) {
						System.out.println();
					}
					StringTokenizer st1 = new StringTokenizer(name1, ".");
					StringTokenizer st2 = new StringTokenizer(name2, ".");
					
					String s1,s2;
					while( st1.hasMoreTokens() && st2.hasMoreTokens() ) {
						int a = -1;
						int b = -1;
						
						s1 = st1.nextToken();
						s2 = st2.nextToken();
						try { a = Integer.parseInt(s1); } catch(Exception e){};
						try { b = Integer.parseInt(s2); } catch(Exception e){};
						
						if ( a != -1 && b != -1 ) { //integer both
							if ( a == b )	continue;
							return prefix*(a - b);
						} else if ( a == -1 && b == -1 ) { //not-integer both
							int comp = s1.compareTo(s2);
							if ( comp == 0 )	continue;
							
							if ( "*".equals(s1) ) {
								return prefix*1;
							} else if ( "*".equals(s2) ) {
								return prefix*(-1);
							} else {
								return prefix*comp;
							}
						} else {
							return prefix*(-1)*a;
						}
					}
					
					return 0;
				}
			}
		}
		

		return prefix;
	}

	private boolean isEmpty(String value) {
		return value == null || "".equals(value);
	}
	
	abstract public COLUMN_TYPE getColumnType(int propertyIndex);
	
	/**
	 * date converter
	 * 
	 * @param date
	 * @return
	 */
	private Date dateToStr(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(str);
	}
	
}
