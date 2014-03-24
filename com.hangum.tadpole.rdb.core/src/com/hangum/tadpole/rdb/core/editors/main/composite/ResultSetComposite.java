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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
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
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.utils.UserPreference;
import com.hangum.tadpole.rdb.core.util.CubridExecutePlanUtils;
import com.hangum.tadpole.rdb.core.util.OracleExecutePlanUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.PartQueryUtil;
import com.hangum.tadpole.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.sql.util.ResultSetUtilDTO;
import com.hangum.tadpole.sql.util.ResultSetUtils;
import com.hangum.tadpole.sql.util.SQLUtil;
import com.hangum.tadpole.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.SWTResourceManager;

/**
 * result set view
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
	private ResultSetUtilDTO rsDAO = new ResultSetUtilDTO();
	
	private ProgressBar progressBarQuery;
	private Button btnStp;
//	private Button btnPrev, btnNext;
	
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
		
		btnStp = new Button(composite, SWT.NONE);
		btnStp.setText(Messages.RDBResultComposite_btnStp_text);
		btnStp.setEnabled(false);
		
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvQueryResult = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		tvQueryResult.setUseHashlookup(true);
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
		
//		btnPrev = new Button(compositeBtn, SWT.NONE);
//		btnPrev.setEnabled(false);
//		btnPrev.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				btnPrev();
//			}
//		});
//		btnPrev.setText(Messages.MainEditor_8);
//		
//		btnNext = new Button(compositeBtn, SWT.NONE);
//		btnNext.setEnabled(false);
//		btnNext.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				btnNext();
//			}
//		});
//		btnNext.setText(Messages.MainEditor_9);
		
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
				for(int i=0; i<rsDAO.getDataList().size(); i++) {
					Map<Integer, Object> mapColumns = rsDAO.getDataList().get(i);
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
		this.rsDAO = new ResultSetUtilDTO();
		sqlFilter.setFilter("");
		textFilter.setText("");

		// 쿼리를 실행 합니다. 
		final SQLHistoryDAO sqlHistoryDAO = new SQLHistoryDAO();
		jobQueryManager = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(reqQuery.getSql(), IProgressMonitor.UNKNOWN);
				
				sqlHistoryDAO.setStartDateExecute(new Date());
				sqlHistoryDAO.setIpAddress(reqQuery.getUserIp());
				try {
						
					if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {
						
						sqlHistoryDAO.setStrSQLText(reqQuery.getOriginalSql());
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
							runSQLExecuteBatch(listStrExecuteQuery, reqQuery);
						}
						
						// select 문장 실행
						if(SQLUtil.isStatement(reqQuery.getSql())) { //$NON-NLS-1$
							sqlHistoryDAO.setStartDateExecute(new Date());
							sqlHistoryDAO.setStrSQLText(reqQuery.getSql());
							sqlHistoryDAO.setIpAddress(reqQuery.getUserIp());

							runSQLSelect(reqQuery);
							sqlHistoryDAO.setRows(rsDAO.getDataList().size());
						}
					} else {
						sqlHistoryDAO.setStrSQLText(reqQuery.getSql());
						
						if(SQLUtil.isStatement(reqQuery.getSql())) {
							runSQLSelect(reqQuery);
							sqlHistoryDAO.setRows(rsDAO.getDataList().size());
						} else {
							runSQLOther(reqQuery);
						}
					}

				} catch(Exception e) {
					logger.error(Messages.MainEditor_50 + reqQuery.getSql(), e);
					
					sqlHistoryDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.toString()); //$NON-NLS-1$
					sqlHistoryDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
					
					sqlHistoryDAO.setEndDateExecute(new Date());
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
							executeFinish(reqQuery, sqlHistoryDAO, rsDAO);
						} else {
							executeErrorProgress(jobEvent.getResult().getMessage());
						}
						
						// 쿼리 후 화면 정리 작업을 합니다.
						getRdbResultComposite().getCompositeQueryHistory().afterQueryInit(sqlHistoryDAO);
						
						// 주의) 일반적으로는 포커스가 잘 가지만, 
						// progress bar가 열렸을 경우 포커스가 잃어 버리게 되어 포커스를 주어야 합니다.
						getRdbResultComposite().setOrionTextFocus();
						
						// ace editor에게 작업이 끝났음을 알립니다.
//						finallyEndExecuteCommand();
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		jobQueryManager.setPriority(Job.SHORT);
		jobQueryManager.setName(getUserDB().getDisplay_name());
		jobQueryManager.schedule();
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
			int readCount = (rsDAO.getDataList().size()+1) - getQueryPageCount();
			if(readCount < -1) readCount = rsDAO.getDataList().size();
			else if(readCount > getQueryPageCount()) readCount = getQueryPageCount();
				
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
	
//	/**
//	 * 에디터를 실행 후에 마지막으로 실행해 주어야 하는 코드.
//	 */
//	private void finallyEndExecuteCommand() {
//		mainEditor.browserEvaluate(EditorFunctionService.EXECUTE_DONE);
//	}
	
	/**
	 * control progress 
	 * 
	 * @param isStart
	 */
	public void setControlProgress(boolean isStart) {
		if(isStart) {
			progressBarQuery.setMinimum(0);
			btnStp.setEnabled(true);
		} else {
			progressBarQuery.setMinimum(100);
			btnStp.setEnabled(true);
		}
	}
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 */
	private void runSQLSelect(final RequestQuery reqQuery) throws Exception {
		rsDAO = new ResultSetUtilDTO();
		if(!PermissionChecker.isExecute(getUserType(), getUserDB(), reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == getUserDB().getDBDefine()) {
			rsDAO = new TajoConnectionManager().select(getUserDB(), reqQuery.getSql(), getQueryPageCount(), reqQuery.isAutoCommit());
			return;
		}  
		
		// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
		if(transactionQuery(reqQuery.getSql())) return;
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		java.sql.Statement stmt = null;
		
		try {
			
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(getUserDB());
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(getUserEMail(), getUserDB());
			}
			
			if(reqQuery.getMode() == EditorDefine.QUERY_MODE.QUERY) {

//				https://github.com/hangum/TadpoleForDBTools/issues/363 SQL 파서를 제대로 갖출때까지는 로직을 막습니다.
//				String tmpExeQuery = reqQuery.getSql();
//				if(reqQuery.getSql().toUpperCase().startsWith("SELECT")) { //$NON-NLS-1$
//					tmpExeQuery = PartQueryUtil.makeSelect(userDB, reqQuery.getSql(), 0, queryResultCount);
//					if(logger.isDebugEnabled()) logger.debug("[SELECT] " + reqQuery.getSql()); //$NON-NLS-1$
//				}
				
				pstmt = javaConn.prepareStatement(reqQuery.getSql());
				//  환경설정에서 원하는 조건을 입력하였을 경우.
				rs = pstmt.executeQuery();
				
			// explain
			}  else if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
				
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(DBDefine.getDBDefine(getUserDB()) == DBDefine.CUBRID_DEFAULT) {
					
					rsDAO.setColumnName(CubridExecutePlanUtils.getMapColumns());
					rsDAO.setDataList(CubridExecutePlanUtils.getMakeData(CubridExecutePlanUtils.plan(getUserDB(), reqQuery.getSql())));
					
					return;
					
				} else if(DBDefine.getDBDefine(getUserDB()) == DBDefine.ORACLE_DEFAULT) {					
					// generation to statement id for query plan. 
					pstmt = javaConn.prepareStatement("select USERENV('SESSIONID') from dual "); //$NON-NLS-1$
					rs = pstmt.executeQuery(); 
					String statement_id = "tadpole"; //$NON-NLS-1$
					if (rs.next()) statement_id = rs.getString(1);
					
					pstmt = javaConn.prepareStatement("delete from " + getPlanTableName() + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					pstmt.execute(); 
					
					// 플랜결과를 디비에 저장합니다.
					OracleExecutePlanUtils.plan(getUserDB(), reqQuery.getSql(), getPlanTableName(), javaConn, pstmt, statement_id);
					// 저장된 결과를 가져와서 보여줍니다.
					pstmt = javaConn.prepareStatement("select * from " + getPlanTableName() + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					rs = pstmt.executeQuery(); 
				 } else if(DBDefine.MSSQL_8_LE_DEFAULT == DBDefine.getDBDefine(getUserDB()) || DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(getUserDB())) {
					 stmt = javaConn.createStatement();
					 stmt.execute(PartQueryUtil.makeExplainQuery(getUserDB(), "ON")); //$NON-NLS-1$

					 pstmt = javaConn.prepareStatement(reqQuery.getSql());
					 rs = pstmt.executeQuery();

					 stmt.execute(PartQueryUtil.makeExplainQuery(getUserDB(), "OFF")); //$NON-NLS-1$
				} else {
				
					pstmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(getUserDB(), reqQuery.getSql()));
					rs = pstmt.executeQuery();
					
				}
			}

			//////////////////////////////////////////////////////////////////////////////////////////////////////
			rsDAO = new ResultSetUtilDTO(true, rs, getQueryPageCount(), getIsResultComma());
			
			if(getUserDB().getDBDefine() == DBDefine.HIVE2_DEFAULT || getUserDB().getDBDefine() == DBDefine.HIVE_DEFAULT) {
			} else {
				// 데이터셋에 추가 결과 셋이 있을경우 모두 fetch 하여 결과 그리드에 표시한다.
				while(pstmt.getMoreResults()){  
					if(logger.isDebugEnabled()) logger.debug("\n**********has more resultset1...***********"); //$NON-NLS-1$
					rsDAO.addDataAll(ResultSetUtils.getResultToList(pstmt.getResultSet(), getQueryPageCount(), getIsResultComma()));
				}
			}
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
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

	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * 
	 * @param listQuery
	 * @throws Exception
	 */
	private void runSQLExecuteBatch(List<String> listQuery, final RequestQuery reqQuery) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), getUserDB(), listQuery)) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(getUserDB());
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(getUserEMail(), getUserDB());
			}
			statement = javaConn.createStatement();
			
			for (String strQuery : listQuery) {
				// 쿼리 중간에 commit이나 rollback이 있으면 어떻게 해야 하나???
				if(!transactionQuery(strQuery)) { 
					
					if(StringUtils.startsWith(strQuery.trim().toUpperCase(), "CREATE TABLE")) { //$NON-NLS-1$
						strQuery = StringUtils.replaceOnce(strQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
				}
				statement.addBatch(strQuery);
			}
			statement.executeBatch();
		} catch(Exception e) {
			logger.error("Execute batch update", e); //$NON-NLS-1$
			throw e;
		} finally {
			try { statement.close();} catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}

	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param reqQuery
	 * @exception
	 */
	private void runSQLOther(RequestQuery reqQuery) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), getUserDB(), reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == getUserDB().getDBDefine()) {
			new TajoConnectionManager().executeUpdate(getUserDB(),reqQuery.getSql());
		} else { 
		
			// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
			if(transactionQuery(reqQuery.getSql())) return;
			
			java.sql.Connection javaConn = null;
			Statement statement = null;
			try {
				if(reqQuery.isAutoCommit()) {
					SqlMapClient client = TadpoleSQLManager.getInstance(getUserDB());
					javaConn = client.getDataSource().getConnection();
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(getUserEMail(), getUserDB());
				}
				statement = javaConn.createStatement();
				
				// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다.
				if(getUserDB().getDBDefine() == DBDefine.MYSQL_DEFAULT || getUserDB().getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWith(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
						reqQuery.setSql(StringUtils.replaceOnce(reqQuery.getSql(), "(", " (")); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} else if(getUserDB().getDBDefine() == DBDefine.ORACLE_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWithIgnoreCase(checkSQL, "CREATE OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE TRIGGER") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER TRIGGER") //$NON-NLS-1$
					) { //$NON-NLS-1$
						reqQuery.setSql(reqQuery.getSql() + UserPreference.QUERY_DELIMITER); //$NON-NLS-1$
					}
				}
				
				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
				if(getUserDB().getDBDefine() == DBDefine.HIVE_DEFAULT) statement.execute(reqQuery.getSql());
				else statement.executeUpdate(reqQuery.getSql());
				
			} finally {
				try { statement.close();} catch(Exception e) {}
	
				if(reqQuery.isAutoCommit()) {
					try { javaConn.close(); } catch(Exception e){}
				}
			}
		}  	// end which db
		
		// create table, drop table이면 작동하도록			
		try {
			if(!SQLUtil.isStatement(reqQuery.getSql())) refreshExplorerView();
		} catch(Exception e) {
			logger.error("CREATE, DROP, ALTER Query refersh error" + reqQuery.getSql()); //$NON-NLS-1$
		}
	}
	
	/**
	 * transaction 쿼리인지 검사합니다.
	 * 
	 * @param query
	 * @return
	 */
	private boolean transactionQuery(String query) {
		if(StringUtils.startsWith(query, "commit")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.commit(getUserEMail(), getUserDB());
			return true;
		}
		// 
		if(StringUtils.startsWith(query, "rollback")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.rollback(getUserEMail(), getUserDB());
			return true;
		}
		
		return false;
	}

	private String getUserEMail() {
		return easyPreferenceData.getUserEMail();
	}

	/**
	 * CREATE, DROP, ALTER 문이 실행되어 ExplorerViewer view를 리프레쉬합니다.
	 */
	private void refreshExplorerView() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().
		getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
					ev.refreshCurrentTab(getUserDB());
				} catch (PartInitException e) {
					logger.error("ExplorerView show", e); //$NON-NLS-1$
				}
			}
			
		});
	}
	
	/**
	 * 1) 마지막 쿼리를 받아서 selct 문일 경우 쿼리 네비게이션을 확성화 해준다.
	 * 2) filter를 설정한다.
	 * 
	 * @param executingSQLDAO 실행된 마지막 쿼리
	 */
	public void executeFinish(final RequestQuery reqQuery, SQLHistoryDAO executingSQLDAO, ResultSetUtilDTO rsDAO) {
//		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
//		setFilter();
		
//		if(SQLUtil.isStatement(executingSQLDAO.getStrSQLText())) {			
//			btnPrev.setEnabled(false);
//			if( rsDAO.getDataList().size() < getQueryPageCount() ) btnNext.setEnabled(false);
//			else btnNext.setEnabled(true);
//		} else {
//			btnPrev.setEnabled(false);
//			btnNext.setEnabled(false);
//		}
		
		// 쿼리의 결과를 화면에 출력합니다.
		setResultTable(executingSQLDAO);
	}
	
//	/**
//	 * 다음 버튼 처리
//	 * 
//	 * pageLocation
//	 * 
//	 */
//	private void btnNext() {
//		// table data를 생성한다.
//		sqlSorter = new SQLResultSorter(-999);
//		
//		List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
//		
//		// 쿼리 결과를 사용자가 설정 한 만큼 보여준다.
//		int startCount 	= getQueryPageCount() * pageNumber;
//		int endCount 	= getQueryPageCount() * (pageNumber+1);
//		if(logger.isDebugEnabled()) logger.debug("btnNext ======> [start point]" + startCount + "\t [endCount]" + endCount); //$NON-NLS-1$ //$NON-NLS-2$
//		
////		//  
////		if(endCount >= (rsDAO.getDataList().size()+1)) {
////			endCount = rsDAO.getDataList().size();
////			
////			// 다음 버튼을 비활성화 한다.
////			btnNext.setEnabled(false);
////		}
//		
//		// 데이터 출력.
//		for(int i=startCount; i<endCount; i++) {
//			showList.add(rsDAO.getDataList().get(i));
//		}
//		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
//		
//		tvQueryResult.setInput(showList);
//		tvQueryResult.setSorter(sqlSorter);
//		
//		// Pack the columns
//		TableUtil.packTable(tvQueryResult.getTable());
//		
//		// page 번호를 하나 추가한다.
//		pageNumber++;
//		if(!btnPrev.getEnabled()) btnPrev.setEnabled(true);
//	}
//	
//	/**
//	 * 이전 버튼 처리
//	 */
//	private void btnPrev() {
//		// table data를 생성한다.
//		sqlSorter = new SQLResultSorter(-999);		
//		List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
//		
//		// 쿼리 결과를 사용자가 설정 한 만큼 보여준다.
//		int startCount 	= getQueryPageCount() * (pageNumber-2);
//		int endCount 	= getQueryPageCount() * (pageNumber-1);
//		if(logger.isDebugEnabled()) logger.debug("btnPrev ======> [start point]" + startCount + "\t [endCount]" + endCount); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		if(startCount <= 0) {
//			startCount = 0;
//			endCount = getQueryPageCount();
//			
//			btnPrev.setEnabled(false);
//		}
//		
//		// 데이터 출력.
//		for(int i=startCount; i<endCount; i++) {
//			showList.add(rsDAO.getDataList().get(i));
//		}
//		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
//		
//		tvQueryResult.setInput(showList);
//		tvQueryResult.setSorter(sqlSorter);
//		
//		// Pack the columns
//		TableUtil.packTable(tvQueryResult.getTable());
//		
//		// page 번호를 하나 추가한다.
//		pageNumber--;
//		if(!btnNext.getEnabled()) btnNext.setEnabled(true);
//	}
//	
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
	
	@Override
	protected void checkSubclass() {
	}

	public Job getJobQueryManager() {
		return jobQueryManager;
	}
}