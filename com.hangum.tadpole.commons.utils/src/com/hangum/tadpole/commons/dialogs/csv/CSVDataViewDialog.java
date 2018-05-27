/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.dialogs.csv;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.csv.CSVUtils;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.AutoResizeTableLayout;
import com.hangum.tadpole.commons.util.GlobalImageUtils;

/**
 * CSV data view dialog
 * 
 * @author hangum
 *
 */
public class CSVDataViewDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(CSVDataViewDialog.class);
	
	final int DOWNLOAD_BTN_ID = IDialogConstants.CLIENT_ID + 1;
	private List<String[]> listData = new ArrayList<String[]>();
	
	private String strData = "";
	private Label lblTotalRowIs;
	private TableViewer tableViewer;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CSVDataViewDialog(Shell parentShell, String strData) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.strData = strData;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Data viewer");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		lblTotalRowIs = new Label(compositeHead, SWT.NONE);
//		lblTotalRowIs.setText("Total row is %s");
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();

		return container;
	}
	
	/**
	 * initialize ui
	 * 
	 */
	private void initUI() {
		listData = CSVUtils.readData(strData, ',');
		lblTotalRowIs.setText(String.format(CommonMessages.get().TotalRowIs, listData.size()-1));
		createTableColumn();
		
		tableViewer.setLabelProvider(new CSViewerLabelProvider());
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		listData.remove(0);
		tableViewer.setInput(listData);
		packTable(tableViewer.getTable());
	}
	
	public static void packTable(Table table) {
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(table);
		table.setLayout(layoutColumnLayout);
		
		TableColumn[] columns = table.getColumns();		
		for (int i = 0; i < columns.length; i++) {			
			columns[i].pack();
			
			// 10의 숫자는 경험치이고 컬럼이 적었을 경우 좀더 편하게 보이는듯합니다.
			layoutColumnLayout.addColumnData(new ColumnPixelData(columns[i].getWidth() + 10));
		}

	}
	
	/**
	 * create table column
	 */
	private void createTableColumn() {
		try {
			String[] arryHead = listData.get(0);
			for(int i=0; i<arryHead.length; i++) {
				final TableViewerColumn tv = new TableViewerColumn(tableViewer, SWT.LEFT);
				final TableColumn tc = tv.getColumn();
				
				tc.setWidth(100);
				tc.setText(arryHead[i]);
				tc.setResizable(true);
				tc.setMoveable(true);
			}	// end for
		
		} catch(Exception e) { 
			logger.error("CSV viewer crate column", e);
		}	
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, DOWNLOAD_BTN_ID, CommonMessages.get().Download, false);
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(566, 640);
	}

}
/**
* csv viewer lable provider
* 
* @author hangum
*
*/
class CSViewerLabelProvider  extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(CSViewerLabelProvider.class);
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String[] arryData = (String[])element;
		try {
			return arryData[columnIndex];
		} catch(Exception e) {
			logger.error("Cannot find colum index:" +  e.getMessage());
		}
		
		return "";
	}
	
}
