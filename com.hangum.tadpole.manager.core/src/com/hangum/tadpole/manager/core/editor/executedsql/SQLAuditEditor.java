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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.USER_ROLE_TYPE;
import com.hangum.tadpole.commons.util.CSVFileUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.utils.TimeZoneUtil;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * 실행한 쿼리.
 * 
 * 
 * @since 2015.05.31 add download function(https://github.com/hangum/TadpoleForDBTools/issues/587)
 * @author hangum
 * 
 */
public class SQLAuditEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLAuditEditor.class);

	public static String ID = "com.hangum.tadpole.manager.core.editor.manager.executed_sql"; //$NON-NLS-1$
	/** 마지막 검색시 사용하는 UserDBDAO */
	private UserDBDAO searchUserDBDAO = null;

	/** 제일 처음 설정 될때 사용하는 dao */
	private UserDAO userDAO;
	private UserDBDAO userDBDAO;
	private PublicTadpoleDefine.USER_ROLE_TYPE roleType;
	
	/** 사용자 db list */
	private List<UserDBDAO> listUserDBDAO;

	private Combo comboDatabase;
	private Combo comboTypes;
	private Text textEmail;
	
	private Text textMillis;
	private Grid gridHistory;
	private final String[] strArrHeader = {"#", Messages.get().Database, Messages.get().User, Messages.get().Date, Messages.get().SQL, Messages.get().MS, Messages.get().ExecutedSQLEditor_7, Messages.get().ResultType, Messages.get().Message, Messages.get().IP}; //$NON-NLS-1$

	private Button btnSearch;

	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;

	/** result list */
	private Text textSearch;
	
	/** 
	 * define grid data
	 * 
	 *  key is row id, value is data
	 */
	private Map<String, RequestResultDAO> mapSQLHistory = new HashMap<String, RequestResultDAO>();

	/** tail composite */
	private Composite compositeTail;
	private Button btnShowQueryEditor;

	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	private ToolItem tltmStart;
	private ToolItem tltmStop;
	private boolean isThread = true;
	private final ServerPushSession pushSession = new ServerPushSession();
	private boolean isNotRefreshUi = true;
	
	/**
	 * 
	 */
	public SQLAuditEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		parent.setLayout(gl_parent);
		
		Composite compositeToolbarHead = new Composite(parent, SWT.NONE);
		compositeToolbarHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(3, false);
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.horizontalSpacing = 0;
		gl_compositeHead.marginWidth = 0;
		compositeToolbarHead.setLayout(gl_compositeHead);
		
		ToolBar toolBar = new ToolBar(compositeToolbarHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tltmStart = new ToolItem(toolBar, SWT.NONE);
		tltmStart.setToolTipText(Messages.get().SQLAuditEditor_0);
		tltmStart.setImage(GlobalImageUtils.getStart()); //$NON-NLS-1$
		tltmStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNotRefreshUi = true;
				
				tltmStart.setEnabled(false);
				tltmStop.setEnabled(true);
			}
		});
		tltmStart.setEnabled(false);
		
		tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setToolTipText(Messages.get().SQLAuditEditor_1);
		tltmStop.setImage(GlobalImageUtils.getStop()); //$NON-NLS-1$
		tltmStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNotRefreshUi = false;
				
				tltmStart.setEnabled(true);
				tltmStop.setEnabled(false);
			} 
		});
		
		ToolItem tltmSecondsRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmSecondsRefresh.setEnabled(false);
		tltmSecondsRefresh.setSelection(true);
		tltmSecondsRefresh.setText(Messages.get().SQLAuditEditor_2);
		
	////////////////////////////////////////////////////////////////////////////////////////////////
		Label label = new Label(compositeToolbarHead, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblUserName = new Label(compositeToolbarHead, SWT.NONE);
		
		if(userDAO != null) {
			lblUserName.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
			lblUserName.setText(String.format("%s : %s(%s)", Messages.get().DBListComposite_26, userDAO.getName(), userDAO.getEmail())); //$NON-NLS-1$
		}
		////////////////////////////////////////////////////////////////////////////////////////////////

		Group compositeHead = new Group(parent, SWT.NONE);
		compositeHead.setText(Messages.get().Search);
		GridLayout gl_compositeHead2 = new GridLayout(4, false);
		compositeHead.setLayout(gl_compositeHead2);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblDatabase = new Label(compositeHead, SWT.NONE);
		GridData gd_lblDatabase = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDatabase.widthHint = 65;
		gd_lblDatabase.minimumWidth = 65;
		lblDatabase.setLayoutData(gd_lblDatabase);
		lblDatabase.setText(Messages.get().Database);

		comboDatabase = new Combo(compositeHead, SWT.READ_ONLY);
		GridData gd_comboDisplayName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboDisplayName.minimumWidth = 200;
		gd_comboDisplayName.widthHint = 200;
		comboDatabase.setLayoutData(gd_comboDisplayName);
		
		Label lblTypes = new Label(compositeHead, SWT.NONE);
		GridData gd_lblUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblUser.minimumWidth = 65;
		gd_lblUser.widthHint = 65;
		lblTypes.setLayoutData(gd_lblUser);
		lblTypes.setText(Messages.get().Type);
		
		comboTypes = new Combo(compositeHead, SWT.READ_ONLY);
		GridData gd_comboUserName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboUserName.widthHint = 200;
		gd_comboUserName.minimumWidth = 200;
		comboTypes.setLayoutData(gd_comboUserName);
		for (PublicTadpoleDefine.EXECUTE_SQL_TYPE type : PublicTadpoleDefine.EXECUTE_SQL_TYPE.values()) {
			comboTypes.add(type.name());
		}
		comboTypes.select(0);
		
		Label lblEmail = new Label(compositeHead, SWT.NONE);
		lblEmail.setText(Messages.get().Email);
		
		textEmail = new Text(compositeHead, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});
		new Label(compositeHead, SWT.NONE);
		new Label(compositeHead, SWT.NONE);
		
		Composite compositeInSearch = new Composite(compositeHead, SWT.NONE);
		compositeInSearch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		GridLayout gl_compositeInSearch = new GridLayout(8, false);
		gl_compositeInSearch.verticalSpacing = 1;
		gl_compositeInSearch.horizontalSpacing = 2;
		gl_compositeInSearch.marginHeight = 1;
		gl_compositeInSearch.marginWidth = 2;
		compositeInSearch.setLayout(gl_compositeInSearch);
				
		Label lblDate = new Label(compositeInSearch, SWT.NONE);
		lblDate.setText(Messages.get().Term);
						
		dateTimeStart = new DateTime(compositeInSearch, SWT.BORDER | SWT.DROP_DOWN);
		Label labelTerm = new Label(compositeInSearch, SWT.NONE);
		labelTerm.setText("~"); //$NON-NLS-1$
								
		dateTimeEnd = new DateTime(compositeInSearch, SWT.BORDER | SWT.DROP_DOWN);
												
		Label lblDuring = new Label(compositeInSearch, SWT.RIGHT);
		lblDuring.setText(Messages.get().ExecutedSQLEditor_17);
																
		textMillis = new Text(compositeInSearch, SWT.BORDER | SWT.CENTER);
		GridData gd_textMillis = new GridData(SWT.RIGHT, SWT.RIGHT, false, false, 1, 1);
		gd_textMillis.widthHint = 70;
		textMillis.setLayoutData(gd_textMillis);
		textMillis.setText("50"); //$NON-NLS-1$
		textMillis.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});
		Label lblMilis = new Label(compositeInSearch, SWT.NONE);
		lblMilis.setText(Messages.get().over);
		
		Composite compositeSearchDetail = new Composite(compositeHead, SWT.NONE);
		compositeSearchDetail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		compositeSearchDetail.setLayout(new GridLayout(3, false));
		
		Label lblSQL = new Label(compositeSearchDetail, SWT.NONE);
		lblSQL.setText(Messages.get().Search);
		
		textSearch = new Text(compositeSearchDetail, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL);
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSearch.setMessage(Messages.get().SQL);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});
		
		btnSearch = new Button(compositeSearchDetail, SWT.NONE);
		btnSearch.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/search.png")); //$NON-NLS-1$
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText(Messages.get().Search);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});

		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);

		gridHistory = new Grid(compositeBody, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		gridHistory.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		gridHistory.setLinesVisible(true);
		gridHistory.setHeaderVisible(true);
		gridHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gridHistory.setToolTipText(""); //$NON-NLS-1$

		gridHistory.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				showQueryEditor();
			}
		});
		
		createTableHistoryColumn();
		
		compositeTail = new Composite(compositeBody, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeTail = new GridLayout(2, false);
		gl_compositeTail.verticalSpacing = 2;
		gl_compositeTail.horizontalSpacing = 2;
		gl_compositeTail.marginHeight = 2;
		gl_compositeTail.marginWidth = 2;
		compositeTail.setLayout(gl_compositeTail);
		
		Button btnDownload = new Button(compositeTail, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				download();
			}
		});
		btnDownload.setText(Messages.get().Download);

		btnShowQueryEditor = new Button(compositeTail, SWT.NONE);
		btnShowQueryEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showQueryEditor();
			}
		});
		btnShowQueryEditor.setText(Messages.get().ExecutedSQLEditor_26);

		initUIData();
		registerServiceHandler();
		
		// google analytic
		AnalyticCaller.track(SQLAuditEditor.ID);
	}
	
	/**
	 * download
	 */
	private void download() {
		if(gridHistory.getItemCount() == 0) return;
		if(!MessageDialog.openConfirm(getSite().getShell(), Messages.get().Confirm, Messages.get().ExecutedSQLEditor_28)) return;
			
		List<String[]> listCsvData = new ArrayList<String[]>();
		
		// add header
		listCsvData.add(strArrHeader);
	
		String[] strArryData = new String[gridHistory.getColumnCount()];
		for (int i=0; i<gridHistory.getItemCount(); i++ ) {
			strArryData = new String[gridHistory.getColumnCount()];
			
			GridItem gi = gridHistory.getItem(i);
			for(int intColumnCnt = 0; intColumnCnt<gridHistory.getColumnCount(); intColumnCnt++) {
				strArryData[intColumnCnt] = Utils.convHtmlToLine(gi.getText(intColumnCnt));
			}
			listCsvData.add(strArryData);
		}
		
		try {
			String strCVSContent = CSVFileUtils.makeData(listCsvData);
			downloadExtFile("SQLAudit.csv", strCVSContent); //$NON-NLS-1$
			
			MessageDialog.openInformation(getSite().getShell(), Messages.get().Confirm, Messages.get().ExecutedSQLEditor_31);
		} catch (Exception e) {
			logger.error("Save CSV Data", e); //$NON-NLS-1$
		}		
	}

	/**
	 * show query editor
	 */
	private void showQueryEditor() {
		GridItem[] gridItems = gridHistory.getSelection();
		if(gridItems.length != 0) {
			try {
				RequestResultDAO reqResultDao = mapSQLHistory.get(gridItems[0].getText(0));
				
				if (null != reqResultDao) {
					String strApiOrSQL = gridItems[0].getText(4);
					
					if(reqResultDao.getEXECUSTE_SQL_TYPE() == PublicTadpoleDefine.EXECUTE_SQL_TYPE.API) {
						int intFindAnd = StringUtils.indexOf(strApiOrSQL, "&"); //$NON-NLS-1$
						String strApi = StringUtils.substring(strApiOrSQL, 0, intFindAnd);
						
						UserDBResourceDAO userDBResourceDao = TadpoleSystem_UserDBResource.findAPIKey(strApi);
						String strSQL = TadpoleSystem_UserDBResource.getResourceData(userDBResourceDao);
						if(logger.isDebugEnabled()) logger.debug(userDBResourceDao.getName() + ", " + strSQL); //$NON-NLS-1$
						
						strApiOrSQL = strSQL;
					}
						
					UserDBDAO dbDao = TadpoleSystem_UserDBQuery.getUserDBInstance(reqResultDao.getDbSeq());
					FindEditorAndWriteQueryUtil.run(dbDao, 
								Utils.convHtmlToLine(strApiOrSQL) + PublicTadpoleDefine.SQL_DELIMITER, 
							PublicTadpoleDefine.OBJECT_TYPE.TABLES);

				}
			} catch (Exception e) {
				logger.error("find editor and write query", e); //$NON-NLS-1$
			}
		}
	}
	
	private void clearGrid() {
		GridItem[] gridItems = gridHistory.getItems();
		for (GridItem gridItem : gridItems) {
			gridItem.dispose();
		}
	}

	/**
	 * search
	 */
	private void search() {
		// 기존체 넣었던 데이터를 삭제한다.
		clearGrid();
		mapSQLHistory.clear();
		
		String strEmail = "%" + StringUtils.trim(textEmail.getText()) + "%"; //$NON-NLS-1$ //$NON-NLS-2$
		
		// check all db
		String db_seq = ""; //$NON-NLS-1$
		if (!comboDatabase.getText().equals("All")) { //$NON-NLS-1$
			searchUserDBDAO = (UserDBDAO) comboDatabase.getData(comboDatabase.getText());
			db_seq = ""+searchUserDBDAO.getSeq(); //$NON-NLS-1$
		} else {
			searchUserDBDAO = null;
			for(int i=0; i<listUserDBDAO.size(); i++) {
				UserDBDAO userDB = listUserDBDAO.get(i);
				if(i == (listUserDBDAO.size()-1)) db_seq += (""+userDB.getSeq()); //$NON-NLS-1$
				else db_seq += userDB.getSeq() + ","; //$NON-NLS-1$
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
		long startTime = cal.getTimeInMillis();

		cal.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
		long endTime = cal.getTimeInMillis();
		int duringExecute = 0;
		try {
			duringExecute = Integer.parseInt(textMillis.getText());
		} catch(Exception e) {
			// ignore exception
		}
		
		String strSearchTxt = "%" + StringUtils.trimToEmpty(textSearch.getText()) + "%"; //$NON-NLS-1$ //$NON-NLS-2$

		try {
			List<RequestResultDAO> listSQLHistory = 
					TadpoleSystem_ExecutedSQL.getExecuteQueryHistoryDetail(strEmail, comboTypes.getText(), db_seq, startTime, endTime, duringExecute, strSearchTxt);
//			for (RequestResultDAO reqResultDAO : listSQLHistory) {
			for (int i=0; i<listSQLHistory.size(); i++) {
				RequestResultDAO reqResultDAO = (RequestResultDAO)listSQLHistory.get(i);
				mapSQLHistory.put(""+i, reqResultDAO); //$NON-NLS-1$
				
				GridItem item = new GridItem(gridHistory, SWT.V_SCROLL | SWT.H_SCROLL);
				
				String strSQL = StringUtils.strip(reqResultDAO.getStrSQLText());
				int intLine = StringUtils.countMatches(strSQL, "\n"); //$NON-NLS-1$
				if(intLine >= 1) {
					int height = (intLine+1) * 24;
					if(height > 100) item.setHeight(100); 
					else item.setHeight(height);
				}
				
				item.setText(0, ""+i); //$NON-NLS-1$
				item.setText(1, reqResultDAO.getDbName());
				item.setText(2, reqResultDAO.getUserName());
				item.setText(3, TimeZoneUtil.dateToStr(reqResultDAO.getStartDateExecute()));
//				logger.debug(Utils.convLineToHtml(strSQL));
				item.setText(4, Utils.convLineToHtml(strSQL));
				item.setToolTipText(4, strSQL);
				
				try {
					item.setText(5, ""+( (reqResultDAO.getEndDateExecute().getTime() - reqResultDAO.getStartDateExecute().getTime()) )); //$NON-NLS-1$
				} catch(Exception e) {
					item.setText(5, "-"); //$NON-NLS-1$
				}
				item.setText(6, ""+reqResultDAO.getRows()); //$NON-NLS-1$
				item.setText(7, reqResultDAO.getResult());
				
				item.setText(8, Utils.convLineToHtml(reqResultDAO.getMesssage()));
				item.setToolTipText(8, reqResultDAO.getMesssage());
				
				item.setText(9, reqResultDAO.getIpAddress());
				
				if("F".equals(reqResultDAO.getResult())) { //$NON-NLS-1$
					item.setBackground(SWTResourceManager.getColor(240, 180, 167));
				}
			}
		} catch (Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * 데이터를 초기 로드합니다.
	 */
	private void initUIData() {

		try {
			if(roleType == USER_ROLE_TYPE.SYSTEM_ADMIN) {
				listUserDBDAO = TadpoleSystem_UserDBQuery.getUserDB(userDAO);
			} else {
				userDAO = new UserDAO();
				userDAO.setSeq(SessionManager.getUserSeq());
				listUserDBDAO = TadpoleSystem_UserDBQuery.getUserDB(userDAO);
			}
			
			comboDatabase.add("All"); //$NON-NLS-1$
			comboDatabase.setData("All", null); //$NON-NLS-1$
			
			for (UserDBDAO userDBDAO : listUserDBDAO) {
				comboDatabase.add(userDBDAO.getDisplay_name());
				comboDatabase.setData(userDBDAO.getDisplay_name(), userDBDAO);
			}
			comboDatabase.setVisibleItemCount(listUserDBDAO.size()+1);
			comboDatabase.select(0);

		} catch (Exception e) {
			logger.error("get db list", e); //$NON-NLS-1$
		}
		// Range of date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		callbackui();
	}
	
	private void callbackui() {
		pushSession.start();
		Thread thread = new Thread(startUIThread());
		thread.setDaemon(true);
		thread.start();
	}
	
	private Runnable startUIThread() {
		final String email = SessionManager.getEMAIL();
		final Display display = PlatformUI.getWorkbench().getDisplay();// tvError.getTable().getDisplay();

		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {

				while(isThread) {
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							if(isNotRefreshUi) search();
						}
					});
					
					// 20 seconds
					try{ Thread.sleep(1000 * 30); } catch(Exception e) {}
				}
			};
		};

		return bgRunnable;
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

		SQLAuditEditorInput esqli = (SQLAuditEditorInput) input;
		setPartName(esqli.getName());

		this.userDAO = esqli.getUserDAO();
		this.userDBDAO = esqli.getUserDBDAO();
		this.roleType = esqli.getRoleType();
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
		
		DownloadUtils.provideDownload(compositeTail, downloadServiceHandler.getId());
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	@Override
	public void dispose() {
		unregisterServiceHandler();

		isThread = false;
		pushSession.stop();
		super.dispose();
	}

	/**
	 * grid column
	 */
	private void createTableHistoryColumn() {
		
		int[] intColumnAlgin = {SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.LEFT, SWT.LEFT};
		int[] intColumnWidth = {50, 150, 100, 150, 300, 60, 60, 90, 250, 80};
		
		for (int i=0; i<intColumnAlgin.length; i++) {
			GridColumn tvcIP= new GridColumn(gridHistory, intColumnAlgin[i]);
			tvcIP.setWidth(intColumnWidth[i]);
			tvcIP.setText(strArrHeader[i]);	
		}
		
	}
}
