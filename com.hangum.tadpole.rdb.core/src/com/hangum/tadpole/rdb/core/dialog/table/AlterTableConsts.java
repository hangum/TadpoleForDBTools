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
package com.hangum.tadpole.rdb.core.dialog.table;

/**
 * alter table consts
 * 
 * @author hangum
 *
 */
public class AlterTableConsts {
//	public static final String SEQ_NO = "Seq"; 
	public static final String COLUMN_NAME = "Column Name"; 
//	public static final String COLUMN_ID = "ID";
	public static final String PRIMARY_KEY = "Pk";
	public static final String DATA_TYPE = "Type";
	public static final String DATA_SIZE = "Size";
//	public static final String DATA_PRECISION = "Precision";
//	public static final String DATA_SCALE = "Scale";
	public static final String DEFAULT_VALUE = "Default";
	public static final String NULLABLE = "Not Null";
	public static final String COMMENT = "Comment";

//	public static final int SEQ_NO_IDX 		= 0;
	public static final int COLUMN_NAME_IDX = 0;
//	public static final int COLUMN_ID_IDX = 2;
	public static final int PRIMARY_KEY_IDX = 1;
	public static final int DATA_TYPE_IDX = 2;
	public static final int DATA_SIZE_IDX = 3;
//	public static final int DATA_PRECISION_IDX = 4;
//	public static final int DATA_SCALE_IDX = 5;
	public static final int DEFAULT_VALUE_IDX = 4;
	public static final int NULLABLE_IDX = 5;
	public static final int COMMENT_IDX = 6;

//	public static final int SEQ_NO_SIZE = 30;
	public static final int COLUMN_NAME_SIZE = 150;
//	public static final int COLUMN_ID_SIZE = 35;
	public static final int PRIMARY_KEY_SIZE = 50;
	public static final int DATA_TYPE_SIZE = 150;
	public static final int DATA_SIZE_SIZE = 35;
//	public static final int DATA_PRECISION_SIZE = 50;
//	public static final int DATA_SCALE_SIZE = 35;
	public static final int DEFAULT_VALUE_SIZE = 70;
	public static final int NULLABLE_SIZE = 50;
	public static final int COMMENT_SIZE = 100;

	public static final String[] names = {/*SEQ_NO,*/COLUMN_NAME,/*COLUMN_ID,*/PRIMARY_KEY,DATA_TYPE,DATA_SIZE,/*DATA_PRECISION,DATA_SCALE,*/DEFAULT_VALUE,NULLABLE, COMMENT};
	public static final int[] sizes = {/*SEQ_NO_SIZE,*/COLUMN_NAME_SIZE,/*COLUMN_ID_SIZE,*/PRIMARY_KEY_SIZE,DATA_TYPE_SIZE,DATA_SIZE_SIZE,/*DATA_PRECISION_SIZE,DATA_SCALE_SIZE,*/DEFAULT_VALUE_SIZE,NULLABLE_SIZE, COMMENT_SIZE};
}
