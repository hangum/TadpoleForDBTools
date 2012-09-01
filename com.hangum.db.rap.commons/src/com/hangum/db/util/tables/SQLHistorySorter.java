/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
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
