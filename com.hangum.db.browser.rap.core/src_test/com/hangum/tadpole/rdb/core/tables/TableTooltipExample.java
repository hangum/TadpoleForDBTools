package com.hangum.tadpole.rdb.core.tables;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TableTooltipExample implements IEntryPoint {
	
	private static class MyContentProvider implements IStructuredContentProvider {
		
		public Object[] getElements(Object inputElement) {
			return new String[] { "one", "two", "three", "four", "five", "six",
					"seven", "eight", "nine", "ten" };
		}
		
		public void dispose() {
		
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
		}
	}


	/**
	 * @wbp.parser.entryPoint
	 */
	public int createUI() {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		TableViewer v = new TableViewer(shell, SWT.FULL_SELECTION);
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
		v.setContentProvider(new MyContentProvider());
		ColumnViewerToolTipSupport.enableFor(v);//,ToolTip.NO_RECREATE);
		
		CellLabelProvider labelProvider = new CellLabelProvider() {

			public String getToolTipText(Object element) {
				return "Tooltip (" + element + ")";
			}

			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			public int getToolTipDisplayDelayTime(Object object) {
				return 2000;
			}

			public int getToolTipTimeDisplayed(Object object) {
				return 5000;
			}

			public void update(ViewerCell cell) {
				cell.setText(cell.getElement().toString());

			}
		};

		TableViewerColumn column = new TableViewerColumn(v, SWT.NONE);
		column.setLabelProvider(labelProvider);
		column.getColumn().setText("Column 1");
		column.getColumn().setWidth(100);

		v.setInput("");

		shell.setSize(514, 453);
		shell.open();

		return 0;
	}
	
	
	
}
