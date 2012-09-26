/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.browser.rap.core.dialog.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

/**
 * sql editor 단축키 도움말.
 * 
 * @author hangum
 *
 */
public class ShortcutHelpDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ShortcutHelpDialog.class);

	protected Object result;
	protected Shell shell;
	private Table table;
	
	private TableViewer tableViewer;
	private List<ShortcutHelpDAO> listShortcut = new ArrayList<ShortcutHelpDAO>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ShortcutHelpDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.NONE);
		shell.setSize(240, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));

		// shell을 오른쪽 하단에 놓을수 있도록 합니다.
		Shell mainShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		int x = mainShell.getSize().x;
		int y = mainShell.getSize().y;
		if(logger.isDebugEnabled()) logger.debug("[x] " + x + ":" + (x - 240) + "[y] " + y + ":" + (y - 300));
		
		// 현재 shell location
		shell.setLocation(x - 240, y - 300);
		
		tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText("Description");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnKey = tableViewerColumn_1.getColumn();
		tblclmnKey.setWidth(120);
		tblclmnKey.setText("Key");

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ShortcutLabelProvider());
		tableViewer.setInput(listShortcut);
		
		initData();
		
		table.setSelection(0);
		table.setFocus();
	}
	
	/**
	 * 단축키 데이터
	 */
	private void initData() {
		listShortcut.add( new ShortcutHelpDAO("Save", 			"Ctrl + S") 		);
		listShortcut.add( new ShortcutHelpDAO("Execute Query", 	"Ctrl + enter") 	);
		listShortcut.add( new ShortcutHelpDAO("Execute Query", 	"F5") 				);
		listShortcut.add( new ShortcutHelpDAO("Execute Plan", 	"Ctrl + E") 		);
		listShortcut.add( new ShortcutHelpDAO("Query Format", 	"Ctrl + Shift + F") );
		listShortcut.add( new ShortcutHelpDAO("Query History", 	"Ctrl + H") 		);
		listShortcut.add( new ShortcutHelpDAO("To Lower case", 	"Ctrl + Shift + Y") );
		listShortcut.add( new ShortcutHelpDAO("To Upper case", 	"Ctrl + Shift + X") );
		listShortcut.add( new ShortcutHelpDAO("Shortcut Help", 	"Ctrl + Shift + L") );
		listShortcut.add( new ShortcutHelpDAO("Clear page", 	"F7") 				);
		
		listShortcut.add( new ShortcutHelpDAO("Select All", 	"Ctrl + A") 		);
		listShortcut.add( new ShortcutHelpDAO("Go to Line", 	"Ctrl + L") 		);
		listShortcut.add( new ShortcutHelpDAO("Copy text", 		"Ctrl + C") 		);
		listShortcut.add( new ShortcutHelpDAO("Past text", 		"Ctrl + V") 		);
		
		tableViewer.refresh(listShortcut);
	}
}

class ShortcutLabelProvider extends LabelProvider implements ITableLabelProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ShortcutLabelProvider.class);

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ShortcutHelpDAO dao = (ShortcutHelpDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getName();
		case 1: return dao.getKey();
		}
		
		return "*** not set column ***";
	}
	
}
