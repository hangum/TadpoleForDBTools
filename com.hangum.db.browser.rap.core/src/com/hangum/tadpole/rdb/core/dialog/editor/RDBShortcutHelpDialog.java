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
package com.hangum.tadpole.rdb.core.dialog.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
 * 주의) 이 dialog은 application model로 설정되어 있습니다.
 * 포커스 아웃 이벤트가 브라우저 위젲으로 이동하면 작동되지 않아서 입니다.  
 * 
 * @author hangum
 *
 */
public class RDBShortcutHelpDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBShortcutHelpDialog.class);

	protected Object result;
	protected Shell shlEditorShortcutDialog;
	
	protected TableViewer tableViewer;
	protected List<ShortcutHelpDAO> listShortcut = new ArrayList<ShortcutHelpDAO>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RDBShortcutHelpDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlEditorShortcutDialog.open();
		shlEditorShortcutDialog.layout();
		Display display = getParent().getDisplay();
		while (!shlEditorShortcutDialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	public void createContents() {
		shlEditorShortcutDialog = new Shell(getParent(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		shlEditorShortcutDialog.setSize(240, 300);
//		shlEditorShortcutDialog.setText("Editor Shortcut Dialog");
		shlEditorShortcutDialog.setLayout(new GridLayout(1, false));

		// shell을 오른쪽 하단에 놓을수 있도록 합니다.
		Shell mainShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		int x = mainShell.getSize().x;
		int y = mainShell.getSize().y;
		
		// 현재 shell location
		shlEditorShortcutDialog.setLocation(x - 240, y - 300);
		
		tableViewer = new TableViewer(shlEditorShortcutDialog, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				//
				// orionhub editor가 포커스를 받으면 이벤트가 발생하지 않는다. 끙...
				//
				//System.out.println("====== focus out =========================");
			}
		});
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
		
		//
		tableViewer.getTable().setSelection(0);//setSelection(new StructuredSelection(listShortcut.get(0)), true);
		tableViewer.getTable().setFocus();
	}
	
	/**
	 * 단축키 데이터
	 */
	private void initData() {
//		listShortcut.add( new ShortcutHelpDAO("Save", 			"Ctrl + S") 		);
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
		
		listShortcut.add( new ShortcutHelpDAO("Delete Line",	"Ctrl + D") 		);		
		tableViewer.refresh(listShortcut);		
	}
	
	
}

/**
 * label provider
 * @author hangum
 *
 */
class ShortcutLabelProvider extends LabelProvider implements ITableLabelProvider {
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
