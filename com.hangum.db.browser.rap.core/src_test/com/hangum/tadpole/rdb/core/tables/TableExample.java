package com.hangum.tadpole.rdb.core.tables;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TableExample implements IEntryPoint {
	private Text text_1;
	private Table table;
	private static boolean isRight = true;

	/**
	 * @wbp.parser.entryPoint
	 */
	public int createUI() {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;

		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		createTableColumn(SWT.RIGHT);
		
		Button btnTableLeftAlignment = new Button(composite, SWT.NONE);
		btnTableLeftAlignment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnAlginment();
			}
		});
		btnTableLeftAlignment.setText("column alignment");

		shell.setSize(514, 453);
		shell.open();

		return 0;
	}
	
	/**
	 * crate table column
	 * 
	 * @param alignment
	 */
	private void createTableColumn(int alignment) {
		TableColumn tblclmnRight = new TableColumn(table, alignment);//SWT.RIGHT);
		tblclmnRight.setWidth(200);
		tblclmnRight.setText("Column1");
		
		TableColumn tblclmnRight2 = new TableColumn(table, alignment);//SWT.RIGHT);
		tblclmnRight2.setWidth(200);
		tblclmnRight2.setText("Column2");
		
		TableItem item = new TableItem(table, alignment);
		item.setText(0, "first column");
		item.setText(1, "second column");
	}
	
	/**
	 * table column alignment
	 */
	private void columnAlginment() {
		
		// data remove
		int itemCnt = table.getItemCount();
		for(int i=0; i<itemCnt; i++) {
			table.getItem(0).dispose();
		}
		
		// column dispose
		int columnCnt = table.getColumnCount();
		for(int i=0; i<columnCnt; i++) {
			table.getColumn(0).dispose();
		}
		
		if(isRight) {
			createTableColumn(SWT.LEFT);
			isRight = false;
		} else {
			createTableColumn(SWT.RIGHT);
			isRight = true;
		}		
	}
}
