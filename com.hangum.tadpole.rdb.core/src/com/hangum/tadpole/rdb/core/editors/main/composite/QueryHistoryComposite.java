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
package com.hangum.tadpole.rdb.core.editors.main.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.engine.sql.util.tables.SQLHistoryCreateColumn;
import com.hangum.tadpole.engine.sql.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLHistorySorter;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;

/**
 * query history composite
 * 
 * @author hangum
 *
 */
public class QueryHistoryComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(QueryHistoryComposite.class);

	/** result composite */
	private ResultMainComposite rdbResultComposite;
	
	private TableViewer tvSQLHistory;
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	private Text textHistoryFilter;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QueryHistoryComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvSQLHistory = new TableViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tvSQLHistory.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				if(!is.isEmpty()) {
					SQLHistoryDAO historyDAO = (SQLHistoryDAO)is.getFirstElement();
					appendText(historyDAO.getStrSQLText());
					
					// google analytic
					AnalyticCaller.track(MainEditor.ID, "QueryHistoryComposite");
				}
			}
		});
		
		Table tableSQLHistory = tvSQLHistory.getTable();
		tableSQLHistory.setLinesVisible(true);
		tableSQLHistory.setHeaderVisible(true);
		tableSQLHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableSQLHistory.setSortDirection(SWT.DOWN);
		
		// auto column layout
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tvSQLHistory.getTable());
		tvSQLHistory.getTable().setLayout(layoutColumnLayout);
		
		SQLHistorySorter sorterHistory = new SQLHistorySorter();
		SQLHistoryCreateColumn.createTableHistoryColumn(tvSQLHistory, sorterHistory, layoutColumnLayout, false);
		
		tvSQLHistory.setLabelProvider(new SQLHistoryLabelProvider());
		tvSQLHistory.setContentProvider(new ArrayContentProvider());
		tvSQLHistory.setInput(listSQLHistory);
		tvSQLHistory.setSorter(sorterHistory);
		
		Composite compositeRecallBtn = new Composite(this, SWT.NONE);
		compositeRecallBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeRecallBtn = new GridLayout(8, false);
		gl_compositeRecallBtn.marginWidth = 1;
		gl_compositeRecallBtn.marginHeight = 0;
		compositeRecallBtn.setLayout(gl_compositeRecallBtn);
		
		final Button btnExportHistory = new Button(compositeRecallBtn, SWT.NONE);
		btnExportHistory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection is = (IStructuredSelection)tvSQLHistory.getSelection();
				if(!is.isEmpty()) {
					SQLHistoryDAO historyDAO = (SQLHistoryDAO)is.getFirstElement();
					
					appendText(historyDAO.getStrSQLText());
				} else {
					MessageDialog.openInformation(null, Messages.MainEditor_2, Messages.MainEditor_29);
				}
			}
		});
		btnExportHistory.setText(Messages.MainEditor_12);
		
		Button btnDetailView = new Button(compositeRecallBtn, SWT.NONE);
		btnDetailView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection is = (IStructuredSelection)tvSQLHistory.getSelection();
				Object selElement = is.getFirstElement();
				if(selElement instanceof SQLHistoryDAO) {
					SQLHistoryDAO tmd = (SQLHistoryDAO)selElement;
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(null, Messages.MainEditor_11, SQLHistoryLabelProvider.dateToStr(tmd.getStartDateExecute()), tmd.getStrSQLText() );
					dlg.open();
				} else {
					MessageDialog.openInformation(null, Messages.MainEditor_2, Messages.MainEditor_29);
				}
			}
		});
		btnDetailView.setText(Messages.MainEditor_btnDetailView_text);
		
//		Button btnSetEditor = new Button(compositeRecallBtn, SWT.NONE);
//		btnSetEditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		btnSetEditor.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				StringBuffer sbExportData = new StringBuffer();
//				
//				for(SQLHistoryDAO dao : listSQLHistory) {
//					sbExportData.append( dao.getStrSQLText() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
//				}
//				
////				downloadExtFile(getRdbResultComposite().getUserDB().getDisplay_name() + "_RecallSQLExport.txt", sbExportData.toString()); //$NON-NLS-1$
//			}
//		});
//		btnSetEditor.setText(Messages.MainEditor_17);
		
		// table clear
		Button btnHistoyClear = new Button(compositeRecallBtn, SWT.NONE);
		btnHistoyClear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnHistoyClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listSQLHistory.clear();
				tvSQLHistory.refresh();
			}
		});
		btnHistoyClear.setText(Messages.MainEditor_btnClear_text);
		
		Label labelDumyRecal = new Label(compositeRecallBtn, SWT.NONE);
		labelDumyRecal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textHistoryFilter = new Text(compositeRecallBtn, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textHistoryFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) refreshSqlHistory();
			}
		});
		textHistoryFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		// refresh
		Button btnRefresh = new Button(compositeRecallBtn, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshSqlHistory();
			}
		});
		btnRefresh.setText(Messages.MainEditor_24);
	}
	
	/**
	 * set parent composite
	 * 
	 * @param rdbResultComposite
	 */
	public void setRDBResultComposite(ResultMainComposite rdbResultComposite) {
		this.rdbResultComposite = rdbResultComposite;
	}
	public ResultMainComposite getRdbResultComposite() {
		return rdbResultComposite;
	}
	
//	/**
//	 * sql history를 텍스트로 만듭니다.
//	 * @return
//	 * @throws Exception
//	 */
//	private String getHistoryTabelSelectData() {
//		IStructuredSelection is = (IStructuredSelection)tvSQLHistory.getTable().getSelection();
//		SQLHistoryDAO historyDAO = (SQLHistoryDAO)is;
//		StringBuffer sbData = new StringBuffer();
//		
//		for(TableItem ti : tvSQLHistory.getTable().getSelection()) {
//			sbData.append(ti.getText(1));
//		}
//		
//		return sbData.toString();
//	}
	
	/**
	 * 쿼리 후 실행결과를 히스토리화면과 프로파일에 저장합니다.
	 */
	public void afterQueryInit(SQLHistoryDAO sqltHistoryDao) {
		try {
			TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(getRdbResultComposite().getUserSeq(), 
							getRdbResultComposite().getUserDB(), 
							PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDITOR, 
							sqltHistoryDao);
		} catch(Exception e) {
			logger.error("save the user query", e); //$NON-NLS-1$
		}
		
		listSQLHistory.add(sqltHistoryDao);
		tvSQLHistory.refresh();
	}
	
	/**
	 * 해당일에 실행했던 쿼리를 보여줍니다.
	 * 
	 * reference https://github.com/hangum/TadpoleForDBTools/issues/387
	 */
	public void findHistoryData() {
		try {
			listSQLHistory.addAll(TadpoleSystem_ExecutedSQL.getExecuteQueryHistory(
									getRdbResultComposite().getUserSeq(), 
									getRdbResultComposite().getUserDB().getSeq(), 
									textHistoryFilter.getText().trim()));
			tvSQLHistory.refresh();
		} catch(Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * refresh sql history table 
	 */
	private void refreshSqlHistory() {
		listSQLHistory.clear();
		findHistoryData();
	}
	
	private void appendText(String cmd) {
		getRdbResultComposite().appendText(cmd);
	}

	@Override
	protected void checkSubclass() {
	}

}
