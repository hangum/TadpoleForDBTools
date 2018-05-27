/**
 * 
 */
package com.hangum.tadpole.rdb.core.editors.main.parameter;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * @author nilriri
 *
 */
public class KeyEventComboBoxCellEditor extends ComboBoxCellEditor {
	private TableViewer viewer = null;
	private String[] items = null;
	private int column = -1;

	/**
	 * 
	 */
	public KeyEventComboBoxCellEditor() {
	}

	/**
	 * @param parent
	 * @param items
	 */
	public KeyEventComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items);
	}

	/**
	 * @param items
	 * @param parent
	 */
	public KeyEventComboBoxCellEditor(int column, TableViewer viewer,
			String[] items) {
		super(viewer.getTable(), items);
		this.viewer = viewer;
		this.column = column;
		this.items = items;
	}

	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		super.keyReleaseOccured(keyEvent);
		
		ComboBoxCellEditor cbe = (ComboBoxCellEditor)this;
		
		CCombo cm = (CCombo)cbe.getControl();
		
		// 아이템 목록이 표시되어 있으면...
		if ( cm.getListVisible() ) {
			if (keyEvent.keyCode == SWT.CR || keyEvent.keyCode == SWT.KEYPAD_CR) {
				// 엔터키을 누른경우 선택값을 반영후 편집상태를 계속유지하도록 한다.
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex());
				viewer.editElement(element, column);
				return;
			}
			return;
		}
		
		
		if (keyEvent.keyCode == SWT.ARROW_UP) {
			if (-1 < viewer.getTable().getSelectionIndex()) {
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex() - 1);
				if (element != null){ 
					viewer.editElement(element, column);
				} else {
					element = viewer.getElementAt(viewer.getTable().getItemCount() - 1);
					viewer.editElement(element, column);
				}
			}
		} else if (keyEvent.keyCode == SWT.ARROW_DOWN) {
			if (viewer.getTable().getItemCount() > viewer.getTable().getSelectionIndex()) {
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex() + 1);
				if (element != null){ 
					viewer.editElement(element, column);
				} else {
					element = viewer.getElementAt(0);
					viewer.editElement(element, column);
				}
			}
		}else if (keyEvent.keyCode == SWT.ARROW_LEFT) {
			if (column > 0) {
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex());
				viewer.editElement(element, column - 1);
				// 이동하려는 컬럼이 수정불가능한 cell일 경우 이동하지 않는다.
				// TODO : viewer에서 column 인덱스를 이용해 수정가능한 컬럼인지 확인하는 방법이 있으면....
				// EditingSupport의 canEdit()정보를 이용할 수 없나??
				if(!viewer.isCellEditorActive()) viewer.editElement(element, column);
			}
		} else if (keyEvent.keyCode == SWT.ARROW_RIGHT) {
			if (column < viewer.getTable().getColumnCount() - 1){
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex());
				viewer.editElement(element, column + 1);
				// 이동하려는 컬럼이 수정불가능한 cell일 경우 이동하지 않는다.
				// TODO : viewer에서 column 인덱스를 이용해 수정가능한 컬럼인지 확인하는 방법이 있으면....
				// EditingSupport의 canEdit()정보를 이용할 수 없나??
				if(!viewer.isCellEditorActive()) viewer.editElement(element, column);
			}
		}

	}

}
