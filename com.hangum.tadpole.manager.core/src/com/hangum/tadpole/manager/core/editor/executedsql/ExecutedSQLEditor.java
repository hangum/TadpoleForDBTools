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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * 실행한 쿼리.
 * 
 * 1. Name 콤보에 보여줄때는 사람을 기준으로 보여줄
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
	/** 마지막 검색시 사용하는 UserDBDAO */
	private UserDBDAO searchUserDBDAO = null;

	/** 제일 처음 설정 될때 사용하는 dao */
	private UserDAO userDAO;
	private UserDBDAO userDBDAO;
	
	/** 사용자 db list */
	private List<UserDBDAO> listUserDBDAO;

	private Combo comboDatabase;
	private Combo comboUser;
	
	private Text textMillis;
	private Grid gridHistory;

	private Button btnSearch;
	private Button btnShowQueryEditor;

	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;

	/** result list */
	private Text textSearch;
//	private SQLHistoryFilter filter;
	
	/** register key */
	private Map<String, SQLHistoryDAO> mapSQLHistory = new HashMap<String, SQLHistoryDAO>();
	
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
		parent.setLayout(gl_parent);

		Group compositeHead = new Group(parent, SWT.NONE);
		compositeHead.setText("Search");
		GridLayout gl_compositeHead = new GridLayout(2, false);
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblDatabase = new Label(compositeHead, SWT.NONE);
		GridData gd_lblDatabase = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDatabase.widthHint = 65;
		gd_lblDatabase.minimumWidth = 65;
		lblDatabase.setLayoutData(gd_lblDatabase);
		lblDatabase.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblDatabase.setText("<b>Database</b>");

		comboDatabase = new Combo(compositeHead, SWT.READ_ONLY);
		GridData gd_comboDisplayName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboDisplayName.minimumWidth = 200;
		gd_comboDisplayName.widthHint = 200;
		comboDatabase.setLayoutData(gd_comboDisplayName);
		
//		Label lblUser = new Label(compositeHead, SWT.NONE);
//		lblUser.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
//		GridData gd_lblUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//		gd_lblUser.minimumWidth = 65;
//		gd_lblUser.widthHint = 65;
//		lblUser.setLayoutData(gd_lblUser);
//		lblUser.setText("<b>User</b>");
//
//		comboUser = new Combo(compositeHead, SWT.READ_ONLY);
//		GridData gd_comboUserName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
//		gd_comboUserName.widthHint = 200;
//		gd_comboUserName.minimumWidth = 200;
//		comboUser.setLayoutData(gd_comboUserName);
		
		Composite compositeInSearch = new Composite(compositeHead, SWT.NONE);
		compositeInSearch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		GridLayout gl_compositeInSearch = new GridLayout(8, false);
		gl_compositeInSearch.verticalSpacing = 1;
		gl_compositeInSearch.horizontalSpacing = 2;
		gl_compositeInSearch.marginHeight = 1;
		gl_compositeInSearch.marginWidth = 2;
		compositeInSearch.setLayout(gl_compositeInSearch);
				
		Label lblDate = new Label(compositeInSearch, SWT.NONE);
		lblDate.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblDate.setText("<b>Search date</b>");
						
		dateTimeStart = new DateTime(compositeInSearch, SWT.BORDER | SWT.DROP_DOWN);
		Label label = new Label(compositeInSearch, SWT.NONE);
		label.setText("~");
								
		dateTimeEnd = new DateTime(compositeInSearch, SWT.BORDER | SWT.DROP_DOWN);
		
		Label label_1 = new Label(compositeInSearch, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
												
		Label lblDuring = new Label(compositeInSearch, SWT.RIGHT);
		lblDuring.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblDuring.setText("<b>During execute</b>");
																
		textMillis = new Text(compositeInSearch, SWT.BORDER | SWT.CENTER);
		GridData gd_textMillis = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_textMillis.widthHint = 70;
		textMillis.setLayoutData(gd_textMillis);
		textMillis.setText("500");
																				
		Label lblMilis = new Label(compositeInSearch, SWT.NONE);
		lblMilis.setText("(ms)");
		
		Label lblSQL = new Label(compositeHead, SWT.NONE);
		GridData gd_lblSQL = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSQL.widthHint = 65;
		gd_lblSQL.minimumWidth = 65;
		lblSQL.setLayoutData(gd_lblSQL);
		lblSQL.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblSQL.setText("<b>SQL</b>");
		
		textSearch = new Text(compositeHead, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL);
		textSearch.setMessage("Filter");
		textSearch.setToolTipText("After entering a value, press the Enter key.");
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});
		GridData gd_textSearch = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textSearch.heightHint = 20;
		textSearch.setLayoutData(gd_textSearch);
		
		new Label(compositeHead, SWT.NONE);
		btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSearch.setImage(ResourceManager.getPluginImage("com.hangum.tadpole.manager.core", "resources/icons/search.png"));
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText("Search");

		Composite compositeSearch = new Composite(parent, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(1, false);
		gl_compositeSearch.marginHeight = 2;
		gl_compositeSearch.verticalSpacing = 1;
		gl_compositeSearch.horizontalSpacing = 1;
		gl_compositeSearch.marginWidth = 1;
		compositeSearch.setLayout(gl_compositeSearch);

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
		gridHistory.setToolTipText("");

		gridHistory.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				showQueryEditor();
			}
		});
		
		createTableHistoryColumn();
		
		Composite compositeTail = new Composite(compositeBody, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeTail = new GridLayout(1, false);
		gl_compositeTail.verticalSpacing = 2;
		gl_compositeTail.horizontalSpacing = 2;
		gl_compositeTail.marginHeight = 2;
		gl_compositeTail.marginWidth = 2;
		compositeTail.setLayout(gl_compositeTail);

		btnShowQueryEditor = new Button(compositeTail, SWT.NONE);
		GridData gd_btnShowQueryEditor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnShowQueryEditor.minimumHeight = 25;
		gd_btnShowQueryEditor.heightHint = 25;
		gd_btnShowQueryEditor.minimumWidth = 220;
		gd_btnShowQueryEditor.widthHint = 220;
		btnShowQueryEditor.setLayoutData(gd_btnShowQueryEditor);
		btnShowQueryEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showQueryEditor();
			}
		});
		btnShowQueryEditor.setText("Show query in the Query editor");

		initUIData();
		
		// google analytic
		AnalyticCaller.track(ExecutedSQLEditor.ID);
	}

	/**
	 * show query editor
	 */
	private void showQueryEditor() {
		GridItem[] gridItems = gridHistory.getSelection();
		if(gridItems.length != 0) {
			try {
				SQLHistoryDAO sqlHistoryDao = mapSQLHistory.get(gridItems[0].getText(0));
				if (null != sqlHistoryDao) {
					UserDBDAO dbDao = TadpoleSystem_UserDBQuery.getUserDBInstance(sqlHistoryDao.getDbSeq());
					FindEditorAndWriteQueryUtil.run(dbDao, 
								Utils.convHtmlToLine(gridItems[0].getText(3)) + PublicTadpoleDefine.SQL_DELIMITER, 
								PublicTadpoleDefine.DB_ACTION.TABLES);
				}
			} catch (Exception e) {
				logger.error("find editor and write query", e);
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
		
		// check all db
		String db_seq = "";
		if (!comboDatabase.getText().equals("All")) {
			searchUserDBDAO = (UserDBDAO) comboDatabase.getData(comboDatabase.getText());
			db_seq = ""+searchUserDBDAO.getSeq();
		} else {
			searchUserDBDAO = null;
			for(int i=0; i<listUserDBDAO.size(); i++) {
				UserDBDAO userDB = listUserDBDAO.get(i);
				if(i == (listUserDBDAO.size()-1)) db_seq += (""+userDB.getSeq());
				else db_seq += userDB.getSeq() + ",";
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
		long startTime = cal.getTimeInMillis();

		cal.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
		long endTime = cal.getTimeInMillis();
		int duringExecute = Integer.parseInt(textMillis.getText());
		
		String strSearchTxt = "%" + StringUtils.trimToEmpty(textSearch.getText()) + "%";

		try {
			List<SQLHistoryDAO> listSQLHistory = TadpoleSystem_ExecutedSQL.getExecuteQueryHistoryDetail(db_seq, startTime, endTime, duringExecute, strSearchTxt);
			for (SQLHistoryDAO sqlHistoryDAO : listSQLHistory) {
				mapSQLHistory.put(""+gridHistory.getRootItemCount(), sqlHistoryDAO);
				
				GridItem item = new GridItem(gridHistory, SWT.V_SCROLL | SWT.H_SCROLL);
				
				String strSQL = StringUtils.strip(sqlHistoryDAO.getStrSQLText());
				int intLine = StringUtils.countMatches(strSQL, "\n");
				if(intLine >= 1) {
					int height = (intLine+1) * 24;
					if(height > 100) item.setHeight(100); 
					else item.setHeight(height);
				}
				
				item.setText(0, ""+gridHistory.getRootItemCount());
				item.setText(1, sqlHistoryDAO.getDbName());
				item.setText(2, Utils.dateToStr(sqlHistoryDAO.getStartDateExecute()));
				logger.debug(Utils.convLineToHtml(strSQL));
				item.setText(3, Utils.convLineToHtml(strSQL));
				item.setToolTipText(3, strSQL);
				
				item.setText(4, ""+( (sqlHistoryDAO.getEndDateExecute().getTime() - sqlHistoryDAO.getStartDateExecute().getTime()) / 1000f));
				item.setText(5, ""+sqlHistoryDAO.getRows());
				item.setText(6, sqlHistoryDAO.getResult());
				
				item.setText(7, Utils.convLineToHtml(sqlHistoryDAO.getMesssage()));
				item.setToolTipText(7, sqlHistoryDAO.getMesssage());
				
				
				item.setText(8, sqlHistoryDAO.getUserName());
				item.setText(9, sqlHistoryDAO.getIpAddress());
				
				if("F".equals(sqlHistoryDAO.getResult())) {
					item.setBackground(SWTResourceManager.getColor(240, 180, 167));
				} else {
					item.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
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
			// database name combo
			listUserDBDAO = TadpoleSystem_UserDBQuery.getUserDB();
			comboDatabase.add("All");
			comboDatabase.setData("All", null);
			
			for (UserDBDAO userDBDAO : listUserDBDAO) {
				comboDatabase.add(userDBDAO.getDisplay_name());
				comboDatabase.setData(userDBDAO.getDisplay_name(), userDBDAO);
			}
			comboDatabase.select(0);

		} catch (Exception e) {
			logger.error("get db list", e);
		}
		// Range of date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
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

		ExecutedSQLEditorInput esqli = (ExecutedSQLEditorInput) input;
		setPartName(esqli.getName());

		this.userDAO = esqli.getUserDAO();
		this.userDBDAO = esqli.getUserDBDAO();
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

	/**
	 * grid column
	 */
	private void createTableHistoryColumn() {
		// time
		GridColumn tvcSeq = new GridColumn(gridHistory, SWT.LEFT);
		tvcSeq.setWidth(50);
		tvcSeq.setText("#");
		
		GridColumn tvcDatabase = new GridColumn(gridHistory, SWT.NONE);
		tvcDatabase.setWidth(150);
		tvcDatabase.setText("Database");
				
		// time
		GridColumn tvcDate = new GridColumn(gridHistory, SWT.LEFT);
		tvcDate.setWidth(150);
		tvcDate.setText("Date");
		
		// sql
		GridColumn tvcSQL = new GridColumn(gridHistory, SWT.LEFT);
		tvcSQL.setWidth(300);
		tvcSQL.setText("SQL");
		tvcSQL.setWordWrap(true);

		// duration
		GridColumn tvcDuration = new GridColumn(gridHistory, SWT.RIGHT);
		tvcDuration.setWidth(60);
		tvcDuration.setText("Sec");
		
		// rows
		GridColumn tvcRows = new GridColumn(gridHistory, SWT.RIGHT);
		tvcRows.setWidth(60);
		tvcRows.setText("Rows");
		
		// result
		GridColumn tvcResult = new GridColumn(gridHistory, SWT.NONE);
		tvcResult.setWidth(90);
		tvcResult.setText("Result");

		GridColumn tvcMessage = new GridColumn(gridHistory, SWT.NONE);
		tvcMessage.setWidth(250);
		tvcMessage.setText("Message");
		
		GridColumn tvcUser = new GridColumn(gridHistory, SWT.NONE);
		tvcUser.setWidth(80);
		tvcUser.setText("User");
		
		GridColumn tvcIP= new GridColumn(gridHistory, SWT.NONE);
		tvcIP.setWidth(80);
		tvcIP.setText("IP");
		
	}
}
