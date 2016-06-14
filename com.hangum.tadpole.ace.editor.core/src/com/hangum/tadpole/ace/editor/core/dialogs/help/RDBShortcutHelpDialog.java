/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.ace.editor.core.dialogs.help;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.Messages;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ServletUserAgent;

/**
 * sql editor 단축키 도움말.
 * 
 * 주의) 이 dialog은 application model로 설정되어 있습니다.
 * 포커스 아웃 이벤트가 브라우저 위젲으로 이동하면 작동되지 않아서 입니다.  
 * 
 * @author hangum
 *
 */
public class RDBShortcutHelpDialog extends AbstractShortCutDialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBShortcutHelpDialog.class);

	protected Object result;
	protected Shell shlEditorShortcutDialog;
	
	protected TableViewer tableViewer;
	

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
		shlEditorShortcutDialog = new Shell(getParent(), SWT.CLOSE | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		shlEditorShortcutDialog.setText(Messages.get().RDBShortcutHelpDialog_0);
		shlEditorShortcutDialog.setSize(300, 330);
		shlEditorShortcutDialog.setLayout(new GridLayout(1, false));

		// shell을 오른쪽 하단에 놓을수 있도록 합니다.
		Shell mainShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		int x = mainShell.getSize().x;
		int y = mainShell.getSize().y;
		
		// 현재 shell location
		shlEditorShortcutDialog.setLocation(x - 300, y - 300);
		
		tableViewer = new TableViewer(shlEditorShortcutDialog, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
			}
		});
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText(Messages.get().RDBShortcutHelpDialog_1);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnKey = tableViewerColumn_1.getColumn();
		tblclmnKey.setWidth(220);
		tblclmnKey.setText(Messages.get().RDBShortcutHelpDialog_2);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ShortcutLabelProvider());
		tableViewer.setInput(listShortcut);
		
		initData();
		
		tableViewer.getTable().setSelection(0);
		tableViewer.getTable().setFocus();
	}
	
	/**
	 * 단축키 데이터
	 */
	private void initData() {
		listShortcut.add(new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_5, 	"F4")); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_8, 	prefixCtrlShortcut +"+ I")); //$NON-NLS-2$
		
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_4, 			prefixCtrlShortcut + "+ S") 		); //$NON-NLS-1$
		if(RequestInfoUtils.findOSSimpleType() == ServletUserAgent.OS_SIMPLE_TYPE.MACOSX) {
			listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_3, 	"Ctrl + Space|Option + Space ") 		); //$NON-NLS-1$
		} else {
			listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_3, 	"Ctrl + Space") 		); //$NON-NLS-2$ //$NON-NLS-1$
		}
		
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_9, 	prefixCtrlShortcut + "+ Enter|F9|F5") 	); //$NON-NLS-2$ //$NON-NLS-1$
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_11, 	prefixCtrlShortcut + "+ E") 		); //$NON-NLS-2$ //$NON-NLS-1$
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_13, 	prefixCtrlShortcut + " + " + prefixShiftShortcut + "+ F") );  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		super.initShortList();
		
		tableViewer.refresh(listShortcut);		
	}
}