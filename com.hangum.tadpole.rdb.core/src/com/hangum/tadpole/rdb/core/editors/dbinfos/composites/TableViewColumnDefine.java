package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;

public class TableViewColumnDefine {

	String column; // display column name - dao column name
	String caption; // table column caption - header title
	int width; // column header width
	int align; // data align
	String preValue; // compare value for cell merge
	boolean merge; // cell merge
	boolean sortable; // 컬럼 헤더 클릭시 데이터의 정렬 여부를 지정한다.
	EditingSupport editor; // 컬럼내용을 수정하기 위한 클래스를 지정한다.

	public TableViewColumnDefine(String column) {
		this(column, StringUtils.capitalize(column.toLowerCase().replace("_", "")));
	}

	public TableViewColumnDefine(String column, String caption) {
		this(column, caption, 100);
	}

	public TableViewColumnDefine(String column, String caption, int width) {
		this(column, caption, width, SWT.LEFT);
	}

	public TableViewColumnDefine(String column, String caption, int width, int align) {
		this(column, caption, width, align, false);
	}

	public TableViewColumnDefine(String column, String caption, int width, int align, boolean merge) {
		this(column, caption, width, align, merge, true);
	}

	TableViewColumnDefine(String column, String caption, int width, int align, boolean merge, boolean sortable) {
		this.column = column;
		this.caption = caption == null ? StringUtils.capitalize(column.toLowerCase().replace("_", "")) : caption;
		this.width = width == 0 ? 100 : width;
		this.align = align <= 0 ? SWT.LEFT : align;
		this.merge = merge;
		this.sortable = sortable;
		this.preValue = "";
	}

	public TableViewColumnDefine assignEditingSupport(EditingSupport obj) {
		this.editor = obj;
		return this;
	}
}
