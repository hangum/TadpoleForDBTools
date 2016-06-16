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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.MultilineTextDialog;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.CommentCellEditor;

/**
 * AXISJ EditingSupport
 * 
 * @author nilriri
 */
public class AxisjEditingSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917152389392816571L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLToStringDialog.class);

	private final TableViewer viewer;
	private int columnIndex;

	public AxisjEditingSupport(TableViewer viewer, int columnIndex) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
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

		final AxisjHeaderDAO dao = (AxisjHeaderDAO) element;

		if (columnIndex == AxisjConsts.ALIGN_IDX) {

			return new ComboBoxCellEditor(viewer.getTable(), AxisjConsts.aligns);

		} else if (columnIndex == AxisjConsts.FORMATTER_IDX){
			return new DialogCellEditor(viewer.getTable()) {
				@Override
		        protected Object openDialogBox(Control cellEditorWindow) {
		            Shell shell = Display.getDefault().getActiveShell();		            
		            //String original = dao.getFormatter();
		            String original = AxisjEditingSupport.this.getValue(element).toString();
		            
		            AxisjFormatterDialog dialog = new AxisjFormatterDialog(shell, original, viewer.getTable().getColumn(columnIndex).getText());
		            if (IStatus.OK == dialog.open()) {
		                setValue(dialog.getValue());
		                return dialog.getValue();
		            }else{
		            	return original;
		            }
		        }
			};			
		} else if (columnIndex == AxisjConsts.TOOLTIP_IDX |
				columnIndex == AxisjConsts.DISABLE_IDX |
				columnIndex == AxisjConsts.CHECKED_IDX ) {
			return new DialogCellEditor(viewer.getTable()) {
				@Override
		        protected Object openDialogBox(Control cellEditorWindow) {
		            Shell shell = Display.getDefault().getActiveShell();		            
		            //String original = dao.getFormatter();
		            String original = AxisjEditingSupport.this.getValue(element).toString();
		            
		            MultilineTextDialog dialog = new MultilineTextDialog(shell, original, viewer.getTable().getColumn(columnIndex).getText());
		            if (IStatus.OK == dialog.open()) {
		                setValue(dialog.getValue());
		                return dialog.getValue();
		            }else{
		            	return original;
		            }
		        }
			};
		} else if (columnIndex == AxisjConsts.HEADTOOL_IDX) {

			return new CheckboxCellEditor(null, SWT.CHECK);// | SWT.READ_ONLY);

		} else if (columnIndex == AxisjConsts.SORT_IDX) {

			return new ComboBoxCellEditor(viewer.getTable(), AxisjConsts.sorts);// | SWT.READ_ONLY);

		} else {

			return new CommentCellEditor(columnIndex, viewer);

		}
	}

	@Override
	protected boolean canEdit(Object element) {
		AxisjHeaderDAO dao = (AxisjHeaderDAO) element;

		if (columnIndex == AxisjConsts.NO_IDX || columnIndex == AxisjConsts.KEY_IDX) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected Object getValue(Object element) {
		AxisjHeaderDAO dao = (AxisjHeaderDAO) element;

		if (columnIndex == AxisjConsts.LABEL_IDX) {
			return dao.getLabel();
		} else if (columnIndex == AxisjConsts.WIDTH_IDX) {
			return dao.getWidth()+"";
		} else if (columnIndex == AxisjConsts.ALIGN_IDX) {
			return dao.getAlign();
		} else if (columnIndex == AxisjConsts.SORT_IDX) {
			return dao.getSort();
		} else if (columnIndex == AxisjConsts.HEADTOOL_IDX) {
			return dao.isColHeadTool();
		} else if (columnIndex == AxisjConsts.FORMATTER_IDX) {
			return dao.getFormatter();
		} else if (columnIndex == AxisjConsts.TOOLTIP_IDX) {
			return dao.getTooltip();
		} else if (columnIndex == AxisjConsts.DISABLE_IDX) {
			return dao.getDisabled();
		} else if (columnIndex == AxisjConsts.CHECKED_IDX) {
			return dao.getChecked();
		} else {
			return null;
		}
	}

	@Override
	protected void setValue(Object element, Object value) {
		AxisjHeaderDAO dao = (AxisjHeaderDAO) element;
		if (columnIndex == AxisjConsts.LABEL_IDX) {
			dao.setLabel(value.toString());
		} else if (columnIndex == AxisjConsts.WIDTH_IDX) {
			try{
				dao.setWidth(Integer.valueOf(value.toString()));
			}catch(Exception e){
				dao.setWidth(100);
			}			
		} else if (columnIndex == AxisjConsts.HEADTOOL_IDX) {
			dao.setColHeadTool((Boolean) value);
		} else if (columnIndex == AxisjConsts.ALIGN_IDX) {
			try{
				dao.setAlign(Integer.valueOf(value.toString()));
			}catch(Exception e){
				dao.setAlign(100);
			}
		} else if (columnIndex == AxisjConsts.SORT_IDX) {
			try{
				dao.setSort(Integer.valueOf(value.toString()));
			}catch(Exception e){
				dao.setSort(100);
			}
		} else if (columnIndex == AxisjConsts.FORMATTER_IDX) {
			dao.setFormatter(value.toString());
		} else if (columnIndex == AxisjConsts.TOOLTIP_IDX) {
			dao.setTooltip(value.toString());
		} else if (columnIndex == AxisjConsts.DISABLE_IDX) {
			dao.setDisabled(value.toString());
		} else if (columnIndex == AxisjConsts.CHECKED_IDX) {
			dao.setChecked(value.toString());
		}

		viewer.update(element, null);
	}

}

class myDialog extends Dialog{

	protected myDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().SQLToStringDialog_2);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}

