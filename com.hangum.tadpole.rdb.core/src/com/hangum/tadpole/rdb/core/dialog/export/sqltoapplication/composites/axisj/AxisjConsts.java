/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.axisj;

/**
 * axisj label provider
 * 
 * @author nilriri
 *
 */
public class AxisjConsts {

/*		
        key      : "no",        // {String} -- 데이터와 맵핑할 키 입니다. key 명칭은 reservedKey
        label    : "번호",      // {String} -- 사용자에게 보여줄 컬럼명입니다.
        width    : 50,          // {Number|String} -- 컬럼의 가로길이를 설정합니다. 픽셀단위의 숫자나 "*" 문자를 사용할 수 있습니다. "*"을 사용하는 경우 그리드의 가로 길이에 따라 컬럼의 결이가 가변적으로 변합니다.
        align    : "right",     // {String} ["left"] -- 컬럼 내용의 정렬을 설정합니다. "left"|"center"|"right" 값을 사용할 수 있습니다.
        sort     : "asc",       // {String|Boolean} [""] -- 컬럼의 정렬을 지정합니다. "asc"|"desc"|false 값을 사용할 수 있습니다. false 값을 사용하면 컬럼의 정렬을 비활성화 합니다.
        colHeadTool : true      // {Boolean} -- 컬럼 display 여부를 설정 합니다.
        formatter: "money",     // {String|Function} -- 컬럼의 값을 표현하는 방식을 지정합니다. "money", "dec", "html", "checkbox", "radio", function은 아래 formatter 함수를 참고하세요.
        tooltip  : "money",     // {String|Function} -- 툴팁의 값을 표현하는 방식을 지정합니다. 툴팁을 지정하면 td div.bodyNode에 title 속성으로 값이 표현됩니다. 위 formatter와 동일한 변수를 사용합니다.
        disabled : function(){},// {Boolean|Function} -- formatter가 checkbox, radio인 경우 input의 disabled 값을 지정합니다. disabled(true|flase)를 반환하는 함수를 작성합니다. 아래 disabled 함수를 참고하세요.
        checked  : function(){} // {Boolean|Function} -- formatter가 checkbox, radio인 경우 input의 checked 값을 지정합니다. checked(true|flase)를 반환하는 함수를 작성합니다. 아래 checked 함수를 참고하세요.
*/
		
	public static final String NO = "No";
	public static final String KEY = "Key";
	public static final String LABEL = "Label";
	public static final String WIDTH = "Width";
	public static final String ALIGN = "Align";
	public static final String SORT = "Sort";
	public static final String HEADTOOL = "Head Tool";
	public static final String FORMATTER = "Formatter";
	public static final String TOOLTIP = "Tooltip";
	public static final String DISABLE = "Disabled";
	public static final String CHECKED = "Checked";

	public static final int NO_IDX = 0;
	public static final int KEY_IDX = 1;
	public static final int LABEL_IDX = 2;
	public static final int WIDTH_IDX = 3;
	public static final int ALIGN_IDX = 4;
	public static final int SORT_IDX = 5;
	public static final int HEADTOOL_IDX = 6;
	public static final int FORMATTER_IDX = 7;
	public static final int TOOLTIP_IDX = 8;
	public static final int DISABLE_IDX = 9;
	public static final int CHECKED_IDX = 10;

	public static final int NO_SIZE = 40;
	public static final int KEY_SIZE = 90;
	public static final int LABEL_SIZE = 90;
	public static final int WIDTH_SIZE = 50;
	public static final int ALIGN_SIZE = 60;
	public static final int SORT_SIZE = 60;
	public static final int HEADTOOL_SIZE = 60;
	public static final int FORMATTER_SIZE = 60;
	public static final int TOOLTIP_SIZE = 100;
	public static final int DISABLE_SIZE = 100;
	public static final int CHECKED_SIZE = 100;

	public static final String[] names = { NO, KEY, LABEL, WIDTH, ALIGN, SORT, HEADTOOL, FORMATTER, TOOLTIP, DISABLE, CHECKED };
	public static final int[] sizes = { NO_SIZE, KEY_SIZE, LABEL_SIZE, WIDTH_SIZE, ALIGN_SIZE, SORT_SIZE, HEADTOOL_SIZE, FORMATTER_SIZE, TOOLTIP_SIZE, DISABLE_SIZE, CHECKED_SIZE };

	
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	public static final String[] aligns = { "Left", "Center", "Right"};
	public static final String[] alignValue = { "\"left\"", "\"center\"", "\"right\""};

	public static final int FALSE = 0;
	public static final int ASC = 1;
	public static final int DESC = 2;

	public static final String[] sorts = { "False", "Ascending", "Descending"};
	public static final String[] sortValue = { "false", "\"asc\"", "\"desc\""};

}