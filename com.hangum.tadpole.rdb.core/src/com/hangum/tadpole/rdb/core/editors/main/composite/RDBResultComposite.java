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

import java.io.BufferedReader;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleImageViewDialog;
import com.hangum.tadpole.commons.dialogs.message.TadpoleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.SQLDefine;
import com.hangum.tadpole.rdb.core.editors.main.dialogs.QueryResultSQLDialog;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.sql.util.ResultSetUtilDTO;
import com.hangum.tadpole.sql.util.SQLUtil;
import com.hangum.tadpole.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.sql.util.tables.SQLHistoryCreateColumn;
import com.hangum.tadpole.sql.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLHistorySorter;
import com.hangum.tadpole.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.swtdesigner.SWTResourceManager;

/**
 * RDB Result Composite
 * 
 * @author hangum
 *
 */
public class RDBResultComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(RDBResultComposite.class);
	
	private MainEditor mainEditor = null;
	
	/** 사용자가 요청한 쿼리 */
	private RequestQuery reqQuery = null;
	
	/** 쿼리 호출 후 결과 dao */
	private ResultSetUtilDTO rsDAO = new ResultSetUtilDTO();
	
	/** query 결과 창 */
	private CTabFolder tabFolderResult;

	/** 쿼리 결과 페이지 로케이션 */
	private int pageNumber = 1;
	
	/** 이전, 이후 버튼 */
	private Button btnPrev, btnNext;
	
	/** 결과 filter */
	private Text textFilter;
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	private Label lblQueryResultStatus;
	private TableViewer tvQueryResult;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	/** 쿼리결과 export */
	private Button btnSQLResultExport;
    /** content download를 위한 더미 composite */
    private Composite compositeDumy;
	
	/** query history */
	private TableViewer tvSQLHistory;
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	private Text textHistoryFilter;
	
	/** tadpole message */
	private TableViewer tableViewerMessage;
	private List<TadpoleMessageDAO> listMessage = new ArrayList<TadpoleMessageDAO>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RDBResultComposite(Composite ssahFormComposite, int style) {
		super(ssahFormComposite, style);
		
		tabFolderResult = new CTabFolder(ssahFormComposite, SWT.NONE);
		tabFolderResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolderResult.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());		
		
		// tab 의 index를 설정한다.
		tabFolderResult.setData(EditorDefine.RESULT_TAB.RESULT_SET.toString(), 		0);
		tabFolderResult.setData(EditorDefine.RESULT_TAB.SQL_RECALL.toString(), 		1);
		tabFolderResult.setData(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE.toString(), 2);
		
		CTabItem tbtmResult = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmResult.setText(Messages.MainEditor_7);
		
		Composite compositeQueryResult = new Composite(tabFolderResult, SWT.NONE);
		tbtmResult.setControl(compositeQueryResult);
		
		GridLayout gl_compositeQueryResultBtn = new GridLayout(1, false);
		gl_compositeQueryResultBtn.marginWidth = 1;
		gl_compositeQueryResultBtn.marginHeight = 0;
		compositeQueryResult.setLayout(gl_compositeQueryResultBtn);
		
		textFilter = new Text(compositeQueryResult,SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvQueryResult = new TableViewer(compositeQueryResult, SWT.BORDER | SWT.FULL_SELECTION);
		tvQueryResult.setUseHashlookup(true);
		final Table tableResult = tvQueryResult.getTable();
		tableResult.addListener(SWT.MouseDoubleClick, new Listener() {
		    public void handleEvent(Event event) {
		    	TableItem[] selection = tableResult.getSelection();
				if (selection.length != 1) return;
				
				TableItem item = tableResult.getSelection()[0];
				for (int i=0; i<tableResult.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						String strText = item.getText(i);
						if(strText == null || "".equals(strText)) return; //$NON-NLS-1$
						strText = RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(i))? (" " + strText + ""): (" '" + strText + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						//결과 그리드의 선택된 행에서 마우스 클릭된 셀에 연결된 컬럼 오브젝트를 조회한다.
						Map<Integer, Object> mapColumns = rsDAO.getDataList().get(tableResult.getSelectionIndex());
						Object columnObject = mapColumns.get(i);
						
						// 해당컬럼 값이 널이 아니고 clob데이터 인지 확인한다.
						//if (columnObject != null && columnObject instanceof net.sourceforge.jtds.jdbc.ClobImpl ){
						if (columnObject != null && columnObject instanceof java.sql.Clob ){
							Clob cl = (Clob) columnObject;
	
							StringBuffer clobContent = new StringBuffer();
							String readBuffer = new String();
	
							// 버퍼를 이용하여 clob컬럼 자료를 읽어서 팝업 화면에 표시한다.
							BufferedReader bufferedReader;
							try {
								bufferedReader = new java.io.BufferedReader(cl.getCharacterStream());
								
								while ((readBuffer = bufferedReader.readLine())!= null)
									clobContent.append(readBuffer);

								TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(null, tableResult.getColumn(i).getText(), clobContent.toString());
					            dlg.open();									
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else if (columnObject != null && columnObject instanceof java.sql.Blob ){
							try {
								Blob blob = (Blob) columnObject;
								
								TadpoleImageViewDialog dlg = new TadpoleImageViewDialog(null, tableResult.getColumn(i).getText(), blob.getBinaryStream());
								dlg.open();								
							} catch (Exception e) {
								logger.error("Blob column echeck", e); //$NON-NLS-1$
							}
		
						}else if (columnObject != null && columnObject instanceof byte[] ){// (columnObject.getClass().getCanonicalName().startsWith("byte[]")) ){
							byte[] b = (byte[])columnObject;
							StringBuffer str = new StringBuffer();
							try {
								for (byte buf : b){
									str.append(buf);
								}
								str.append("\n\nHex : " + new BigInteger(str.toString(), 2).toString(16)); //$NON-NLS-1$
								TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(null, tableResult.getColumn(i).getText(), str.toString() );
				                dlg.open();
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else{
							appendTextAtPosition(strText);
//							if(logger.isDebugEnabled()) logger.debug("\nColumn object type is" + columnObject.getClass().toString()); //$NON-NLS-1$
						}
					}
				}
		    }
		});
		
		tableResult.setLinesVisible(true);
		tableResult.setHeaderVisible(true);
		tableResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		sqlFilter.setTable(tableResult);
		
		// single column select start
		TableUtil.makeSelectSingleColumn(tvQueryResult);
	    // single column select end
		
		Composite compositeBtn = new Composite(compositeQueryResult, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(6, false);
		gl_compositeBtn.marginWidth = 1;
		gl_compositeBtn.marginHeight = 0;
		compositeBtn.setLayout(gl_compositeBtn);
		
		btnPrev = new Button(compositeBtn, SWT.NONE);
		btnPrev.setEnabled(false);
		btnPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnPrev();
			}
		});
		btnPrev.setText(Messages.MainEditor_8);
		
		btnNext = new Button(compositeBtn, SWT.NONE);
		btnNext.setEnabled(false);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnNext();
			}
		});
		btnNext.setText(Messages.MainEditor_9);
		
		compositeDumy = new Composite(compositeBtn, SWT.NONE);
		compositeDumy.setLayout(new GridLayout(1, false));
		compositeDumy.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnSQLResultExport = new Button(compositeBtn, SWT.NONE);
		btnSQLResultExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 분리자 정보를 가져옵니다.
				StringBuffer sbExportData = new StringBuffer();
					
				// column 헤더추가.
				TableColumn[] tcs = tvQueryResult.getTable().getColumns();
				for (TableColumn tableColumn : tcs) {
					sbExportData.append( tableColumn.getText()).append(SQLDefine.EXPORT_DEMILITER);
				}
				sbExportData.append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				
				// column 데이터 추가 
				for(int i=0; i<rsDAO.getDataList().size(); i++) {
					Map<Integer, Object> mapColumns = rsDAO.getDataList().get(i);
					for(int j=0; j<mapColumns.size(); j++) {
						String strContent = mapColumns.get(j)==null?"":mapColumns.get(j).toString(); //$NON-NLS-1$
						if(strContent.length() == 0 ) strContent = " "; //$NON-NLS-1$
						sbExportData.append(strContent).append(SQLDefine.EXPORT_DEMILITER); //$NON-NLS-1$
					}
					sbExportData.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				
				downloadExtFile(getUserDB().getDisplay_name() + "_SQLResultExport.csv", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnSQLResultExport.setText(Messages.MainEditor_btnExport_text);
		
		Button btnSql = new Button(compositeBtn, SWT.NONE);
		btnSql.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				queryDialogOpen();
			}
		});
		btnSql.setText(Messages.RDBResultComposite_btnSql_text);
		
		lblQueryResultStatus = new Label(compositeBtn, SWT.NONE);
		lblQueryResultStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));

		///////////////////// tab item //////////////////////////
		CTabItem tbtmNewItem = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmNewItem.setText(Messages.MainEditor_10);
		
		Composite compositeSQLHistory = new Composite(tabFolderResult, SWT.NONE);
		tbtmNewItem.setControl(compositeSQLHistory);
		GridLayout gl_compositeSQLHistory = new GridLayout(1, false);
		gl_compositeSQLHistory.marginHeight = 0;
		gl_compositeSQLHistory.marginWidth = 1;
		gl_compositeSQLHistory.marginBottom = 0;
		compositeSQLHistory.setLayout(gl_compositeSQLHistory);
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvSQLHistory = new TableViewer(compositeSQLHistory, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tvSQLHistory.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				if(!is.isEmpty()) {
					appendText(getHistoryTabelSelectData() + PublicTadpoleDefine.SQL_DILIMITER);
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
		
		Composite compositeRecallBtn = new Composite(compositeSQLHistory, SWT.NONE);
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
					appendText(getHistoryTabelSelectData() + PublicTadpoleDefine.SQL_DILIMITER);
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
		
		Button btnSetEditor = new Button(compositeRecallBtn, SWT.NONE);
		btnSetEditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSetEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(SQLHistoryDAO dao : listSQLHistory) {
					sbExportData.append( dao.getStrSQLText() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadExtFile(getUserDB().getDisplay_name() + "_RecallSQLExport.txt", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnSetEditor.setText(Messages.MainEditor_17);
		
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
		new Label(compositeRecallBtn, SWT.NONE);
		
		///////////////////// tab Message //////////////////////////
		CTabItem tbtmMessage = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmMessage.setText(Messages.MainEditor_0);
		
		Composite compositeMessage = new Composite(tabFolderResult, SWT.NONE);
		tbtmMessage.setControl(compositeMessage);
		GridLayout gl_compositeMessage = new GridLayout(1, false);
		gl_compositeMessage.marginWidth = 1;
		gl_compositeMessage.marginHeight = 0;
		compositeMessage.setLayout(gl_compositeMessage);
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableViewerMessage = new TableViewer(compositeMessage, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewerMessage.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object selElement = is.getFirstElement();
				if(selElement instanceof TadpoleMessageDAO) {
					TadpoleMessageDAO tmd = (TadpoleMessageDAO)selElement;
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(null, Messages.MainEditor_20, SQLHistoryLabelProvider.dateToStr(tmd.getDateExecute()), tmd.getStrMessage());
					dlg.open();
				}
			}
		});
		Table tableMessage = tableViewerMessage.getTable();
		tableMessage.setLinesVisible(true);
		tableMessage.setHeaderVisible(true);
		tableMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableMessage.setSortDirection(SWT.DOWN);
		
		// auto column layout
		AutoResizeTableLayout layoutColumnLayoutMsg = new AutoResizeTableLayout(tableViewerMessage.getTable());
		tableViewerMessage.getTable().setLayout(layoutColumnLayoutMsg);
		
		SQLHistorySorter sorterMessage = new SQLHistorySorter();
		MainEditorHelper.createTableMessageColumn(tableViewerMessage, sorterMessage, layoutColumnLayoutMsg);
		
		tableViewerMessage.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerMessage.setContentProvider(new ArrayContentProvider());
		tableViewerMessage.setInput(listMessage);
		tableViewerMessage.setComparator(sorterMessage);
		
		Composite compositeMessageSub = new Composite(compositeMessage, SWT.NONE);
		compositeMessageSub.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		GridLayout gl_compositeMessageSubBtn = new GridLayout(3, false);
		gl_compositeMessageSubBtn.marginWidth = 1;
		gl_compositeMessageSubBtn.marginHeight = 0;
		compositeMessageSub.setLayout(gl_compositeMessageSubBtn);
		
		Button btnExportMessage = new Button(compositeMessageSub, SWT.NONE);
		btnExportMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportMessage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(TadpoleMessageDAO dao : listMessage) {
					sbExportData.append( dao.getStrMessage() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadExtFile(getUserDB().getDisplay_name() + "_Message.txt", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnExportMessage.setText(Messages.MainEditor_43);
		
		Label labelMsgDumy = new Label(compositeMessageSub, SWT.NONE);
		labelMsgDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnMessageViewClear = new Button(compositeMessageSub, SWT.NONE);
		btnMessageViewClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listMessage.clear();
				tableViewerMessage.refresh();
			}
		});
		btnMessageViewClear.setText(Messages.MainEditor_btnClear_text);
		
		tabFolderResult.setSelection(0);
		registerServiceHandler();
	}
	
	public void setMainEditor(MainEditor mainEditor) {
		this.mainEditor = mainEditor;
	}
	
	private void appendText(String cmd) {
		mainEditor.appendText(cmd);
	}
	private void appendTextAtPosition(String cmd) {
		mainEditor.appendTextAtPosition(cmd);
	}
	
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/**
	 * sql history를 텍스트로 만듭니다.
	 * @return
	 * @throws Exception
	 */
	private String getHistoryTabelSelectData() {
		StringBuffer sbData = new StringBuffer();
		
		for(TableItem ti : tvSQLHistory.getTable().getSelection()) {
			sbData.append(ti.getText(1));
		}
		
		return sbData.toString();
	}
	
	/**
	 * error message 추가한다.
	 * @param msg
	 */
	public void executeErrorProgress(final RequestQuery reqQuery, final String msg) {
		this.reqQuery = reqQuery;
		resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
		
		listMessage.add(new TadpoleMessageDAO(new Date(), msg));
		tableViewerMessage.refresh();
	}
	
	/**
	 * tab을 선택합니다.
	 * 
	 * @param selectTab
	 */
	public void resultFolderSel(final EditorDefine.RESULT_TAB selectTab) {
		int index = (Integer)tabFolderResult.getData(selectTab.toString());
		
		if(tabFolderResult.getSelectionIndex() != index) {
			tabFolderResult.setSelection(index);
		}
	}
	
	/**
	 * refresh sql history table 
	 */
	private void refreshSqlHistory() {
		try {
			listSQLHistory.clear();
			listSQLHistory.addAll( TadpoleSystem_ExecutedSQL.getExecuteQueryHistory(mainEditor.getUserSeq(), getUserDB().getSeq(), textHistoryFilter.getText().trim()) );
			tvSQLHistory.refresh();
		} catch(Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * 쿼리의 결과를 화면에 출력하거나 정리 합니다.
	 */
	private void setResultTable(SQLHistoryDAO executingSQLDAO) {
		if(SQLUtil.isStatement(executingSQLDAO.getStrSQLText())) {			
			// table data를 생성한다.
			sqlSorter = new SQLResultSorter(-999);
			
			SQLResultLabelProvider.createTableColumn(tvQueryResult, rsDAO, sqlSorter);
			tvQueryResult.setLabelProvider(new SQLResultLabelProvider());
			tvQueryResult.setContentProvider(new SQLResultContentProvider(rsDAO.getDataList()));
			
			// 쿼리 결과를 사용자가 설정 한 만큼 보여준다. 
			List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
			int readCount = (rsDAO.getDataList().size()+1) - mainEditor.getQueryPageCount();
			if(readCount < -1) readCount = rsDAO.getDataList().size();
			else if(readCount > mainEditor.getQueryPageCount()) readCount = mainEditor.getQueryPageCount();
			
//			if(logger.isDebugEnabled()) {
//				logger.debug("====[first][start]================================================================="); //$NON-NLS-1$
//				logger.debug("[total count]" + rsDAO.getDataList().size() + "[first][readCount]" + readCount); //$NON-NLS-1$ //$NON-NLS-2$
//				logger.debug("====[first][stop]================================================================="); //$NON-NLS-1$
//			}
				
			for(int i=0; i<readCount; i++) {
				showList.add(rsDAO.getDataList().get(i));
			}
			
			// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
			tvQueryResult.setInput(showList);
			tvQueryResult.setSorter(sqlSorter);
			
			// 메시지를 출력합니다.
			float longExecuteTime = (executingSQLDAO.getEndDateExecute().getTime() - executingSQLDAO.getStartDateExecute().getTime()) / 1000f;
			String strResultMsg = String.format("%s %s (%s%s)", rsDAO.getDataList().size(), Messages.MainEditor_33, longExecuteTime, Messages.MainEditor_74); //$NON-NLS-1$
			tvQueryResult.getTable().setToolTipText(strResultMsg);
			lblQueryResultStatus.setText(strResultMsg);
			sqlFilter.setTable(tvQueryResult.getTable());
			
			// Pack the columns
			TableUtil.packTable(tvQueryResult.getTable());
			resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
		} else {
			listMessage.add(new TadpoleMessageDAO(new Date(), "success. \n\n" + executingSQLDAO.getStrSQLText())); //$NON-NLS-1$
			tableViewerMessage.refresh(listMessage);
			resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
		}
	}
	
	/**
	 * 1) 마지막 쿼리를 받아서 selct 문일 경우 쿼리 네비게이션을 확성화 해준다.
	 * 2) filter를 설정한다.
	 * 
	 * @param executingSQLDAO 실행된 마지막 쿼리
	 */
	public void executeFinish(final RequestQuery reqQuery, SQLHistoryDAO executingSQLDAO, ResultSetUtilDTO rsDAO) {
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
		setFilter();
		
		if(SQLUtil.isStatement(executingSQLDAO.getStrSQLText())) {			
			btnPrev.setEnabled(false);
			if( rsDAO.getDataList().size() < mainEditor.getQueryPageCount() ) btnNext.setEnabled(false);
			else btnNext.setEnabled(true);
		} else {
			btnPrev.setEnabled(false);
			btnNext.setEnabled(false);
		}
		
		// 쿼리의 결과를 화면에 출력합니다.
		setResultTable(executingSQLDAO);
	}
	
	/**
	 * 다음 버튼 처리
	 * 
	 * pageLocation
	 * 
	 */
	private void btnNext() {
		// table data를 생성한다.
		sqlSorter = new SQLResultSorter(-999);
		
		List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
		
		// 쿼리 결과를 사용자가 설정 한 만큼 보여준다.
		int startCount 	= mainEditor.getQueryPageCount() * pageNumber;
		int endCount 	= mainEditor.getQueryPageCount() * (pageNumber+1);
		if(logger.isDebugEnabled()) logger.debug("btnNext ======> [start point]" + startCount + "\t [endCount]" + endCount); //$NON-NLS-1$ //$NON-NLS-2$
		
		//  
		if(endCount >= (rsDAO.getDataList().size()+1)) {
			endCount = rsDAO.getDataList().size();
			
			// 다음 버튼을 비활성화 한다.
			btnNext.setEnabled(false);
		}
		
		// 데이터 출력.
		for(int i=startCount; i<endCount; i++) {
			showList.add(rsDAO.getDataList().get(i));
		}
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		
		tvQueryResult.setInput(showList);
		tvQueryResult.setSorter(sqlSorter);
		
		// Pack the columns
		TableUtil.packTable(tvQueryResult.getTable());
		
		// page 번호를 하나 추가한다.
		pageNumber++;
		if(!btnPrev.getEnabled()) btnPrev.setEnabled(true);
	}
	
	/**
	 * 이전 버튼 처리
	 */
	private void btnPrev() {
		// table data를 생성한다.
		sqlSorter = new SQLResultSorter(-999);		
		List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
		
		// 쿼리 결과를 사용자가 설정 한 만큼 보여준다.
		int startCount 	= mainEditor.getQueryPageCount() * (pageNumber-2);
		int endCount 	= mainEditor.getQueryPageCount() * (pageNumber-1);
		if(logger.isDebugEnabled()) logger.debug("btnPrev ======> [start point]" + startCount + "\t [endCount]" + endCount); //$NON-NLS-1$ //$NON-NLS-2$
		
		if(startCount <= 0) {
			startCount = 0;
			endCount = mainEditor.getQueryPageCount();
			
			btnPrev.setEnabled(false);
		}
		
		// 데이터 출력.
		for(int i=startCount; i<endCount; i++) {
			showList.add(rsDAO.getDataList().get(i));
		}
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		
		tvQueryResult.setInput(showList);
		tvQueryResult.setSorter(sqlSorter);
		
		// Pack the columns
		TableUtil.packTable(tvQueryResult.getTable());
		
		// page 번호를 하나 추가한다.
		pageNumber--;
		if(!btnNext.getEnabled()) btnNext.setEnabled(true);
	}
	
	/**
	 * 결과 테이블을 초기화 상태로 만듭니다.
	 */
	public void resultTableInit() {
		tvQueryResult.setLabelProvider( new SQLResultLabelProvider() );
		tvQueryResult.setContentProvider(new SQLResultContentProvider(null) );
		tvQueryResult.setInput(null);			
		lblQueryResultStatus.setText(Messages.MainEditor_28 );
	}
	
	/**
	 * 필터를 설정합니다.
	 */
	private void setFilter() {
		sqlFilter.setFilter(textFilter.getText());
		tvQueryResult.addFilter( sqlFilter );
	}
	

	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}

	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	public void downloadExtFile(String fileName, String newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents.getBytes());
		
		DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
	}
	
	public UserDBDAO getUserDB() {
		return mainEditor.getUserDB();
	}
	
	@Override
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	}
	
	public List<SQLHistoryDAO> getListSQLHistory() {
		return listSQLHistory;
	}
	
	public TableViewer getTvSQLHistory() {
		return tvSQLHistory;
	}
	
	/**
	 * 사용자가 호출한 쿼리 결과를 다이얼로그에 표시합니다.
	 */
	private void queryDialogOpen() {
		QueryResultSQLDialog qrsDialog = new QueryResultSQLDialog(null, reqQuery);
		qrsDialog.open();
	}

	@Override
	protected void checkSubclass() {
	}
}
