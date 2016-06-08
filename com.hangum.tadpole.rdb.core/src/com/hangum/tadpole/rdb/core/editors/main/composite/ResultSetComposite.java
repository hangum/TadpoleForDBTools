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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.engine.sql.paremeter.lang.GenericTokenParser;
import com.hangum.tadpole.engine.sql.paremeter.lang.JavaNamedParameterUtil;
import com.hangum.tadpole.engine.sql.paremeter.lang.OracleStyleSQLNamedParameterUtil;
import com.hangum.tadpole.engine.sql.util.ObjectCompileUtil;
import com.hangum.tadpole.engine.sql.util.ParameterUtils;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.AbstractResultDetailComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.ResultTableComposite;
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

/**
 * result set composite
 * 
 * @author hangum
 *
 */
public class ResultSetComposite extends Composite {
	private static final long serialVersionUID = -3706926974815713584L;

	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultSetComposite.class);
	
	/** 쿼리를 배치실행했을때 수행 할 수 있는 SQL 수 */
	private int BATCH_EXECUTE_SQL_LIMIT = 5;
	
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
	
	/** result composite */
	private ResultMainComposite rdbResultComposite;
	
	private ProgressBar progressBarQuery;
	private Button btnStopQuery;
	
	private ScrolledComposite scrolledComposite;
	private SashForm sashFormResult;
	/** 쿼리 결과 컴포짖 */
	private AbstractResultDetailComposite compositeResult;
	    
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param resultMainComposite 
	 */
	public ResultSetComposite(Composite parent, int style, ResultMainComposite rdbResultComposite) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		this.rdbResultComposite = rdbResultComposite;
		
		Composite compHead = new Composite(this, SWT.NONE);
		compHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(7, false);
		gl_composite.verticalSpacing = 2;
		gl_composite.horizontalSpacing = 2;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 2;
		compHead.setLayout(gl_composite);
		
		Label lblProgress = new Label(compHead, SWT.NONE);
		lblProgress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProgress.setText(Messages.get().ResultSetComposite_3);
		
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
		btnStopQuery.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/stop.png"));
		btnStopQuery.setToolTipText(Messages.get().QueryStop);
		btnStopQuery.setEnabled(false);
		
		btnAddVertical = new Button(compHead, SWT.NONE);
		btnAddVertical.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(SWT.VERTICAL == sashFormResult.getOrientation()) {
					sashFormResult.setOrientation(SWT.HORIZONTAL);	
					btnAddVertical.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/layouts_split_vertical.png"));										
				} else {
					sashFormResult.setOrientation(SWT.VERTICAL);
					btnAddVertical.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/layouts_split_horizontal.png"));
				}
				
				layout();
			}
		});
		btnAddVertical.setToolTipText(Messages.get().ChangeRotation);
		btnAddVertical.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/layouts_split_horizontal.png"));
		
		Label lblTemp = new Label(compHead, SWT.NONE);
		lblTemp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// scrolled composite
		scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		sashFormResult = new SashForm(scrolledComposite, SWT.VERTICAL);
		sashFormResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		scrolledComposite.setContent(sashFormResult);
		scrolledComposite.setMinSize(sashFormResult.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * 결과탭을 새로 생성하거나 합니다.
	 */
	private void changeResultType(final RequestQuery reqQuery, final List<QueryExecuteResultDTO> listRSDao) {
		
		// 모든 쿼리를 수행 하면 기존 결과 화면을 핀 유무와 상관없이 닫는다.
		if(reqQuery.getExecuteType() == EditorDefine.EXECUTE_TYPE.ALL) {
			Control[] childControls = sashFormResult.getChildren();
			for (int i=0; i<childControls.length; i++) {
				if(childControls[i] instanceof AbstractResultDetailComposite) {
					AbstractResultDetailComposite resultComposite = (AbstractResultDetailComposite)childControls[i];
					resultComposite.dispose();
				}
			}
		}
		
		// 화면 결과를 출력한다.
		try {
			for (QueryExecuteResultDTO rsDAO : listRSDao) {
				boolean isMakePing = listRSDao.size()==1?false:true;
				RequestQuery reqNewQuery = (RequestQuery)reqQuery.clone();
				reqNewQuery.setSql(rsDAO.getReqQuery());
				
				if(compositeResult != null && !compositeResult.getCompositeTail().getBtnPinSelection()) {
					compositeResult.printUI(reqNewQuery, rsDAO, isMakePing);
				} else {
					compositeResult = new ResultTableComposite(sashFormResult, SWT.BORDER, this);
					compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
					compositeResult.printUI(reqNewQuery, rsDAO, isMakePing);
				}
			}
			
			resultSashLayout();
		} catch(CloneNotSupportedException e) {
			logger.error("show execute result", e);
		}
	}
	
	/**
	 * 화면을 다시 조절한다.
	 */
	private void resultSashLayout() {
//		Map<Integer, Integer> mapWidths = new HashMap<Integer, Integer>();
//		Map<Integer, Integer> mapHeight = new HashMap<Integer, Integer>();
//		int intTmpCount = 0;
//		
//		try {
//			Control[] childControls = sashFormResult.getChildren();
//			for (int i=0; i<childControls.length; i++) {
//				Control control = childControls[i];
//				if(control instanceof AbstractResultDetailComposite) {
//					AbstractResultDetailComposite resultComposite = (AbstractResultDetailComposite)control;
//					mapWidths.put(intTmpCount, resultComposite.getBounds().width);
//					mapHeight.put(intTmpCount, resultComposite.getBounds().height);
//					intTmpCount++;
//				}
//			}
//			
//			int weights[] = new int[mapWidths.size()+1];
//			if(mapWidths.size() != 0) {
//				for (int i=0; i<mapWidths.size(); i++) {
//					float intCompositeWeights = 0f;
//					if(sashFormResult.getOrientation() == SWT.HORIZONTAL) {
//						intCompositeWeights = mapWidths.get(i) * 100;
//					} else {
//						intCompositeWeights = mapHeight.get(i) * 100;
//					}
//					weights[i] = (int)intCompositeWeights;
//					intTmpCount += weights[i];
//					// 처음 위젯이 생성 되었을 경우무조건 100이므로 반만 위젲을 준다. 
//					if(weights[i] == 100) {
//						weights[i] = 50;
//						intTmpCount = 50;
//					// 100 이 넘어가면 마지막 위젲에서 30로 만큼 위젲을 차지한다.
//					} else if(intTmpCount >= 100) { 
//						weights[i] = 30;
//					}
//				}
//				weights[mapWidths.size()] = 100 - intTmpCount;
//			} else {
//				weights[0] = 100;
//			}
//			sashFormResult.setWeights(weights);
//		} catch(Exception e) {
//			logger.error("calc weights of result composite");
//		}

		sashFormResult.layout();
		scrolledComposite.setMinSize(sashFormResult.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.layout();
		
		if(SWT.VERTICAL == scrolledComposite.getOrientation()) {
			ScrollBar sbX = scrolledComposite.getHorizontalBar();
			sbX.setSelection(sbX.getMaximum());
		} else {
			ScrollBar sbX = scrolledComposite.getVerticalBar();
			sbX.setSelection(sbX.getMaximum());
		}
	}

	/**
	 * get rdb result composite
	 * @return
	 */
	public ResultMainComposite getRdbResultComposite() {
		return rdbResultComposite;
	}
	
	/**
	 * 파라미터 쿼리인지 검사하여 쿼리를 만듭니다.
	 * @return
	 */
	private boolean ifIsParameterQuery(final RequestQuery reqQuery) {
		if(reqQuery.getExecuteType() == EditorDefine.EXECUTE_TYPE.ALL) return true;
		final DBDefine selectDBDefine = getUserDB().getDBDefine();
		if((selectDBDefine == DBDefine.HIVE_DEFAULT 		|| 
				selectDBDefine == DBDefine.HIVE2_DEFAULT 	|| 
				selectDBDefine == DBDefine.TAJO_DEFAULT 	||
				// not support this java.sql.ParameterMetaData 
				selectDBDefine == DBDefine.CUBRID_DEFAULT)
		) return true; 

		final Shell runShell = btnStopQuery.getShell();
		
		// java named parameter
		try {
			JavaNamedParameterUtil javaNamedParameterUtil = new JavaNamedParameterUtil();
			int paramCnt = javaNamedParameterUtil.calcParamCount(getUserDB(), reqQuery.getSql());
			if(paramCnt > 0) {
				ParameterDialog epd = new ParameterDialog(runShell, getUserDB(), paramCnt);
				if(Dialog.OK == epd.open()) {
					ParameterObject paramObj = epd.getParameterObject();
					String repSQL = ParameterUtils.fillParameters(reqQuery.getSql(), paramObj.getParameter());
					reqQuery.setSql(repSQL);
					
					if(logger.isDebugEnabled()) logger.debug("[Java Type]User parameter query is  " + repSQL); //$NON-NLS-1$
					return true;
				} else {
					return false;
				}
			}
		} catch(Exception e) {
			logger.error("Java style parameter parse", e); //$NON-NLS-1$
		}
		
		// oracle parameter
		try {
			OracleStyleSQLNamedParameterUtil oracleNamedParamUtil = new OracleStyleSQLNamedParameterUtil();
			String strSQL = oracleNamedParamUtil.parse(reqQuery.getSql());
			
			Map<Integer, String> mapIndexToName = oracleNamedParamUtil.getMapIndexToName();
			if(!mapIndexToName.isEmpty()) {
				
				ParameterDialog epd = new ParameterDialog(runShell, getUserDB(), mapIndexToName);
				if(Dialog.OK == epd.open()) {
					ParameterObject paramObj = epd.getOracleParameterObject(mapIndexToName);
					String repSQL = ParameterUtils.fillParameters(strSQL, paramObj.getParameter());
					reqQuery.setSql(repSQL);
					
					if(logger.isDebugEnabled()) logger.debug("[Oracle Type] User parameter query is  " + repSQL); //$NON-NLS-1$
					return true;
				} else {
					return false;
				}
			}
		} catch(Exception e) {
			logger.error("Oracle sytle parameter parse", e); //$NON-NLS-1$
		}

		// mybatis shap
		GenericTokenParser mybatisShapeUtil = new GenericTokenParser("#{", "}");
		String strSQL = mybatisShapeUtil.parse(reqQuery.getSql());
		Map<Integer, String> mapIndexToName = mybatisShapeUtil.getMapIndexToName();
		if(!mapIndexToName.isEmpty()) {
			
			ParameterDialog epd = new ParameterDialog(runShell, getUserDB(), mapIndexToName);
			if(Dialog.OK == epd.open()) {
				ParameterObject paramObj = epd.getOracleParameterObject(mapIndexToName);
				String repSQL = ParameterUtils.fillParameters(strSQL, paramObj.getParameter());
				reqQuery.setSql(repSQL);
				
				if(logger.isDebugEnabled()) logger.debug("[mybatisShapeUtil] User parameter query is  " + repSQL); //$NON-NLS-1$
				return true;
			} else {
				return false;
			}
		}
		
		if(GetPreferenceGeneral.getIsMyBatisDollor()) {
			GenericTokenParser mybatisDollarUtil = new GenericTokenParser("${", "}");
			strSQL = mybatisDollarUtil.parse(reqQuery.getSql());
			mapIndexToName = mybatisDollarUtil.getMapIndexToName();
			if(!mapIndexToName.isEmpty()) {
				ParameterDialog epd = new ParameterDialog(runShell, getUserDB(), mapIndexToName);
				if(Dialog.OK == epd.open()) {
					ParameterObject paramObj = epd.getOracleParameterObject(mapIndexToName);
					String repSQL = ParameterUtils.fillParameters(strSQL, paramObj.getParameter());
					reqQuery.setSql(repSQL);
					
					if(logger.isDebugEnabled()) logger.debug("[mybatisDollarUtil] User parameter query is  " + repSQL); //$NON-NLS-1$
					return true;
				} else {
					return false;
				}
			}
		}

		return true;
	}
	
	/**
	 * 쿼리를 수행합니다.
	 * 
	 * @param reqQuery
	 */
	public boolean executeCommand(final RequestQuery reqQuery) {
		// 쿼리를 이미 실행 중이라면 무시합니다
		if(jobQueryManager != null) {
			if(Job.RUNNING == jobQueryManager.getState()) {
				if(logger.isDebugEnabled()) logger.debug("\t\t================= return already running query job "); //$NON-NLS-1$
				executeErrorProgress(reqQuery, new Exception(Messages.get().ResultSetComposite_1), Messages.get().ResultSetComposite_1);
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
		if(!ifIsParameterQuery(reqQuery)) return false;
		
		// 프로그래스 상태와 쿼리 상태를 초기화한다.
		controlProgress(true);
		
//		if(logger.isDebugEnabled()) logger.debug("Start query time ==> " + System.currentTimeMillis() ); //$NON-NLS-1$
//		this.rsDAO = new QueryExecuteResultDTO();
		if(compositeResult != null) compositeResult.initUI();
		
//		// selected first tab request quring.
//		rdbResultComposite.resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
		
		final List<QueryExecuteResultDTO> listRSDao = new ArrayList<>();
		
		final int intSelectLimitCnt = GetPreferenceGeneral.getSelectLimitCount();
		final String strPlanTBName 	= GetPreferenceGeneral.getPlanTableName();
		final String strUserEmail 	= SessionManager.getEMAIL();
		final int queryTimeOut 		= GetPreferenceGeneral.getQueryTimeOut();
		final int intCommitCount 	= Integer.parseInt(GetPreferenceGeneral.getRDBCommitCount());
		final String strNullValue	= GetPreferenceGeneral.getResultNull();
		final UserDBDAO tmpUserDB 	= getUserDB();
		final String errMsg = Messages.get().MainEditor_21;
		final RequestResultDAO reqResultDAO = new RequestResultDAO();
		
		jobQueryManager = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(reqQuery.getSql(), IProgressMonitor.UNKNOWN);
				
				reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
				reqResultDAO.setIpAddress(reqQuery.getUserIp());
				reqResultDAO.setStrSQLText(reqQuery.getOriginalSql());
				
				try {
					if(reqQuery.getExecuteType() == EditorDefine.EXECUTE_TYPE.ALL) {
						List<String> listStrExecuteQuery = new ArrayList<String>();
						List<String> listStrSQL = new ArrayList<String>();
						for (String strSQL : reqQuery.getSql().split(PublicTadpoleDefine.SQL_DELIMITER)) {
							String strExeSQL = SQLUtil.makeExecutableSQL(tmpUserDB, strSQL);
							
							// execute batch update는 ddl문이 있으면 안되어서 실행할 수 있는 쿼리만 걸러 줍니다.
							if(!SQLUtil.isStatement(strExeSQL)) {
								listStrExecuteQuery.add(strExeSQL);
							} else {
								listStrSQL.add(strExeSQL);
							}
						}
						// select 이외의 쿼리 실행
						if(!listStrExecuteQuery.isEmpty()) {
							ExecuteBatchSQL.runSQLExecuteBatch(errMsg, listStrExecuteQuery, reqQuery, getUserDB(), getDbUserRoleType(), intCommitCount, strUserEmail);
						}
						
						if(!listStrSQL.isEmpty()) {
							for(int i=0;i<listStrSQL.size(); i++) {
								if(i >= BATCH_EXECUTE_SQL_LIMIT) break;
								reqQuery.setSql(listStrSQL.get(i));
								QueryExecuteResultDTO qeResultDao = runSelect(reqQuery, queryTimeOut, strUserEmail, intSelectLimitCnt, 0, strNullValue);
								listRSDao.add(qeResultDao);
							}
						}
						
					} else {
						if(reqQuery.isStatement()) {
							if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
								listRSDao.add(ExecuteQueryPlan.runSQLExplainPlan(reqQuery, getUserDB(), strPlanTBName, strNullValue));
							} else {
								QueryExecuteResultDTO rsDAO = runSelect(reqQuery, queryTimeOut, strUserEmail, intSelectLimitCnt, 0, strNullValue);
								if(rsDAO.getDataList() == null) {
									reqResultDAO.setRows(0);
								} else {
									reqResultDAO.setRows(rsDAO.getDataList().getData().size());
								}
								listRSDao.add(rsDAO);
							}
						} else if(TransactionManger.isTransaction(reqQuery.getSql())) {
							if(TransactionManger.isStartTransaction(reqQuery.getSql())) {
								startTransactionMode();
								reqQuery.setAutoCommit(false);
							} else {
								TransactionManger.calledCommitOrRollback(reqQuery.getSql(), strUserEmail, getUserDB());
							}
						} else {
							ExecuteOtherSQL.runPermissionSQLExecution(errMsg, reqQuery, getUserDB(), getDbUserRoleType(), strUserEmail);
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
						// 처리를 위해 결과를 담아 둡니다.
						reqQuery.setResultDao(reqResultDAO);
						
						// 쿼리가 정상일 경우 결과를 테이블에 출력하고, 히스토리를 남기며, 필요하면 오브젝트익스플로에 리프레쉬한다.
						if(jobEvent.getResult().isOK()) {
							executeFinish(reqQuery, listRSDao);
						} else {
							executeErrorProgress(reqQuery, jobEvent.getResult().getException(), jobEvent.getResult().getMessage());
							getRdbResultComposite().getMainEditor().browserEvaluateToStr(EditorFunctionService.SET_SELECTED_TEXT); //$NON-NLS-1$
						}
						
						// 히스토리 화면을 갱신합니다.
						getRdbResultComposite().getCompositeQueryHistory().afterQueryInit(reqResultDAO);
						
						// 모든 쿼리가 종료 되었음을 알린다.
						finallyEndExecuteCommand(listRSDao);
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
	private Button btnAddVertical;

	/**
	 * 실제쿼리를 호출한다.
	 * 
	 * @param reqQuery
	 * @param queryTimeOut
	 * @param strUserEmail
	 * @param intSelectLimitCnt
	 * @param intStartCnt
	 * @param strNullValue 
	 * @return
	 * @throws Exception
	 */
	public QueryExecuteResultDTO runSelect(final RequestQuery reqQuery, final int queryTimeOut, final String strUserEmail, final int intSelectLimitCnt, final int intStartCnt, String strNullValue) throws Exception {
		String strSQL = reqQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug("==> real execute query : " + strSQL);
		
		if(!PermissionChecker.isExecute(getDbUserRoleType(), getUserDB(), strSQL)) {
			throw new Exception(Messages.get().MainEditor_21);
		}
		
		QueryExecuteResultDTO queryResultDAO = null; 
		
		// 확장 포인트가 있다면 확장 포인트의 쿼리로 대체합니다.
		IMainEditorExtension[] extensions = getRdbResultComposite().getMainEditor().getMainEditorExtions();
		if(extensions != null) {
			for (IMainEditorExtension iMainEditorExtension : extensions) {
				String strCostumSQL = iMainEditorExtension.sqlCostume(strSQL);
				if(!strCostumSQL.equals(strSQL)) {
					if(logger.isDebugEnabled()) logger.debug("** extension costume sql is : " + strCostumSQL); //$NON-NLS-1$
					strSQL = strCostumSQL;
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
			cst.setName("TDB Query Stop checker"); //$NON-NLS-1$
			esCheckStop.execute(cst);
			
			// execute query
			execServiceQuery = Executors.newSingleThreadExecutor();
			if(intStartCnt == 0) {
				resultSet = runSQLSelect(statement, strSQL);
			} else {
				strSQL = PartQueryUtil.makeSelect(getUserDB(), strSQL, intStartCnt, intSelectLimitCnt);
				
				if(logger.isDebugEnabled()) logger.debug("part sql called : " + strSQL);
				resultSet = runSQLSelect(statement, strSQL);
			}
			
			queryResultDAO = new QueryExecuteResultDTO(getUserDB(), reqQuery.getSql(), true, resultSet, intSelectLimitCnt, intStartCnt, strNullValue);
		} catch(Exception e) {
			throw e;
		} finally {
			isCheckRunning = false;
			
			try { if(statement != null) statement.close(); } catch(Exception e) {}
			try { if(resultSet != null) resultSet.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
		
		return queryResultDAO;
	}
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param strSQL
	 */
	private ResultSet runSQLSelect(final Statement statement, final String strSQL) throws Exception {
		
		Future<ResultSet> queryFuture = execServiceQuery.submit(new Callable<ResultSet>() {
			@Override
			public ResultSet call() throws SQLException {
				statement.execute(strSQL);
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
							if(!stmt.isClosed()) execServiceQuery.shutdownNow();
							
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
			} finally {
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
		getRdbResultComposite().refreshErrorMessageView(requestQuery, throwable, msg);
	}
	
	private UserDBDAO getUserDB() {
		return getRdbResultComposite().getUserDB();
	}
	
	/**
	 * 에디터를 실행 후에 마지막으로 실행해 주어야 하는 코드.
	 */
	private void finallyEndExecuteCommand(final List<QueryExecuteResultDTO> listRsDAO) {
		controlProgress(false);
		compositeResult.endQuery();
		
		// 확장포인트에 실행결과를 위임합니다. 
		IMainEditorExtension[] extensions = getRdbResultComposite().getMainEditor().getMainEditorExtions();
		if(extensions == null) return;
		for (IMainEditorExtension iMainEditorExtension : extensions) {
			try {
				iMainEditorExtension.queryEndedExecute(listRsDAO.get(0));
			} catch(Exception e) {
				logger.error("sql result extension", e);
			}
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
	 */
	public void executeFinish(final RequestQuery reqQuery, final List<QueryExecuteResultDTO> listRSDao) {
		if(reqQuery.isStatement()) {
			
			if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
				getRdbResultComposite().setQueryPlanView(reqQuery, listRSDao.get(0));
				getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.QUERY_PLAN);
			} else {	// table data를 생성한다.
				changeResultType(reqQuery, listRSDao);
			}
		} else {
			
			if(reqQuery.getSqlType() == SQL_TYPE.DDL) {
				String strDefaultMsg = Messages.get().ResultSetComposite_10 + reqQuery.getResultDao().getStrSQLText();
				String retMsg = ObjectCompileUtil.validateObject(getUserDB(), reqQuery.getSqlDDLType(), reqQuery.getSqlObjectName());
				if(!"".equals(retMsg)) { //$NON-NLS-1$
					strDefaultMsg = Messages.get().ObjectEditor_7 + retMsg;
				}
				
				getRdbResultComposite().refreshInfoMessageView(reqQuery, strDefaultMsg);
				getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
				refreshExplorerView(getUserDB(), reqQuery);
			} else {
				getRdbResultComposite().refreshInfoMessageView(reqQuery, Messages.get().ResultSetComposite_10 + reqQuery.getResultDao().getStrSQLText());
				getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
			}
			
			if(reqQuery.getQueryStatus() == PublicTadpoleDefine.QUERY_DDL_STATUS.CREATE ||
					reqQuery.getQueryStatus() == PublicTadpoleDefine.QUERY_DDL_STATUS.DROP ||
					reqQuery.getQueryStatus() == PublicTadpoleDefine.QUERY_DDL_STATUS.ALTER
			) {
//				// working schema_history 에 history 를 남깁니다.
//				aram strWorkType TABLE, VIEW, PROCEDURE, FUNCTION, TRIGGER...
//				 * @param strObjecType CREATE, ALTER, DROP
//				 * @param strObjectId 객체 명
//				String strWorkType, String strObjecType, String strObjectId, String strSQL) {
				try {
					TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), 
							getUserDB(),
							reqQuery.getQueryStatus().name(), //$NON-NLS-1$
							reqQuery.getSqlDDLType().name(),
							reqQuery.getSqlObjectName(),
							reqQuery.getSql());
				} catch(Exception e) {
					logger.error("save schemahistory", e); //$NON-NLS-1$
				}
			}
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
	
	@Override
	public void dispose() {
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