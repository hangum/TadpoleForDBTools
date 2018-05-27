/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.help.core.views.sub.shortcutkey;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ServletUserAgent;
import com.hangum.tadpole.help.core.Messages;

/**
 * ShortcutComposte
 * @author hangum
 *
 */
public class ShortcutKeyComposite extends AbstraceShortcutKeyComposite {
	protected TableViewer tableViewer;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ShortcutKeyComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
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
		tblclmnDescription.setText(Messages.get().RDBShortcutHelpDialog_1);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnKey = tableViewerColumn_1.getColumn();
		tblclmnKey.setWidth(220);
		tblclmnKey.setText(Messages.get().RDBShortcutHelpDialog_2);

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
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
		super.initShortList();
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_4, 		prefixCtrlShortcut + "+ S") 		);
		if(RequestInfoUtils.findOSSimpleType() == ServletUserAgent.OS_SIMPLE_TYPE.MACOSX) {
			listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_3, 	"Ctrl + Space|Option + Space ") 		);
			listShortcut.add(new ShortcutHelpDAO(Messages.get().AbstractShortCutDialog_44, 	prefixCtrlShortcut  + " + " + prefixAltShortcut + "+ F") ); //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_7, 	"Ctrl + Space") 		); //$NON-NLS-2$
			listShortcut.add(new ShortcutHelpDAO(Messages.get().AbstractShortCutDialog_44, 	"Ctrl + H") ); //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_9, 	prefixCtrlShortcut + "+ enter") 	); //$NON-NLS-2$
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_11, 	prefixCtrlShortcut + "+ E") 		); //$NON-NLS-2$
		listShortcut.add( new ShortcutHelpDAO(Messages.get().RDBShortcutHelpDialog_13, 	prefixCtrlShortcut + " + " + prefixShiftShortcut + "+ F") ); //$NON-NLS-2$ //$NON-NLS-3$
		
		tableViewer.refresh(listShortcut);		
	}

}
