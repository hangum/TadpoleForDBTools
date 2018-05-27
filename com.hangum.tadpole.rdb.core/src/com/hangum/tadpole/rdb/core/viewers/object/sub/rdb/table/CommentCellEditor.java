/**
 * 
 */
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * @author nilriri
 *
 */
public class CommentCellEditor extends TextCellEditor {
	private TableViewer viewer = null;
	private int column = -1;

	/**
	 * 
	 */
	public CommentCellEditor() {
	}

	/**
	 * @param parent
	 */
	public CommentCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 */
	public CommentCellEditor(int column, TableViewer viewer) {
		super(viewer.getTable());
		this.viewer = viewer;
		this.column = column;
	}

	/**
	 * @param parent
	 * @param style
	 */
	public CommentCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
    protected void keyReleaseOccured(KeyEvent keyEvent) {
		super.keyReleaseOccured(keyEvent);
		
		if (keyEvent.keyCode == SWT.ARROW_UP) { 
			if (-1 < viewer.getTable().getSelectionIndex()){
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex() - 1);
				if (element != null){ 
					viewer.editElement(element, column);
				} else {
					element = viewer.getElementAt(viewer.getTable().getItemCount() - 1);
					viewer.editElement(element, column);
				}
			}
		}else if (keyEvent.keyCode == SWT.ARROW_DOWN) { 
			if (viewer.getTable().getItemCount() > viewer.getTable().getSelectionIndex()){
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
