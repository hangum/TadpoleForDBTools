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
package com.hangum.db.browser.rap.core.editors.main;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.db.browser.rap.core.editors.main.browserfunction.EditorBrowserFunctionService;

/**
 * SQL to Application string dropdown string
 * 
 * @author hangum
 * @deprecated ToolBar의 ToolItem의 버그(?)로 인해 
 */
public class SQLToApplicationSelectionListener extends SelectionAdapter {
	private static final Logger logger = Logger.getLogger(SQLToApplicationSelectionListener.class);
	private MainEditor editor;
	private ToolItem dropdown;
	private Menu menu;

	public SQLToApplicationSelectionListener(MainEditor editor, ToolItem dropdown) {
		this.editor = editor;
		this.dropdown = dropdown;
		menu = new Menu(dropdown.getParent().getShell());
	}

	public void add(String item) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(item);
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				MenuItem selected = (MenuItem) event.widget;
				dropdown.setText(selected.getText());
			}
		});
	}

	public void widgetSelected(SelectionEvent event) {
		
		if (event.detail == SWT.ARROW) {
			ToolItem item = (ToolItem) event.widget;
			Rectangle rect = item.getBounds();
			Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);
		} else {
			editor.browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SQL_TO_APPLICATION);
		}
	}
	
	public String getSelectionText() {
		return this.dropdown.getText();
	}
}
