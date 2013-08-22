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
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.swtdesigner.SWTResourceManager;

/**
 * Login dialog
 * 
 * @author hangum
 * 
 */
public class DBLoginDialog extends Dialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1327678815994219469L;
	private static final Logger logger = Logger.getLogger(DBLoginDialog.class);
	
	
	public static final int TEST_CONNECTION_ID = IDialogConstants.CLIENT_ID + 1;
	
	/** main composite */
	private Composite container;
	
	/** group name */
	protected List<String> groupName;
	/** 초기 선택한 그룹 */
	private String selGroupName;
	
	private Combo comboDBList;
	private Composite compositeBody;

	private AbstractLoginComposite loginComposite;
//	private Group grpLoginHistory;
//	
//	private List<UserDBDAO> listTreeData = new ArrayList<UserDBDAO>();
//	private TreeViewer treeViewerLoginData;

	// 결과셋으로 사용할 logindb
	private UserDBDAO retuserDb;
	
//	// delete button id
//	public final int DELETE_BTN_ID = 99999;

	public DBLoginDialog(Shell paShell, String selGroupName) {
		super(paShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.selGroupName = selGroupName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Database Connection"); //$NON-NLS-1$
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		
		SashForm sashFormContainer = new SashForm(container, SWT.VERTICAL);
		sashFormContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeHead = new Composite(sashFormContainer, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 3;
		gl_compositeHead.horizontalSpacing = 3;
		gl_compositeHead.marginHeight = 3;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.DBLoginDialog_35);

		comboDBList = new Combo(compositeHead, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboDBList.setBackground(SWTResourceManager.getColor(255, 250, 205));
		comboDBList.setVisibleItemCount(10);
		comboDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
//				thisWorkType = WORK_TYPE.INSERT;				
				initDBWidget(null);
			}
		});
		comboDBList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// 초기데이터 추가
		for (DBDefine dbDefine : DBDefine.userDBValues()) {
			comboDBList.add(dbDefine.getDBToString());
			comboDBList.setData(dbDefine.getDBToString(), dbDefine);
		}
		comboDBList.select(3);
				
		// combo에서 선택된 디비의 콤포짖
		compositeBody = new Composite(compositeHead, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		// db groupData 
		try {
			groupName = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getGroupSeqs());
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		
		createDBWidget(null);

		// history .....................................
//		createHistory(sashFormContainer);
//		sashFormContainer.setWeights(new int[] {73, 27});
		sashFormContainer.setWeights(new int[] {1});

		
		comboDBList.setFocus();
		
		return container;
	}
	
	/**
	 * db widget을 설정한다.
	 * @param userDB
	 */
	private void initDBWidget(UserDBDAO userDB) {
		if (loginComposite != null)loginComposite.dispose();

		createDBWidget(userDB);
		compositeBody.layout();
		container.layout();
	}
	
	/**
	 * db widget을 생성한다.
	 */
	private void createDBWidget(UserDBDAO userDB) {
		
		DBDefine dbDefine = (DBDefine) comboDBList.getData(comboDBList.getText());
		loginComposite = DBConnection.getDBConnection(dbDefine, compositeBody, groupName, selGroupName, userDB);
	}

//	/**
//	 * login history
//	 * 
//	 * @param container
//	 */
//	private void createHistory(Composite container) {
//		grpLoginHistory = new Group(container, SWT.NONE);
//		grpLoginHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		grpLoginHistory.setText(Messages.DBLoginDialog_grpHistory_text);
//		grpLoginHistory.setLayout(new GridLayout(1, false));
//		
//		Composite compositeHistory = new Composite(grpLoginHistory, SWT.NONE);
//		compositeHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		TreeColumnLayout tcl_compositeHistory = new TreeColumnLayout();
//		compositeHistory.setLayout(tcl_compositeHistory);
//		
//		treeViewerLoginData = new TreeViewer(compositeHistory, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
//		treeViewerLoginData.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				// 선택이 될때마다 로그인창의 화면에 정보를 출력합니다.
//				IStructuredSelection iss = (IStructuredSelection)event.getSelection();
//				UserDBDAO userDB = (UserDBDAO)iss.getFirstElement();
//			
//				if(userDB != null) {
//					comboDBList.setText(userDB.getTypes());
//					initDBWidget(userDB);
//				}
//			}
//		});
//		Tree tree = treeViewerLoginData.getTree();
//		tree.setHeaderVisible(true);
//		tree.setLinesVisible(true);
//		
//		TreeViewerColumn tvcGroup = new TreeViewerColumn(treeViewerLoginData, SWT.NONE);
//		TreeColumn trclmnGroupName = tvcGroup.getColumn();
//		tcl_compositeHistory.setColumnData(trclmnGroupName, new ColumnPixelData(106, true, true));
//		trclmnGroupName.setText(Messages.DBLoginDialog_trclmnGroupName_text);
//		
//		TreeViewerColumn tvcDisplayName = new TreeViewerColumn(treeViewerLoginData, SWT.NONE);
//		TreeColumn trclmnDisplayName = tvcDisplayName.getColumn();
//		tcl_compositeHistory.setColumnData(trclmnDisplayName, new ColumnPixelData(111, true, true));
//		trclmnDisplayName.setText(Messages.DBLoginDialog_trclmnDisplayName_text);
//		
//		TreeViewerColumn tvcURL = new TreeViewerColumn(treeViewerLoginData, SWT.NONE);
//		TreeColumn trclmnURL = tvcURL.getColumn();
//		tcl_compositeHistory.setColumnData(trclmnURL, new ColumnPixelData(240, true, true));
//		trclmnURL.setText(Messages.DBLoginDialog_trclmnNewColumn_text);
//		
//		TreeViewerColumn tvcUser = new TreeViewerColumn(treeViewerLoginData, SWT.NONE);
//		TreeColumn trclmnUSer = tvcUser.getColumn();
//		tcl_compositeHistory.setColumnData(trclmnUSer, new ColumnPixelData(100, true, true));
//		trclmnUSer.setText(Messages.DBLoginDialog_37);
//		
//		treeViewerLoginData.setContentProvider(new LoginContentProvider());
//		treeViewerLoginData.setLabelProvider(new LoginLabelProvider());
//		makeHistoryData();
//	}

//	/**
//	 * 입력된 디비 정보 가공
//	 * 
//	 * @return
//	 */
//	private void makeHistoryData() {
//		listTreeData.clear();
//		
//		try {
//			// groupName 기준으로 그룹을 만든다.
//			List<String> groupNames = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getSeq());
//			List<UserDBDAO> listUserDB = TadpoleSystem_UserDBQuery.getUserDB();
//			Map<String, List<UserDBDAO>> mapGroupUserDB = new HashMap<String, List<UserDBDAO>>();
//			
//			for(String groupName: groupNames) {
//				List listGrp = new ArrayList<UserDBDAO>();
//				mapGroupUserDB.put(groupName, listGrp);
//				
//				for (UserDBDAO userDB : listUserDB) {
//					if(groupName.equals(userDB.getGroup_name())) listGrp.add(userDB);
//				}	// end for
//			}
//			
//			// listTreeData에 그룹을 넣는다.
//			for(String groupName: groupNames) {
//				List<UserDBDAO> calcUserDB = mapGroupUserDB.get(groupName);
//				UserDBDAO groupHead = calcUserDB.get(0);
//				for(int i=1; i<calcUserDB.size(); i++) {
//					groupHead.getListUserDBGroup().add(calcUserDB.get(i));
//				}				
//				listTreeData.add(groupHead);
//			}
//			
//		} catch (Exception e) {
//			logger.error("initialize DBLogin", e); //$NON-NLS-1$
//			
//			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ManagerViewer_4, errStatus); //$NON-NLS-1$
//		}
//		
//		treeViewerLoginData.setInput(listTreeData);
//	}
//	
//	/**
//	 * 
//	 * @param userDb
//	 */
//	private void selectDatabase(UserDBDAO userDb) {
////		if (loginComposite.connectValite(userDb, userDb.getDb())) {
//			this.retuserDb = userDb;
//
//			super.okPressed();
////		}
//	}

	@Override
	protected void okPressed() {
		if (!loginComposite.connection()) return;
		this.retuserDb = loginComposite.getDBDTO();
		
		super.okPressed();
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(TEST_CONNECTION_ID == buttonId) {
			if(loginComposite.testConnection()) {
				MessageDialog.openInformation(null, "Confirm", "Connection Successful.");
			}
		}
	}

	public UserDBDAO getDTO() {
		return retuserDb;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, DELETE_BTN_ID, Messages.DBLoginDialog_button_text, false);
		
		createButton(parent, TEST_CONNECTION_ID, "Test Connection", false);
		createButton(parent, IDialogConstants.OK_ID, Messages.DBLoginDialog_6, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.DBLoginDialog_7, false);
	}
	
//	@Override
//	protected void buttonPressed(int buttonId) {
//		super.buttonPressed(buttonId);
//		
//		if(buttonId == DELETE_BTN_ID) {
//			IStructuredSelection ss = (IStructuredSelection) treeViewerLoginData.getSelection();
//			if(!ss.isEmpty()) {
//				UserDBDAO userDb = (UserDBDAO) ss.getFirstElement();
//				
//				// 자신이 등록한 것만 삭제하도록
//				if(SessionManager.getSeq() == userDb.getUser_seq()) {
//					
//					if(MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.DBLoginDialog_9, Messages.DBLoginDialog_28)) {
//						try {
//							
//							TadpoleSystem_UserDBQuery.removeUserDB(userDb.getSeq());							
//							makeHistoryData();
//						} catch(Exception e) {
//							logger.error(Messages.DBLoginDialog_32, e);
//							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//							ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.DBLoginDialog_29, errStatus); //$NON-NLS-1$
//						}						
//					}	// end confirm
//					
//				}	// end if 자신의 등록한 것
//				else {
//					MessageDialog.openInformation(null, Messages.DBLoginDialog_39, Messages.DBLoginDialog_40);//자신이 등록한 것만 삭제 할수 있습니다.");
//				}
//			}	// end select
//		}	// end delete button
//	}
	
	/**
	 * group name
	 * 
	 * @return
	 */
	public List<String> getGroupName() {
		return groupName;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 540);
	}
}

///**
// * login data content provider
// * @author hangum
// *
// */
//class LoginContentProvider extends ArrayContentProvider implements ITreeContentProvider {
//
//	@Override
//	public Object[] getChildren(Object parentElement) {
//		UserDBDAO listUserDBDAO = (UserDBDAO)parentElement;
//		return listUserDBDAO.getListUserDBGroup().toArray();
//	}
//
//	@Override
//	public Object getParent(Object element) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean hasChildren(Object element) {
//		UserDBDAO groupDao = (UserDBDAO)element;
//		return groupDao.getListUserDBGroup().size() > 0;				
//	}	
//}
//
///**
// * login data label provider
// * @author hangum
// *
// */
//class LoginLabelProvider extends LabelProvider implements ITableLabelProvider {
//
//	@Override
//	public Image getColumnImage(Object element, int columnIndex) {
//		if(columnIndex == 1) {
//			UserDBDAO dto = (UserDBDAO)element;
//			DBDefine dbType = DBDefine.getDBDefine(dto.getTypes());
//			
//			if(DBDefine.MYSQL_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mysql-add.png"); //$NON-NLS-1$
//			
//			else if(DBDefine.ORACLE_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/oracle-add.png"); //$NON-NLS-1$
//			
//			else if(DBDefine.SQLite_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sqlite-add.png"); //$NON-NLS-1$
//			
//			else if(DBDefine.MSSQL_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mssql-add.png"); //$NON-NLS-1$
//			
//			else if(DBDefine.CUBRID_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/cubrid-add.png"); //$NON-NLS-1$
//			
//			else if(DBDefine.POSTGRE_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/postgresSQL-add.png"); //$NON-NLS-1$
//			
//			else if(DBDefine.MONGODB_DEFAULT == dbType) 
//				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mongodb-add.png"); //$NON-NLS-1$
//		}
//		
//		return null;
//	}
//
//	@Override
//	public String getColumnText(Object element, int columnIndex) {
//		UserDBDAO dto = (UserDBDAO)element;
//		
//		switch(columnIndex) {
//		case 0: return dto.getGroup_name();
//		case 1: return dto.getDisplay_name();
//		case 2: return dto.getUrl();
//		case 3: return dto.getUsers();
//		}
//		
//		return "*** not set column ***"; //$NON-NLS-1$
//	}
//	
//}
