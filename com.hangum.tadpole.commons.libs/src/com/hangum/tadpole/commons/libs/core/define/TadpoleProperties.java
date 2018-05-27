package com.hangum.tadpole.commons.libs.core.define;

public class TadpoleProperties {
	
	public static final int SESSION_TIMEOUT_MIN = 5;       /* minutes */
	public static final int SESSION_TIMEOUT_MAX = 300;     /* minutes */
	public static final int SESSION_TIMEOUT_DEFAULT = 180; /* minutes */
	
	public static final int NUMBER_OF_ROWS_BY_SELECT_MIN = 100;
	public static final int NUMBER_OF_ROWS_BY_SELECT_MAX = 5000;
	public static final int NUMBER_OF_ROWS_BY_SELECT_DEF = 500; 
	
	public static final int ROWS_PER_PAGE_MIN = 100;
	public static final int ROWS_PER_PAGE_MAX = 1000;
	public static final int ROWS_PER_PAGE_DEF = 500;
	
	public static final int BATCH_SIZE_MIN = 1000;
	public static final int BATCH_SIZE_MAX = 10000;
	public static final int BATCH_SIZE_DEF = 1000;

	public static final int QUERY_TIMEOUT_MIN = 5; // secs
	public static final int QUERY_TIMEOUT_MAX = 60000; 
	public static final int QUERY_TIMEOUT_DEF = 60;
	
	public static final int NUMBER_OF_CHARACTERS_PER_COLUMN_MIN = 5; // secs
	public static final int NUMBER_OF_CHARACTERS_PER_COLUMN_MAX = 1000; 
	public static final int NUMBER_OF_CHARACTERS_PER_COLUMN_DEF = 100;
	
	public static final int NUMBER_OF_CHARACTERS_PER_LINE_MIN = 40;
	public static final int NUMBER_OF_CHARACTERS_PER_LINE_MAX = 1000;
	public static final int NUMBER_OF_CHARACTERS_PER_LINE_DEF = 200;
}
