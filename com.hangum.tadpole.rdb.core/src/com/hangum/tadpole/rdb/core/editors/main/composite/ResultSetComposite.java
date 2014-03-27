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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.commons.dialogs.message.TadpoleImageViewDialog;
import com.hangum.tadpole.commons.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.execute.TransactionManger;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteBatchSQL;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteOtherSQL;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteQueryPlan;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteSelect;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.utils.UserPreference;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.sql.util.SQLUtil;
import com.hangum.tadpole.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.hangum.tadpole.tajo.core.connections.manager.ConnectionPoolManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.SWTResourceManager;

/**
 * result set composite
 * 
 * @author hangum
 *
 */
public class ResultSetComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultSetComposite.class);
	private UserPreference easyPreferenceData = new UserPreference();
	
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
	/** 쿼리결과 export */
	private Button btnSQLResultExport;
    /** content download를 위한 더미 composite */
    private Composite compositeDumy;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ResultSetComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.verticalSpacing = 1;
		gl_composite.horizontalSpacing = 1;
		gl_composite.marginHeight = 1;
		gl_composite.marginWidth = 1;
		composite.setLayout(gl_composite);
		
		textFilter = new Text(composite,SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		progressBarQuery = new ProgressBar(composite, SWT.SMOOTH);
		progressBarQuery.setSelection(0);
		
		btnStopQuery = new Button(composite, SWT.NONE);
		btnStopQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isRunning = false;
			}
		});
		btnStopQuery.setText(Messages.RDBResultComposite_btnStp_text);
//		btnStopQuery.setEnabled(false);
		
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvQueryResult = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		final Table tableResult = tvQueryResult.getTable();
		tableResult.addListener(SWT.MouseDoubleClick, new Listener() {
		    public void handleEvent(Event event) {
		    	TableItem[] selection = tableResult.getSelection();
				if (selection.length != 1) return;
				
				TableItem item = tableResult.getSelection()[0];
				for (int i=0; i<tableResult.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						// 첫번째 컬럼이면 전체 로우의 데이터를 상세하게 뿌려줍니
						if(i == 0) {
							
						} else {
							String strText = item.getText(i);
							if(strText == null || "".equals(strText)) return; //$NON-NLS-1$
							strText = RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(i))? (" " + strText + ""): (" '" + strText + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							
							//결과 그리드의 선택된 행에서 마우스 클릭된 셀에 연결된 컬럼 오브젝트를 조회한다.
							Map<Integer, Object> mapColumns = rsDAO.getDataList().getData().get(tableResult.getSelectionIndex());
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
							}
						}	// end if first column
					}	// for column index
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
		
		Composite compositeBtn = new Composite(this, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(6, false);
		gl_compositeBtn.marginWidth = 1;
		gl_compositeBtn.marginHeight = 0;
		compositeBtn.setLayout(gl_compositeBtn);
		
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
					sbExportData.append( tableColumn.getText()).append(UserPreference.EXPORT_DEMILITER);
				}
				sbExportData.append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				
				// column 데이터 추가
				List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
				for(int i=0; i<dataList.size(); i++) {
					Map<Integer, Object> mapColumns = dataList.get(i);
					for(int j=0; j<mapColumns.size(); j++) {
						String strContent = mapColumns.get(j)==null?"":mapColumns.get(j).toString(); //$NON-NLS-1$
						if(strContent.length() == 0 ) strContent = " "; //$NON-NLS-1$
						sbExportData.append(strContent).append(UserPreference.EXPORT_DEMILITER); //$NON-NLS-1$
					}
					sbExportData.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				
				downloadExtFile(getUserDB().getDisplay_name() + "_SQLResultExport.csv", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnSQLResultExport.setText(Messages.MainEditor_btnExport_text);
		
		lblQueryResultStatus = new Label(compositeBtn, SWT.NONE);
		lblQueryResultStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		new Label(compositeBtn, SWT.NONE);
		new Label(compositeBtn, SWT.NONE);
		new Label(compositeBtn, SWT.NONE);
		
		registerServiceHandler();
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
	 * 쿼리를 수행합니다.
	 */
	public void executeCommand(final RequestQuery reqQuery) {
		this.reqQuery = reqQuery; 
		this.rsDAO = new QueryExecuteResultDTO();
		sqlFilter.setFilter("");
		textFilter.setText("");
		
		setControlProgress(true);

		// 쿼리를 실행 합니다. 
		final SQLHistoryDAO sqlHistoryDAO = new SQLHistoryDAO();
		jobQueryManager = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(reqQuery.getSql(), IProgressMonitor.UNKNOWN);
				
				sqlHistoryDAO.setStartDateExecute(new Date());
				sqlHistoryDAO.setIpAddress(reqQuery.getUserIp());
				sqlHistoryDAO.setStrSQLText(reqQuery.getOriginalSql());
				
				try {
						
					if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {
						
						List<String> listStrExecuteQuery = new ArrayList<String>();
						for (String strSQL : reqQuery.getSql().split(PublicTadpoleDefine.SQL_DILIMITER)) {
							String strExeSQL = SQLUtil.sqlExecutable(strSQL);
							
							// execute batch update는 ddl문이 있으면 안되어서 실행할 수 있는 쿼리만 걸러 줍니다.
							if(!SQLUtil.isStatement(strExeSQL)) {
								listStrExecuteQuery.add(strExeSQL);
							} else {
								reqQuery.setSql(strExeSQL);					
							}
						}
						
						// select 이외의 쿼리 실행
						if(!listStrExecuteQuery.isEmpty()) {
							ExecuteBatchSQL.runSQLExecuteBatch(listStrExecuteQuery, reqQuery,getUserDB(), getUserType(), getUserEMail());
						}
						
						// select 문장 실행
						if(SQLUtil.isStatement(reqQuery.getSql())) { //$NON-NLS-1$
							rsDAO = runSelect();
							sqlHistoryDAO.setRows(rsDAO.getDataList().getData().size());
						}
					} else {
						
						// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
						if(TransactionManger.isTransaction(reqQuery.getSql())) {
							TransactionManger.transactionQuery(reqQuery.getSql(), getUserEMail(), getUserDB());// userEmail, userDB)) return null;
						} else if(SQLUtil.isStatement(reqQuery.getSql())) {
							if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
								rsDAO = ExecuteQueryPlan.runSQLExplainPlan(reqQuery, getUserDB(), getPlanTableName());
							} else {
								rsDAO = runSelect();
								sqlHistoryDAO.setRows(rsDAO.getDataList().getData().size());
							}
						} else {
							ExecuteOtherSQL.runSQLOther(reqQuery, getUserDB(), getUserType(), getUserEMail());
						}
					}
					
				} catch(Exception e) {
					logger.error(Messages.MainEditor_50 + reqQuery.getSql(), e);
					
					sqlHistoryDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.toString()); //$NON-NLS-1$
					sqlHistoryDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					sqlHistoryDAO.setEndDateExecute(new Date());
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
						if(jobEvent.getResult().isOK()) {
							executeFinish(sqlHistoryDAO);
						} else {
							executeErrorProgress(jobEvent.getResult().getMessage());
						}
						
						// 쿼리 후 화면 정리 작업을 합니다.
						getRdbResultComposite().getCompositeQueryHistory().afterQueryInit(sqlHistoryDAO);
						
						// 주의) 일반적으로는 포커스가 잘 가지만, 
						// progress bar가 열렸을 경우 포커스가 잃어 버리게 되어 포커스를 주어야 합니다.
						getRdbResultComposite().setOrionTextFocus();

						// ace editor에게 작업이 끝났음을 알립니다.
						finallyEndExecuteCommand();
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		jobQueryManager.setPriority(Job.INTERACTIVE);
		jobQueryManager.setName(getUserDB().getDisplay_name() + reqQuery.getOriginalSql());
		jobQueryManager.schedule();
	}
	
	private boolean isRunning = true;
	private QueryExecuteResultDTO runSelect() throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), getUserDB(), reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
//		btnStopQuery.getDisplay().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				setControlProgress(true);
//			} 	// end run
//		});		
		
		isRunning = true;
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(getUserDB());
				javaConn = client.getDataSource().getConnection();
			} else {
				if(DBDefine.TAJO_DEFAULT == getUserDB().getDBDefine()) {
					javaConn = ConnectionPoolManager.getDataSource(getUserDB()).getConnection();
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(getUserEMail(), getUserDB());
				}
			}
			
			statement = javaConn.createStatement();
			final Statement stmt = statement;
			Thread stopCheckThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int i = 0;
					
					while(isRunning) {
						final int progressAdd = i++; 
						
						try {
							btnStopQuery.getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									logger.debug("[----------------------------------------------------progressAdd]"+ progressAdd);
									progressBarQuery.setSelection(progressAdd);
									
								}
							});
							
							logger.debug("=====> " + isRunning);
							Thread.sleep(100);
							if(!isRunning) stmt.cancel();
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				} 	// end run
			});
			stopCheckThread.start();
			logger.debug("== start stop thread====");
			
			ExecuteSelect es = new ExecuteSelect();
			rs = es.runSQLSelect(stmt, reqQuery);
			isRunning = false;
			
			rsDAO = new QueryExecuteResultDTO(true, rs, getQueryPageCount(), getIsResultComma());
			
			
//			if(getUserDB().getDBDefine() == DBDefine.HIVE2_DEFAULT || getUserDB().getDBDefine() == DBDefine.HIVE_DEFAULT) {
//			} else {
//				// 데이터셋에 추가 결과 셋이 있을경우 모두 fetch 하여 결과 그리드에 표시한다.
//				while(rs.getMoreResults()){  
//					rsDAO.getDataList().getData().addAll(ResultSetUtils.getResultToList(pstmt.getResultSet(), queryPageCount, isResultComma).getData());
//				}
//			}
			
		} finally {
			
//			try { if(statement != null) pstmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
		
//		ExecuteSelect es = new ExecuteSelect();
//		es.runSQLSelect(reqQuery, getUserDB(), getUserType(), getUserEMail(), getQueryPageCount(), getIsResultComma());
		
		
		return rsDAO;
	}

	private int getQueryPageCount() {
		return easyPreferenceData.getQueryPageCount();
	}

	/**
	 * error message 추가한다.
	 * @param msg
	 */
	public void executeErrorProgress(final String msg) {
		getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
		getRdbResultComposite().refreshMessageView(msg);
	}
	
	private UserDBDAO getUserDB() {
		return getRdbResultComposite().getUserDB();
	}
	
	/**
	 * 에디터를 실행 후에 마지막으로 실행해 주어야 하는 코드.
	 */
	private void finallyEndExecuteCommand() {
		setControlProgress(false);
		getRdbResultComposite().browserEvaluate(EditorFunctionService.EXECUTE_DONE);
	}
	
	/**
	 * control progress 
	 * 
	 * @param isStart
	 */
	public void setControlProgress(boolean isStart) {
		if(isStart) {
			progressBarQuery.setSelection(0);
//			btnStopQuery.setEnabled(true);
		} else {
			progressBarQuery.setSelection(100);
//			btnStopQuery.setEnabled(false);
		}
	}
	
	private boolean getIsResultComma() {
		return easyPreferenceData.isResultComma();
	}

	private String getPlanTableName() {
		return easyPreferenceData.getPlanTableName();
	}

	private String getUserType() {
		return null;
	}

	private String getUserEMail() {
		return easyPreferenceData.getUserEMail();
	}

	/**
	 * 쿼리 결과를 화면에 출력합니다.
	 * 
	 * @param executingSQLDAO 실행된 마지막 쿼리
	 */
	public void executeFinish(SQLHistoryDAO executingSQLDAO) {
		if(SQLUtil.isStatement(reqQuery.getSql())) {			
			// table data를 생성한다.
			final TadpoleResultSet trs = rsDAO.getDataList();
			
			sqlSorter = new SQLResultSorter(-999);
			SQLResultLabelProvider.createTableColumn(tvQueryResult, rsDAO, sqlSorter);
			tvQueryResult.setLabelProvider(new SQLResultLabelProvider());
			tvQueryResult.setContentProvider(new SQLResultContentProvider(trs.getData()));
			
			// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
			tvQueryResult.setInput(trs.getData());
			tvQueryResult.setSorter(sqlSorter);
			
			// 메시지를 출력합니다.
			float longExecuteTime = (executingSQLDAO.getEndDateExecute().getTime() - executingSQLDAO.getStartDateExecute().getTime()) / 1000f;
			String strResultMsg = "";
			if(trs.isEndOfRead()) {
				strResultMsg = String.format("%s %s (%s%s)", trs.getData().size(), Messages.MainEditor_33, longExecuteTime, Messages.MainEditor_74); //$NON-NLS-1$
			} else {
				// 
				// 데이터가 한계가 넘어 갔습니다.
				String strMsg = String.format(Messages.MainEditor_34, getQueryPageCount());
				strResultMsg = String.format("%s (%s%s)", strMsg, longExecuteTime, Messages.MainEditor_74); //$NON-NLS-1$
			}
			
			tvQueryResult.getTable().setToolTipText(strResultMsg);
			lblQueryResultStatus.setText(strResultMsg);
			lblQueryResultStatus.pack();
			sqlFilter.setTable(tvQueryResult.getTable());
			
			// Pack the columns
			TableUtil.packTable(tvQueryResult.getTable());
			getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
		} else {
			getRdbResultComposite().refreshMessageView("success. \n" + executingSQLDAO.getStrSQLText()); //$NON-NLS-1$
			getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
		}
	}
	
//	/**
//	 * 결과 테이블을 초기화 상태로 만듭니다.
//	 */
//	public void initResultTable() {
//		tvQueryResult.setLabelProvider( new SQLResultLabelProvider() );
//		tvQueryResult.setContentProvider(new SQLResultContentProvider(null) );
//		tvQueryResult.setInput(null);			
//		lblQueryResultStatus.setText(Messages.MainEditor_28 );
//	}
	
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

	public Job getJobQueryManager() {
		return jobQueryManager;
	}

	@Override
	protected void checkSubclass() {
	}
}