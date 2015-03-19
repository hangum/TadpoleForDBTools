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

import com.hangum.tadpole.engine.sql.util.tables.DefaultViewerSorter;

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
		case 0 : return COLUMN_TYPE.Date;
		case 1 : return COLUMN_TYPE.String;
		case 2 : 
		case 3 : return COLUMN_TYPE.Double;
		default:				
			return COLUMN_TYPE.String;
		}
	}
}
