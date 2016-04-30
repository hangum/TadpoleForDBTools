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
 * AXISJ HEADER DAO
 * 
 * @author hangum
 *
 */
public class AxisjHeaderDAO {
	private int seqNo;//
    private String key;//      : "no",        // {String} -- 데이터와 맵핑할 키 입니다. key 명칭은 reservedKey
    private String label;//    : "번호",      // {String} -- 사용자에게 보여줄 컬럼명입니다.
    private int width;//    : 50,          // {Number|String} -- 컬럼의 가로길이를 설정합니다. 픽셀단위의 숫자나 "*" 문자를 사용할 수 있습니다. "*"을 사용하는 경우 그리드의 가로 길이에 따라 컬럼의 결이가 가변적으로 변합니다.
    private int align;//    : "right",     // {String} ["left"] -- 컬럼 내용의 정렬을 설정합니다. "left"|"center"|"right" 값을 사용할 수 있습니다.
    private int sort;//     : "asc",       // {String|Boolean} [""] -- 컬럼의 정렬을 지정합니다. "asc"|"desc"|false 값을 사용할 수 있습니다. false 값을 사용하면 컬럼의 정렬을 비활성화 합니다.
	private boolean colHeadTool;// : true      // {Boolean} -- 컬럼 display 여부를 설정 합니다.
    private String formatter;//: "money",     // {String|Function} -- 컬럼의 값을 표현하는 방식을 지정합니다. "money", "dec", "html", "checkbox", "radio", function은 아래 formatter 함수를 참고하세요.
    private String tooltip;//  : "money",     // {String|Function} -- 툴팁의 값을 표현하는 방식을 지정합니다. 툴팁을 지정하면 td div.bodyNode에 title 속성으로 값이 표현됩니다. 위 formatter와 동일한 변수를 사용합니다.
    private String disabled;// : function(){},// {Boolean|Function} -- formatter가 checkbox, radio인 경우 input의 disabled 값을 지정합니다. disabled(true|flase)를 반환하는 함수를 작성합니다. 아래 disabled 함수를 참고하세요.
    private String checked;//  : function(){} // {Boolean|Function} -- formatter가 checkbox, radio인 경우 input의 checked 값을 지정합니다. checked(true|flase)를 반환하는 함수를 작성합니다. 아래 checked 함수를 참고하세요.

    public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getAlign() {
		return align;
	}
	public void setAlign(int align) {
		this.align = align;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public boolean isColHeadTool() {
		return colHeadTool;
	}
	public void setColHeadTool(boolean colHeadTool) {
		this.colHeadTool = colHeadTool;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
}
