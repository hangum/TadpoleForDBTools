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
			if (0 < viewer.getTable().getSelectionIndex()){
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex() - 1);
				viewer.editElement(element, column);
			}
		}else if (keyEvent.keyCode == SWT.ARROW_DOWN) { 
			if (viewer.getTable().getItemCount() > viewer.getTable().getSelectionIndex()){
				Object element = viewer.getElementAt(viewer.getTable().getSelectionIndex() + 1);
				viewer.editElement(element, column);
			}
		}
	}
	
}
