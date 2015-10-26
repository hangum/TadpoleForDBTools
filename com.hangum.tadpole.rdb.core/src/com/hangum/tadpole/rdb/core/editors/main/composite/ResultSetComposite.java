/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     billy.goo - add dialog to view detail record
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite;

import java.io.BufferedReader;
import java.io.File;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.commons.dialogs.message.TadpoleImageViewDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.sqls.ParameterUtils;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.commons.utils.zip.util.ZipUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.engine.sql.paremeter.lang.JavaNamedParameterUtil;
import com.hangum.tadpole.engine.sql.paremeter.lang.OracleStyleSQLNamedParameterUtil;
import com.hangum.tadpole.engine.sql.util.CSVUtil;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.global.OpenSingleRowDataDialogAction;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBInfoDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.SQLResultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.main.execute.TransactionManger;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteBatchSQL;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteOtherSQL;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteQueryPlan;
import com.hangum.tadpole.rdb.core.editors.main.parameter.ParameterDialog;
import com.hangum.tadpole.rdb.core.editors.main.parameter.ParameterObject;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.IMainEditorExtension;
import com.hangum.tadpole.rdb.core.util.GrantCheckerUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.tajo.core.connections.manager.ConnectionPoolManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * result set composite
 * 
 * @author hangum
 *
 */
public class ResultSetComposite extends Composite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3706926974815713584L;

	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultSetComposite.class);
	
	/**
	 * 에디터가 select 에디터인지 즉 구분자로 쿼리를 검색하는 상태인지 나타냅니다.
	 */
	private boolean isSelect = true;
	/**
	 * 현재 사용자의 데이터의 궈한타입.
	 */
	private String dbUserRoleType = ""; //$NON-NLS-1$
	
	/** execute job */
	private Job jobQueryManager = null;

	/** 사용자가 요청한 쿼리 */
	private RequestQuery reqQuery = null;
	
	/** result composite */
	private ResultMainComposite rdbResultComposite;
	
	/** 쿼리 호출 후 결과 dao */
	private QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();
	
	private ProgressBar progressBarQuery;
	private Button btnStopQuery;
	
	/** 결과 filter */
	private Text textFilter;
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	private Label lblQueryResultStatus;
	private TableViewer tvQueryResult;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
    /** content download를 위한 더미 composite */
    private Composite compositeDumy;
    
    private OpenSingleRowDataDialogAction openSingleRowDataAction;
    
    /** define result to editor button  */
    private Event eventTableSelect;
    private Button btnResultToEditor;
    
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param isSelect
	 */
	public ResultSetComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite compHead = new Composite(this, SWT.NONE);
		compHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(5, false);
		gl_composite.verticalSpacing = 3;
		gl_composite.horizontalSpacing = 3;
		gl_composite.marginHeight = 3;
		gl_composite.marginWidth = 3;
		compHead.setLayout(gl_composite);
		
		Label lblProgress = new Label(compHead, SWT.NONE);
		lblProgress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProgress.setText(Messages.ResultSetComposite_3);
		
		progressBarQuery = new ProgressBar(compHead, SWT.NULL);
//		progressBarQuery.setBackground(SWTResourceManager.getColor(127,255,0));
		progressBarQuery.setSelection(0);
		
		btnStopQuery = new Button(compHead, SWT.NONE);
		btnStopQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isUserInterrupt = false;
			}
		});
		btnStopQuery.setText(Messages.RDBResultComposite_btnStp_text);
		btnStopQuery.setEnabled(false);
		
		Label lblFilter = new Label(compHead, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.ResultSetComposite_lblFilter_text);
		
		textFilter = new Text(compHead,SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvQueryResult = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		final Table tableResult = tvQueryResult.getTable();

		final String PREFERENCE_USER_FONT = GetPreferenceGeneral.getRDBResultFont();
		if(!"".equals(PREFERENCE_USER_FONT)) { //$NON-NLS-1$
			try {
				String[] arryFontInfo = StringUtils.split(PREFERENCE_USER_FONT, "|"); //$NON-NLS-1$
				tableResult.setFont(ResourceManager.getFont(arryFontInfo[0], Integer.parseInt(arryFontInfo[1]), Integer.parseInt(arryFontInfo[2])));
			} catch(Exception e) {
				logger.error("Fail setting the user font", e); //$NON-NLS-1$
			}
		}
		tableResult.addListener(SWT.MouseDown, new Listener() {
		    public void handleEvent(final Event event) {
		    	TableItem[] selection = tableResult.getSelection();
		    	
				if (selection.length != 1) return;
				eventTableSelect = event;
				
				final TableItem item = tableResult.getSelection()[0];
				for (int i=0; i<tableResult.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						Map<Integer, Object> mapColumns = rsDAO.getDataList().getData().get(tableResult.getSelectionIndex());
						// execute extension start =============================== 
						IMainEditorExtension[] extensions = getRdbResultComposite().getMainEditor().getMainEditorExtions();
						for (IMainEditorExtension iMainEditorExtension : extensions) {
							iMainEditorExtension.resultSetClick(i, mapColumns);
						}
						break;
					}
				}	// for column count
			}
		});
		
		tableResult.setLinesVisible(true);
		tableResult.setHeaderVisible(true);
		tableResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		createResultMenu();
		
		sqlFilter.setTable(tableResult);
		
		// single column select start
		TableUtil.makeSelectSingleColumn(tvQueryResult);
	    // single column select end
		
		tableResult.getVerticalBar().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				caclTableData();
			}
		});
		tableResult.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				caclTableData();
			}
		});
		
		Composite compositeBtn = new Composite(this, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(8, false);
		gl_compositeBtn.marginWidth = 1;
		gl_compositeBtn.marginHeight = 0;
		compositeBtn.setLayout(gl_compositeBtn);
		
		btnResultToEditor = new Button(compositeBtn, SWT.NONE);
		btnResultToEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableColumnDAO columnDao = selectColumnToEditor();
				if(columnDao == null) return;
				
				if(!"".equals(columnDao.getCol_value())) { //$NON-NLS-1$
					if(RDBTypeToJavaTypeUtils.isNumberType(columnDao.getType())) {
						appendTextAtPosition(columnDao.getCol_value());
					} else {
						appendTextAtPosition(String.format(" %s ", columnDao.getCol_value())); //$NON-NLS-1$
					}
				}
			}
		});
		btnResultToEditor.setText(Messages.ResultSetComposite_2);
		
		btnDetailView = new Button(compositeBtn, SWT.NONE);
		GridData gd_btnDetailView = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDetailView.widthHint = 80;
		btnDetailView.setLayoutData(gd_btnDetailView);
		btnDetailView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openSingleRecordViewDialog();
			}
		});
		btnDetailView.setText(Messages.ResultSetComposite_0);
		
		btnColumnDetail = new Button(compositeBtn, SWT.NONE);
		btnColumnDetail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openSinglColumViewDialog();
			}
		});
		btnColumnDetail.setText(Messages.ResultSetComposite_btnColumnDetail_text);
		
		comboDownload = new Combo(compositeBtn, SWT.NONE | SWT.READ_ONLY);
		comboDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		comboDownload.add("CSV"); //$NON-NLS-1$
		comboDownload.add("INSERT INTO statement"); //$NON-NLS-1$
		comboDownload.select(0);
		
		Button btnSQLResultDownload = new Button(compositeBtn, SWT.NONE);
		btnSQLResultDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		btnSQLResultDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(getShell(), Messages.ResultSetComposite_4, Messages.ResultSetComposite_5)) {
					if("CSV".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultCSVType();	
					} else {
						exportInsertIntoStatement();
					}
				}
			}
			
		});
		btnSQLResultDownload.setText(Messages.ResultSetComposite_11);
		
		compositeDumy = new Composite(compositeBtn, SWT.NONE);
		compositeDumy.setLayout(new GridLayout(1, false));
		compositeDumy.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		lblQueryResultStatus = new Label(compositeBtn, SWT.NONE);
		lblQueryResultStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		new Label(compositeBtn, SWT.NONE);
		
		registerServiceHandler();
	}
	
	/**
	 * select table column to editor
	 */
	private TableColumnDAO selectColumnToEditor() {
		if(eventTableSelect == null) return null;
		final Table tableResult = tvQueryResult.getTable();
    	TableItem[] selection = tableResult.getSelection();
		if (selection.length != 1) return null;
		
		TableColumnDAO columnDao = new TableColumnDAO();
		TableItem item = tableResult.getSelection()[0];
		for (int i=0; i<tableResult.getColumnCount(); i++) {
			
			if (item.getBounds(i).contains(eventTableSelect.x, eventTableSelect.y)) {
				Map<Integer, Object> mapColumns = rsDAO.getDataList().getData().get(tableResult.getSelectionIndex());
				// execute extension start =============================== 
				IMainEditorExtension[] extensions = getRdbResultComposite().getMainEditor().getMainEditorExtions();
				for (IMainEditorExtension iMainEditorExtension : extensions) {
					iMainEditorExtension.resultSetDoubleClick(i, mapColumns);
				}
				// execute extension stop ===============================
				
				// 첫번째 컬럼이면 전체 로우의 데이터를 상세하게 뿌려줍니
				if(i == 0) {
					columnDao.setName("_TDB_ALL_");
					columnDao.setType(null);
					
					for (int j=1; j<tableResult.getColumnCount(); j++) {
						Object columnObject = mapColumns.get(j);
						
						if(RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(j))) {
							String strText = ""; //$NON-NLS-1$
							
							// if select value is null can 
							if(columnObject == null) strText = "0"; //$NON-NLS-1$
							else strText = columnObject.toString();
							columnDao.setCol_value(columnDao.getCol_value() + strText + ", ");
						} else {
							String strText = ""; //$NON-NLS-1$
							
							// if select value is null can 
							if(columnObject == null) strText = "''"; //$NON-NLS-1$
							else strText = columnObject.toString();
							columnDao.setCol_value(columnDao.getCol_value() + "'" + strText + "', ");
						}
					}
					columnDao.setCol_value(StringUtils.removeEnd(columnDao.getCol_value(), ", "));

					break;
				} else {
					
					//결과 그리드의 선택된 행에서 마우스 클릭된 셀에 연결된 컬럼 오브젝트를 조회한다.
					Object columnObject = mapColumns.get(i);
					
					String strType = RDBTypeToJavaTypeUtils.getRDBType(rsDAO.getColumnType().get(i));
					columnDao.setName(rsDAO.getColumnName().get(i));
					columnDao.setType(strType);
					
					if(columnObject != null) {
						// 해당컬럼 값이 널이 아니고 clob데이터 인지 확인한다.
						if (columnObject instanceof java.sql.Clob ){
							Clob cl = (Clob) columnObject;
	
							StringBuffer clobContent = new StringBuffer();
							String readBuffer = new String();
	
							// 버퍼를 이용하여 clob컬럼 자료를 읽어서 팝업 화면에 표시한다.
							BufferedReader bufferedReader;
							try {
								bufferedReader = new java.io.BufferedReader(cl.getCharacterStream());
								while ((readBuffer = bufferedReader.readLine())!= null) {
									clobContent.append(readBuffer);
								}
	
								columnDao.setCol_value(clobContent.toString());				
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else if (columnObject instanceof java.sql.Blob ){
							try {
								Blob blob = (Blob) columnObject;
								columnDao = null;
								
								TadpoleImageViewDialog dlg = new TadpoleImageViewDialog(null, tableResult.getColumn(i).getText(), blob.getBinaryStream());
								dlg.open();
	
							} catch (Exception e) {
								logger.error("Blob column echeck", e); //$NON-NLS-1$
							}
		
						}else if (columnObject instanceof byte[] ){// (columnObject.getClass().getCanonicalName().startsWith("byte[]")) ){
							byte[] b = (byte[])columnObject;
							StringBuffer str = new StringBuffer();
							try {
								for (byte buf : b){
									str.append(buf);
								}
								str.append("\n\nHex : " + new BigInteger(str.toString(), 2).toString(16)); //$NON-NLS-1$
								
								columnDao.setCol_value(str.toString());
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else{
							String strText = ""; //$NON-NLS-1$
							
							// if select value is null can 
							if(columnObject == null) strText = ""; //$NON-NLS-1$
							else strText = columnObject.toString();
							
							columnDao.setCol_value(strText);
						}
					} 	// end object null
				}	// end if first column
			
				break;
			}	// for column index
		}	// end for

		return columnDao;
	}
	
	/**
	 * export insert into statement
	 */
	private void exportInsertIntoStatement() {
		try {
			String strTableName = "TempTable"; //$NON-NLS-1$
			if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
			if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
			
			String strPath = SQLUtil.makeFileInsertStatment(strTableName, rsDAO);
			String strZipFile = ZipUtils.pack(strPath);
			byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
			
			downloadExtFile(strTableName+"_SQL.zip", bytesZip); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error("make insertinto", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * Export resultset csvMessages.ResultSetComposite_14
	 * 
	 */
	private void exportResultCSVType() {
		try {
			String strTableName = "TempTable"; //$NON-NLS-1$
			if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
			if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
			
			String strPath = CSVUtil.makeCSVFile(strTableName, rsDAO);
			String strZipFile = ZipUtils.pack(strPath);
			byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
			
			downloadExtFile(strTableName +"_CSV.zip", bytesZip); //$NON-NLS-1$
		} catch(Exception ee) {
			logger.error("csv type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * Open Single recode view.
	 * Just view detail data.
	 */
	private void openSingleRecordViewDialog() {
		// selection sevice를 이용할 수 없어 중복되는 코드 생성이 필요해서 작성.
		openSingleRowDataAction.selectionChanged(rsDAO, tvQueryResult.getSelection());
		if (openSingleRowDataAction.isEnabled()) {
			openSingleRowDataAction.run();
		} else {
			MessageDialog.openWarning(getShell(), Messages.ResultSetComposite_7, Messages.ResultSetComposite_8);
		}
	}
	
	/**
	 * column popup dialog
	 */
	private void openSinglColumViewDialog() {
		TableColumnDAO columnDao = selectColumnToEditor();
		if(columnDao == null) {
			MessageDialog.openWarning(getShell(), Messages.ResultSetComposite_7, Messages.ResultSetComposite_6);
			return;
		}
			
		String strType = columnDao.getType();
		if("JSON".equalsIgnoreCase(strType)) { //$NON-NLS-1$
			TadpoleSimpleMessageDialog dialog = new TadpoleSimpleMessageDialog(getShell(), Messages.ResultSetComposite_16, columnDao.getCol_value());
			dialog.open();
		} else {
			TDBInfoDialog dialog = new TDBInfoDialog(getShell(), Messages.ResultSetComposite_16, columnDao.getCol_value());
			dialog.open();
		}
	}
	
	/**
	 * tvQueryResult 테이블 뷰어에 메뉴 추가하기 
	 */
	private void createResultMenu() {
		openSingleRowDataAction = new OpenSingleRowDataDialogAction();
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "ResultSet"); //$NON-NLS-1$ //$NON-NLS-2$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(openSingleRowDataAction);
			}
		});

		tvQueryResult.getTable().setMenu(menuMgr.createContextMenu(tvQueryResult.getTable()));

		// 
		// 본 Composite는 Editor에서 최초 생성되는데, 에디터가 site()에 등록되지 않은 상태에서
		// selection service에 접근할 수 없어서 임시로 selection 이벤트가 발생할때마다 
		// 직접 action selection 메소드를 호출하도록 수정함.
		// 또한, 쿼리 실행할 때 마다 rsDAO 값도 변경되므로, selectoin이 변경될때 마다 같이
		// 전달해 준다. 
		tvQueryResult.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				openSingleRowDataAction.selectionChanged(rsDAO, event.getSelection());
			}
		});
	}

	/**
	 * scroll data에 맞게 데이터를 출력합니다. 
	 */
	private void caclTableData() {
		final Table tableResult = tvQueryResult.getTable();
		int tableRowCnt = tableResult.getBounds().height / tableResult.getItemHeight();
		// 만약에(테이블 위치 인덱스 + 테이블에 표시된로우 수 + 1) 보다 전체 아이템 수가 크면).
		if( (tableResult.getTopIndex() + tableRowCnt + 1) > tableResult.getItemCount()) { 

			final TadpoleResultSet trs = rsDAO.getDataList();
			if(trs.getData().size() > tableResult.getItemCount()) {
				if(trs.getData().size() > (tableResult.getItemCount() + GetPreferenceGeneral.getPageCount())) {
					tvQueryResult.setInput(trs.getData().subList(0, tableResult.getItemCount() + GetPreferenceGeneral.getPageCount()));
				} else {
					tvQueryResult.setInput(trs.getData());
				}
			}
		}
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
	
	/**
	 * 파라미터 쿼리인지 검사하여 쿼리를 만듭니다.
	 * @return
	 */
	private boolean ifIsParameterQuery() {
		final Shell runShell = textFilter.getShell();
		
		if(reqQuery.getExecuteType() != EditorDefine.EXECUTE_TYPE.ALL) {
			
			final DBDefine selectDBDefine = getUserDB().getDBDefine();
			if(!(selectDBDefine == DBDefine.HIVE_DEFAULT 		|| 
					selectDBDefine == DBDefine.HIVE2_DEFAULT 	|| 
					selectDBDefine == DBDefine.TAJO_DEFAULT 	||
					// not support this java.sql.ParameterMetaData 
					selectDBDefine == DBDefine.CUBRID_DEFAULT)
			) {
				boolean isAlreadyApply = false;
				try {
					// if named parameter?
					OracleStyleSQLNamedParameterUtil oracleNamedParamUtil = OracleStyleSQLNamedParameterUtil.getInstance();
					String strSQL = oracleNamedParamUtil.parse(reqQuery.getSql());
					
					Map<String, int[]> mapIndex = oracleNamedParamUtil.getIndexMap();
					if(!mapIndex.isEmpty()) {
						isAlreadyApply = true;
						
						ParameterDialog epd = new ParameterDialog(runShell, getUserDB(), mapIndex);
						if(Dialog.OK == epd.open()) {
							ParameterObject paramObj = epd.getOracleParameterObject(oracleNamedParamUtil.getMapIndexToName());
							String repSQL = ParameterUtils.fillParameters(strSQL, paramObj.getParameter());
							reqQuery.setSql(repSQL);
							
							if(logger.isDebugEnabled()) logger.debug("[Oracle Type] User parameter query is  " + repSQL); //$NON-NLS-1$
						} else {
							return false;
						}
					}
				} catch(Exception e) {
					logger.error("Oracle sytle parameter parse", e); //$NON-NLS-1$
				}

				if(!isAlreadyApply) {
					try {
						// java named parameter
						JavaNamedParameterUtil javaNamedParameterUtil = new JavaNamedParameterUtil();
						int paramCnt = javaNamedParameterUtil.calcParamCount(getUserDB(), reqQuery.getSql());
						if(paramCnt > 0) {
							ParameterDialog epd = new ParameterDialog(runShell, getUserDB(), paramCnt);
							if(Dialog.OK == epd.open()) {
								ParameterObject paramObj = epd.getParameterObject();
								String repSQL = ParameterUtils.fillParameters(reqQuery.getSql(), paramObj.getParameter());
								reqQuery.setSql(repSQL);
								
								if(logger.isDebugEnabled()) logger.debug("[Java Type]User parameter query is  " + repSQL); //$NON-NLS-1$
							} else {
								return false;
							}
						}
					} catch(Exception e) {
						logger.error("Java style parameter parse", e); //$NON-NLS-1$
					}
				}  // if(!isAlreadyApply
			}
		}	// end if(reqQuery.getExecuteType() != EditorDefine.EXECUTE_TYPE.ALL) {

		return true;
	}
	
	/**
	 * 쿼리를 수행합니다.
	 * 
	 * @param reqQuery
	 */
	public boolean executeCommand(final RequestQuery reqQuery) {
		this.reqQuery = reqQuery;
		
		// 쿼리를 이미 실행 중이라면 무시합니다.
		if(jobQueryManager != null) {
			if(Job.RUNNING == jobQueryManager.getState()) {
				if(logger.isDebugEnabled()) logger.debug("\t\t================= return already running query job "); //$NON-NLS-1$
				executeErrorProgress(reqQuery, new Exception(Messages.ResultSetComposite_1), Messages.ResultSetComposite_1);
				return false;
			}
		}

		// 쿼리가 실행 가능한 상태인지(디비 락상태인지?, 프러덕디비이고 select가 아닌지?,설정인지?) 
		try {
			if(!GrantCheckerUtils.ifExecuteQuery(getUserDB(), reqQuery)) {
				return false;
			}
		} catch(Exception e) {
			executeErrorProgress(reqQuery, e, e.getMessage());
			return false;
		}
		
		// 파라미터 쿼리이라면 파라미터 쿼리 상태로 만듭니다.
		if(!ifIsParameterQuery()) return false;
		
		// 프로그래스 상태와 쿼리 상태를 초기화한다.
		controlProgress(true);
		
//		if(logger.isDebugEnabled()) logger.debug("Start query time ==> " + System.currentTimeMillis() ); //$NON-NLS-1$
		this.rsDAO = new QueryExecuteResultDTO();
		this.sqlFilter.setFilter(""); //$NON-NLS-1$
		this.textFilter.setText(""); //$NON-NLS-1$
		
		// selected first tab request quring.
		rdbResultComposite.resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
		
		// 쿼리를 실행 합니다. 
		final RequestResultDAO reqResultDAO = new RequestResultDAO();
		final int intSelectLimitCnt = GetPreferenceGeneral.getSelectLimitCount();
		final String strPlanTBName 	= GetPreferenceGeneral.getPlanTableName();
		final String strUserEmail 	= SessionManager.getEMAIL();
		final int queryTimeOut 		= GetPreferenceGeneral.getQueryTimeOut();
		final int intCommitCount 	= Integer.parseInt(GetPreferenceGeneral.getRDBCommitCount());
		
		jobQueryManager = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(reqQuery.getSql(), IProgressMonitor.UNKNOWN);
				
				reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
				reqResultDAO.setIpAddress(reqQuery.getUserIp());
				reqResultDAO.setStrSQLText(reqQuery.getOriginalSql());
				
				try {
					
					if(reqQuery.getExecuteType() == EditorDefine.EXECUTE_TYPE.ALL) {
						
						List<String> listStrExecuteQuery = new ArrayList<String>();
						for (String strSQL : reqQuery.getSql().split(PublicTadpoleDefine.SQL_DELIMITER)) {
							String strExeSQL = SQLUtil.sqlExecutable(strSQL);
							
							// execute batch update는 ddl문이 있으면 안되어서 실행할 수 있는 쿼리만 걸러 줍니다.
							if(SQLUtil.isStatement(strExeSQL)) {
								reqQuery.setSql(strExeSQL);											
							} else if(TransactionManger.isStartTransaction(strExeSQL)) {
								startTransactionMode();
								reqQuery.setAutoCommit(false);
							} else {
								listStrExecuteQuery.add(strExeSQL);
							}
						}
						
						// select 이외의 쿼리 실행
						if(!listStrExecuteQuery.isEmpty()) {
							ExecuteBatchSQL.runSQLExecuteBatch(listStrExecuteQuery, reqQuery, getUserDB(), getDbUserRoleType(), intCommitCount, strUserEmail);
						}
						
						// select 문장 실행
						if(SQLUtil.isStatement(reqQuery.getSql())) { //$NON-NLS-1$
							rsDAO = runSelect(queryTimeOut, strUserEmail, intSelectLimitCnt);
							reqResultDAO.setRows(rsDAO.getDataList().getData().size());
						}
					} else {
						
						if(reqQuery.isAutoCommit()) {
							if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
								rsDAO = ExecuteQueryPlan.runSQLExplainPlan(reqQuery, getUserDB(), strPlanTBName);
							} else if(reqQuery.isStatement()) {
								rsDAO = runSelect(queryTimeOut, strUserEmail, intSelectLimitCnt);
								reqResultDAO.setRows(rsDAO.getDataList().getData().size());
							} else {
								ExecuteOtherSQL.runPermissionSQLExecution(reqQuery, getUserDB(), getDbUserRoleType(), strUserEmail);
							}

						} else if(TransactionManger.isTransaction(reqQuery.getSql())) {
							if(TransactionManger.isStartTransaction(reqQuery.getSql())) {
								startTransactionMode();
								reqQuery.setAutoCommit(false);
							} else {
								TransactionManger.calledCommitOrRollback(reqQuery.getSql(), strUserEmail, getUserDB());
							}
						}
					}
					
				} catch(Exception e) {
					
					reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
					reqResultDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage(), e);
				} finally {
					reqResultDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
					monitor.done();
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		jobQueryManager.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				
				getRdbResultComposite().getSite().getShell().
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						// 쿼리가 정상일 경우 결과를 테이블에 출력하고, 히스토리를 남기며, 필요하면 오브젝트익스플로에 리프레쉬한다.
						if(jobEvent.getResult().isOK()) {
							executeFinish(reqQuery, reqResultDAO);
						} else {
							executeErrorProgress(reqQuery, jobEvent.getResult().getException(), jobEvent.getResult().getMessage());
							getRdbResultComposite().getMainEditor().browserEvaluateToStr(EditorFunctionService.SET_SELECTED_TEXT); //$NON-NLS-1$
						}
						
						// 처리를 위해 결과를 담아 둡니다.
						reqQuery.setResultDao(reqResultDAO);
						
						// 히스토리 화면을 갱신합니다.
						getRdbResultComposite().getCompositeQueryHistory().afterQueryInit(reqResultDAO);
						
						// 모든 쿼리가 종료 되었음을 알린다.
						finallyEndExecuteCommand();
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		jobQueryManager.setPriority(Job.INTERACTIVE);
		jobQueryManager.setName(getUserDB().getDisplay_name() + reqQuery.getOriginalSql());
		jobQueryManager.schedule();
		
		return true;
	}
	
	/**
	 * 쿼리 중간에 begin 으로 시작하는 구문이 있어서 트랜잭션을 시작합니다. 
	 */
	private void startTransactionMode() {
		rdbResultComposite.getMainEditor().beginTransaction();
	}
	
	private boolean isCheckRunning = true;
	private boolean isUserInterrupt = false;
	private ExecutorService execServiceQuery = null;
	private ExecutorService esCheckStop = null; 
	private Button btnDetailView;
	private Combo comboDownload;
	private Button btnColumnDetail;
	private QueryExecuteResultDTO runSelect(final int queryTimeOut, final String strUserEmail, final int intSelectLimitCnt) throws Exception {
		if(!PermissionChecker.isExecute(getDbUserRoleType(), getUserDB(), reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// 확장 포인트가 있다면 확장 포인트의 쿼리로 대체합니다.
		IMainEditorExtension[] extensions = getRdbResultComposite().getMainEditor().getMainEditorExtions();
		if(extensions != null) {
			for (IMainEditorExtension iMainEditorExtension : extensions) {
				String strCostumSQL = iMainEditorExtension.sqlCostume(reqQuery.getSql());
				if(!strCostumSQL.equals(reqQuery.getSql())) {
					if(logger.isInfoEnabled()) logger.info("** extension costume sql is : " + strCostumSQL); //$NON-NLS-1$
					reqQuery.setSql(strCostumSQL);
				}
			}
		}
		// 확장 포인트가 있다면 확장 포인트의 쿼리로 대체합니다.
		
		ResultSet resultSet = null;
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(DBDefine.TAJO_DEFAULT == getUserDB().getDBDefine()) {
				javaConn = ConnectionPoolManager.getDataSource(getUserDB()).getConnection();
			} else {
				if(reqQuery.isAutoCommit()) {
					SqlMapClient client = TadpoleSQLManager.getInstance(getUserDB());
					javaConn = client.getDataSource().getConnection();
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(strUserEmail, getUserDB());
				}
			}
			statement = javaConn.createStatement();
			
			statement.setFetchSize(intSelectLimitCnt);
			if(!(getUserDB().getDBDefine() == DBDefine.HIVE_DEFAULT || 
					getUserDB().getDBDefine() == DBDefine.HIVE2_DEFAULT)
			) {
				statement.setQueryTimeout(queryTimeOut);
				statement.setMaxRows(intSelectLimitCnt);
			}
			
			// check stop thread
			esCheckStop = Executors.newSingleThreadExecutor();
			CheckStopThread cst = new CheckStopThread(statement);
			cst.setName("Stop thread checker"); //$NON-NLS-1$
			esCheckStop.execute(cst);
			
			// execute query
			execServiceQuery = Executors.newSingleThreadExecutor();
			resultSet = runSQLSelect(statement, reqQuery);
			
			rsDAO = new QueryExecuteResultDTO(getUserDB(), true, resultSet, intSelectLimitCnt);
		} catch(Exception e) {
//			if(logger.isDebugEnabled()) logger.error("execute query", e); //$NON-NLS-1$
			throw e;
		} finally {
			isCheckRunning = false;
			
			try { if(statement != null) statement.close(); } catch(Exception e) {}
			try { if(resultSet != null) resultSet.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
		
		return rsDAO;
	}
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 */
	private ResultSet runSQLSelect(final Statement statement, final RequestQuery reqQuery) throws Exception {
		
		Future<ResultSet> queryFuture = execServiceQuery.submit(new Callable<ResultSet>() {
			@Override
			public ResultSet call() throws SQLException {
				statement.execute(reqQuery.getSql());
				return statement.getResultSet();
			}
		});
		
		
		/* SELECT ALRM_DATE 와같은 select다음에 한글 모음이 들어갔을때 아래와 같은 에러가 발생한다.
    
		 * Caused by: java.lang.NullPointerException
			at oracle.jdbc.driver.T4C8Oall.getNumRows(T4C8Oall.java:973)
		 */
		return queryFuture.get();
	}

	/**
	 * check stop thread
	 * @author hangum
	 *
	 */
	private class CheckStopThread extends Thread {
		private Statement stmt = null;
		
		public CheckStopThread(Statement stmt) {
			super("CheckStopThread "); //$NON-NLS-1$
			
			this.stmt = stmt;
		}
		
		@Override
		public void run() {
			int i = 0;
			
			try {
				while(isCheckRunning) {
					
					// Is user stop?
					if(!isUserInterrupt) {
						isCheckRunning = false;
						stmt.cancel();
						
						try {
							if(logger.isDebugEnabled()) logger.debug("********* User stop operation is [statement close] " + stmt.isClosed()); //$NON-NLS-1$
							if(!stmt.isClosed()) execServiceQuery.shutdown();
						} catch(Exception ee) {
							logger.error("Execute stop", ee); //$NON-NLS-1$
						}
					}
					
					if(isCheckRunning) {
						if(i>100) i = 0;
						final int progressAdd = i++; 
						btnStopQuery.getDisplay().asyncExec(new Runnable() {
							@Override
							public void run() {
								progressBarQuery.setSelection(progressAdd);
							}
						});
	
						Thread.sleep(20);
					}
				}   // end while
			} catch(Exception e) {
				logger.error("isCheckThread exception", e); //$NON-NLS-1$
				isCheckRunning = false;
			}
		} 	// end run
	}	// end method

	/**
	 * error message 추가한다.
	 * 
	 * @param requestQuery
	 * @param throwable
	 * @param msg
	 */
	public void executeErrorProgress(RequestQuery requestQuery, Throwable throwable, final String msg) {
		getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
		getRdbResultComposite().refreshMessageView(requestQuery, throwable, msg);
	}
	
	private UserDBDAO getUserDB() {
		return getRdbResultComposite().getUserDB();
	}
	
	/**
	 * 에디터를 실행 후에 마지막으로 실행해 주어야 하는 코드.
	 */
	private void finallyEndExecuteCommand() {
		controlProgress(false);
		eventTableSelect = null;
		
		// 확장포인트에 실행결과를 위임합니다. 
		IMainEditorExtension[] extensions = getRdbResultComposite().getMainEditor().getMainEditorExtions();
		if(extensions == null) return;
		for (IMainEditorExtension iMainEditorExtension : extensions) {
			iMainEditorExtension.queryEndedExecute(rsDAO);
		}

		// 주의) 일반적으로는 포커스가 잘 가지만, 
		// progress bar가 열렸을 경우 포커스가 잃어 버리게 되어 포커스를 주어야 합니다.
		getRdbResultComposite().setOrionTextFocus();
	}
	
	/**
	 * control progress 
	 * 
	 * @param isStart
	 */
	private void controlProgress(final boolean isStart) {
		if(isStart) {
			isCheckRunning = true;
			isUserInterrupt = true;
			
			progressBarQuery.setSelection(0);
			
			// HIVE는 CANCLE 기능이 없습니다. 
			if(!(getUserDB().getDBDefine() == DBDefine.HIVE_DEFAULT ||
					getUserDB().getDBDefine() == DBDefine.HIVE2_DEFAULT)
			) {
				btnStopQuery.setEnabled(true);
			}
		} else {
			isCheckRunning = false;
			isUserInterrupt = false;
			
			progressBarQuery.setSelection(100);
			btnStopQuery.setEnabled(false);
		}
	}

	public void setDbUserRoleType(String userRoleType) {
		dbUserRoleType = userRoleType;
	}
	public String getDbUserRoleType() {
		return dbUserRoleType;
	}

	/**
	 * 쿼리 결과를 화면에 출력합니다.
	 * 
	 * @param executingSQLDAO 실행된 마지막 쿼리
	 */
	public void executeFinish(RequestQuery reqQuery, RequestResultDAO executingSQLDAO) {
		if(reqQuery.isStatement()) {

			// table data를 생성한다.
			final TadpoleResultSet trs = rsDAO.getDataList();
			sqlSorter = new SQLResultSorter(-999);
			
			boolean isEditable = true;
			if("".equals(rsDAO.getColumnTableName().get(1))) isEditable = false; //$NON-NLS-1$
			SQLResultLabelProvider.createTableColumn(reqQuery, tvQueryResult, rsDAO, sqlSorter, isEditable);
			
			tvQueryResult.setLabelProvider(new SQLResultLabelProvider(reqQuery.getMode(), GetPreferenceGeneral.getISRDBNumberIsComma(), rsDAO));
			tvQueryResult.setContentProvider(new ArrayContentProvider());
			
			// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
			if(trs.getData().size() > GetPreferenceGeneral.getPageCount()) {
				tvQueryResult.setInput(trs.getData().subList(0, GetPreferenceGeneral.getPageCount()));	
			} else {
				tvQueryResult.setInput(trs.getData());
			}
			tvQueryResult.setSorter(sqlSorter);
			
			// 메시지를 출력합니다.
			float longExecuteTime = (executingSQLDAO.getEndDateExecute().getTime() - executingSQLDAO.getStartDateExecute().getTime()) / 1000f;
			String strResultMsg = ""; //$NON-NLS-1$
			if(trs.isEndOfRead()) {
				strResultMsg = String.format("%s %s (%s %s)", NumberFormatUtils.commaFormat(trs.getData().size()), Messages.MainEditor_33, longExecuteTime, Messages.MainEditor_74); //$NON-NLS-1$
			} else {
				// 데이터가 한계가 넘어 갔습니다.
				String strMsg = String.format(Messages.MainEditor_34, NumberFormatUtils.commaFormat(GetPreferenceGeneral.getSelectLimitCount()));
				strResultMsg = String.format("%s (%s %s)", strMsg, longExecuteTime, Messages.MainEditor_74); //$NON-NLS-1$
			}
			
			tvQueryResult.getTable().setToolTipText(strResultMsg);
			lblQueryResultStatus.setText(strResultMsg);
			lblQueryResultStatus.pack();
			sqlFilter.setTable(tvQueryResult.getTable());
			
			// Pack the columns
			TableUtil.packTable(tvQueryResult.getTable());
			getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
		} else {
			getRdbResultComposite().refreshMessageView(reqQuery, null, Messages.ResultSetComposite_10 + executingSQLDAO.getStrSQLText());
			getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
			
			// working schema_history 에 history 를 남깁니다.
			try {
				TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), getUserDB(),
						"EDITOR", //$NON-NLS-1$
						reqQuery.getSqlDDLType().name(),
						reqQuery.getSqlObjectName(),
						reqQuery.getSql());
			} catch(Exception e) {
				logger.error("save schemahistory", e); //$NON-NLS-1$
			}
		
			refreshExplorerView(getUserDB(), reqQuery);
		}
	}
	
	/**
	 * CREATE, DROP, ALTER 문이 실행되어 ExplorerViewer view를 리프레쉬합니다.
	 * 
	 * @param userDB
	 * @param reqQuery
	 */
	protected void refreshExplorerView(final UserDBDAO userDB, final RequestQuery reqQuery) {
		rdbResultComposite.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
					ev.refreshCurrentTab(userDB, reqQuery);
				} catch (PartInitException e) {
					logger.error("ExplorerView show", e); //$NON-NLS-1$
				}
			}
			
		});
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
	public void downloadExtFile(String fileName, byte[] newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents);
		
		DownloadUtils.provideDownload(getShell(), downloadServiceHandler.getId());
	}
	
	private void appendTextAtPosition(String cmd) {
		getRdbResultComposite().appendTextAtPosition(cmd);
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	@Override
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	}

	/**
	 * 실행중인 쿼리 job을 가져옵니다. 
	 * @return
	 */
	public Job getJobQueryManager() {
		return jobQueryManager;
	}

	@Override
	protected void checkSubclass() {
	}
	
	/**
	 * 에디터의 쿼리 타입을 설정합니다. 
	 * 
	 * @param isSelect
	 */
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
	/**
	 * 에디터의 쿼리 타입을 설정합니다.
	 * 
	 * @return
	 */
	public boolean isSelect() {
		return isSelect;
	}

}