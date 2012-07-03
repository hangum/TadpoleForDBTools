package com.hangum.db.util.tables;

import com.hangum.db.util.tables.DefaultViewerSorter;

/**
 * sql history, message sorter 
 * 
 * @author hangum
 *
 */
public class SQLHistorySorter extends DefaultViewerSorter {

	@Override
	public COLUMN_TYPE getColumnType(int propertyIndex) {
		switch(propertyIndex) {
		case 0 :
			return COLUMN_TYPE.Date;
		default:				
			return COLUMN_TYPE.String;
		}
	}
}
