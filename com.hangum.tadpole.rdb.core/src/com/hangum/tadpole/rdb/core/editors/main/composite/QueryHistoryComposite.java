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
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.rap.rwt.RWT;
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
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
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
	
	private GridTableViewer tvSQLHistory;
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
		tvSQLHistory = new GridTableViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.WRAP);
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
		
		Grid tableSQLHistory = tvSQLHistory.getGrid();
		tableSQLHistory.setLinesVisible(true);
		tableSQLHistory.setHeaderVisible(true);
		tableSQLHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		tableSQLHistory.setSortDirection(SWT.DOWN);
		
		// auto column layout
//		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tvSQLHistory.getTable());
//		tvSQLHistory.getTable().setLayout(layoutColumnLayout);
		
		SQLHistorySorter sorterHistory = new SQLHistorySorter();
		createTableHistoryColumn(tvSQLHistory, sorterHistory);
		
		tvSQLHistory.setLabelProvider(new SQLHistoryLabelProvider());
		tvSQLHistory.setContentProvider(new ArrayContentProvider());
		tvSQLHistory.setAutoPreferredHeight(true);
		tvSQLHistory.setInput(listSQLHistory);
//		tvSQLHistory.setSorter(sorterHistory);
		tvSQLHistory.getGrid().setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		
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
	
	/**
	 * history column
	 * 
	 * @param tv
	 * @param sorterHistory
	 * @param layoutColumnLayout
	 */
	private void createTableHistoryColumn(GridTableViewer tv, SQLHistorySorter sorterHistory) {
		// time
		GridColumn tvcDate = new GridColumn(tv.getGrid(), SWT.NONE);
		tvcDate.setWidth(155);
		tvcDate.setText("Date");
//		tvcDate.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tvcDate, 0));
////		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//		
		// sql
		GridColumn tvcSQL = new GridColumn(tv.getGrid(), SWT.NONE);
//		TableColumn tblclmnSql = tvcSQL.getColumn();
		tvcSQL.setWidth(300);
		tvcSQL.setText("SQL");
		tvcSQL.setWordWrap(true);
//		tblclmnSql.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnSql, 1));
////		layoutColumnLayout.addColumnData(new ColumnPixelData(300));
//		
		// duration
		GridColumn tvcDuration = new GridColumn(tv.getGrid(), SWT.RIGHT);
//		TableColumn tblclmnDuration = tvcDuration.getColumn();
		tvcDuration.setWidth(60);
		tvcDuration.setText("Sec");
//		tblclmnDuration.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDuration, 2));
////		layoutColumnLayout.addColumnData(new ColumnPixelData(50));
//		
		// rows
		GridColumn tvcRows = new GridColumn(tv.getGrid(), SWT.RIGHT);
//		TableColumn tblclmnRows = tvcRows.getColumn();
		tvcRows.setWidth(60);
		tvcRows.setText("Rows");
//		tblclmnRows.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnRows, 3));
////		layoutColumnLayout.addColumnData(new ColumnPixelData(50));
		
		// result
		GridColumn tvcResult = new GridColumn(tv.getGrid(), SWT.NONE);
//		TableColumn tblclmnResult = tvcResult.getColumn();
		tvcResult.setWidth(90);
		tvcResult.setText("Result");
//		tblclmnResult.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnResult, 4));
////		layoutColumnLayout.addColumnData(new ColumnPixelData(40));
//		
		GridColumn tvcMessage = new GridColumn(tv.getGrid(), SWT.NONE);
//		TableColumn tblclmnMessage = tvcMessage.getColumn();
		tvcMessage.setWidth(250);
		tvcMessage.setText("Message");
//		tblclmnMessage.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnMessage, 5));
////		layoutColumnLayout.addColumnData(new ColumnPixelData(80));
//		
//		if (!isQueryHistoryTrack) {
//			return;
//		}
//	
//		// User name 
//		TableViewerColumn tvcUser = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnUser = tvcUser.getColumn();
//		tblclmnUser.setWidth(200);
//		tblclmnUser.setText(Messages.SQLHistoryCreateColumn_6);
//		tblclmnUser.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnUser, 6));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//		
//		// Database
//		TableViewerColumn tvcDatabase = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnDatabase = tvcDatabase.getColumn();
//		tblclmnDatabase.setWidth(250);
//		tblclmnDatabase.setText(Messages.SQLHistoryCreateColumn_7);
//		tblclmnDatabase.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDatabase, 7));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//		
//		// ip
//		TableViewerColumn tvcIp = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnIp = tvcIp.getColumn();
//		tblclmnIp.setWidth(250);
//		tblclmnIp.setText(Messages.SQLHistoryCreateColumn_8);
//		tblclmnIp.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnIp, 8));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
	}
	
//	protected void calculateHeight() {
//		for (GridItem item : tvSQLHistory.getGrid().getItems()) {
//			GC gc = new GC(item.getDisplay());
//			GridColumn gridColumn = tvSQLHistory.getGrid().getColumn(1);
//			Point textBounds = gridColumn.getCellRenderer().computeSize(gc, gridColumn.getWidth(), SWT.DEFAULT, item);
//			gc.dispose();
//			item.setHeight(textBounds.y);
//		}
//	}
	
//	/**
//	 * tablecolumn adapter
//	 * @param viewer
//	 * @param comparator
//	 * @param column
//	 * @param index sort index
//	 * @return
//	 */
//	private static SelectionAdapter getSelectionAdapter(final GridTableViewer viewer, final DefaultViewerSorter comparator, final GridColumn column, final int index) {
//		SelectionAdapter selectionAdapter = new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				comparator.setColumn(index);
//				
//				viewer.getGrid().setSortDirection(comparator.getDirection());
//				viewer.getGrid().setSortColumn(column);
//				viewer.refresh();
//			}
//		};
//		return selectionAdapter;
//	}

}
