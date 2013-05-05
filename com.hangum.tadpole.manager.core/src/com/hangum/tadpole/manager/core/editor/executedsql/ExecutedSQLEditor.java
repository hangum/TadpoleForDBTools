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
package com.hangum.tadpole.manager.core.editor.executedsql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.DBOperationType;
import com.hangum.tadpole.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.util.tables.SQLHistoryCreateColumn;
import com.hangum.tadpole.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.util.tables.SQLHistorySorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * 실행한 쿼리.
 * 
 * @author hangum
 *
 */
public class ExecutedSQLEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExecutedSQLEditor.class);

	public static String ID = "com.hangum.tadpole.manager.core.editor.manager.executed_sql";
	
	private Combo comboOperationType;
	private Combo comboGroup;
	private Combo comboDisplayName;
	
	private DateTime dateTimeSearch;
	private Text textMillis;
	private TableViewer tvList;
	
	private Button btnSearch;
	
	/** result list */
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	
	/**
	 * 
	 */
	public ExecutedSQLEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(6, false);
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		Label lblOperationType = new Label(compositeHead, SWT.NONE);
		lblOperationType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOperationType.setText("Operation Type");
		
		comboOperationType = new Combo(compositeHead, SWT.NONE);
		comboOperationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblGroup = new Label(compositeHead, SWT.NONE);
		lblGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroup.setText("Group");
		
		comboGroup = new Combo(compositeHead, SWT.NONE);
		comboGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(compositeHead, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		comboDisplayName = new Combo(compositeHead, SWT.BORDER);
		comboDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblExecuteTime = new Label(compositeHead, SWT.NONE);
		lblExecuteTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExecuteTime.setText("Execute Time");
		
		dateTimeSearch = new DateTime(compositeHead, SWT.BORDER);
		
		Label lblExecuteMills = new Label(compositeHead, SWT.NONE);
		lblExecuteMills.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExecuteMills.setText("during execute");
		
		textMillis = new Text(compositeHead, SWT.BORDER);
		textMillis.setText("100");
		textMillis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblmillis = new Label(compositeHead, SWT.NONE);
		lblmillis.setText("(millis)");
		
		btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText("Search");
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvList = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvList.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// auto column layout
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tvList.getTable());
		tvList.getTable().setLayout(layoutColumnLayout);
		
		SQLHistorySorter sorterHistory = new SQLHistorySorter();
		SQLHistoryCreateColumn.createTableHistoryColumn(tvList, sorterHistory, layoutColumnLayout);
		
		tvList.setLabelProvider(new SQLHistoryLabelProvider());
		tvList.setContentProvider(new ArrayContentProvider());
		tvList.setInput(listSQLHistory);
		tvList.setComparator(sorterHistory);
		
		initUIData();
	}
	
	/**
	 * search 
	 */
	private void search() {
		listSQLHistory.clear();
		
		
	}
	
	/**
	 * 데이터를 초기 로드합니다.
	 */
	private void initUIData() {
		
		// operation type combo
		for (DBOperationType opType : DBOperationType.values()) {
			comboOperationType.add(opType.getTypeName());
		}
		comboOperationType.select(1);
		
		// db groupData combo
		try {
			List<String> groupName = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getSeq());
			for (String string : groupName) comboGroup.add(string);
			comboGroup.select(0);
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		
		// database name combo
		try {
			List<UserDBDAO> listUserDBDAO = TadpoleSystem_UserDBQuery.getUserDB();
			for (UserDBDAO userDBDAO : listUserDBDAO) {
				comboDisplayName.add(userDBDAO.getDisplay_name());
			}
			comboDisplayName.select(0);
		} catch(Exception e) {
			logger.error("get db list", e);
		}
		
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		ExecutedSQLEditorInput esqli = (ExecutedSQLEditorInput)input;
		setPartName(esqli.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		btnSearch.setFocus();
	}

}
