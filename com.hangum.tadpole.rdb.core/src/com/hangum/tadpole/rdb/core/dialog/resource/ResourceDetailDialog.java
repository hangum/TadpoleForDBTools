/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleCompareWidget;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.RESOURCE_TYPE;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Resource history dialog
 * 
 * @author hangum
 *
 */
public class ResourceDetailDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResourceDetailDialog.class);
	
	private int MODIFY_ID = IDialogConstants.CLIENT_ID + 1;
	
	private UserDBResourceDAO originalResourceDB;
	private ResourceManagerDAO resourceManagerDao;
	private Text textUser;
	private Text textTitle;
	private Combo comboSharedType;
	private Text textDescription;
	private Combo comboUseAPI;
	private Text textCreateTime;
	private TadpoleEditorWidget textSQL;
	private TableViewer tvHistory;
	private TadpoleCompareWidget compareWidget;
	private Text textAPIURL;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param resourceManagerDao 
	 * @param resourceDB 
	 */
	public ResourceDetailDialog(Shell parentShell, ResourceManagerDAO resourceManagerDao, UserDBResourceDAO resourceDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

		this.resourceManagerDao = resourceManagerDao;
		this.originalResourceDB = resourceDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ResourceHistoryDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblTitle = new Label(compositeHead, SWT.NONE);
		lblTitle.setText(Messages.get().Title);
		
		textTitle = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY);
		textTitle.setEditable(true);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTitle.setText(originalResourceDB.getName());
		
		Label lblSharedType = new Label(compositeHead, SWT.NONE);
		lblSharedType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSharedType.setText("Shared type");
		
		comboSharedType = new Combo(compositeHead, SWT.READ_ONLY);
		comboSharedType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.SHARED_TYPE type : PublicTadpoleDefine.SHARED_TYPE.values()) {
			comboSharedType.add(type.toString());
		}
		comboSharedType.setText(originalResourceDB.getShared_type());
		
		Label lblDescription = new Label(compositeHead, SWT.NONE);
		lblDescription.setText(Messages.get().Description);
		
		textDescription = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textDescription.setEditable(true);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 40;
		textDescription.setLayoutData(gd_textDescription);
		textDescription.setText(originalResourceDB.getDescription());

		Composite compositeHeaderUser = new Composite(compositeHead, SWT.NONE);
		compositeHeaderUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		compositeHeaderUser.setLayout(new GridLayout(4, false));
		
		Label lblApi = new Label(compositeHeaderUser, SWT.NONE);
		lblApi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblApi.setText(Messages.get().ISApiUse);
		
		comboUseAPI = new Combo(compositeHeaderUser, SWT.READ_ONLY);
		comboUseAPI.add(PublicTadpoleDefine.YES_NO.YES.name());
		comboUseAPI.add(PublicTadpoleDefine.YES_NO.NO.name());
		comboUseAPI.setText(originalResourceDB.getRestapi_yesno());
		comboUseAPI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApiUrl = new Label(compositeHeaderUser, SWT.NONE);
		lblApiUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblApiUrl.setText("API URL");
		
		textAPIURL = new Text(compositeHeaderUser, SWT.BORDER);
		textAPIURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textAPIURL.setText(originalResourceDB.getRestapi_uri());
		
		Label lblUser = new Label(compositeHeaderUser, SWT.NONE);
		lblUser.setText(Messages.get().User);
		
		textUser = new Text(compositeHeaderUser, SWT.BORDER | SWT.READ_ONLY);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUser.setText(resourceManagerDao.getUser_name());
		
		Label lblCreateTime = new Label(compositeHeaderUser, SWT.NONE);
		lblCreateTime.setText(Messages.get().CreatTime);
		
		textCreateTime = new Text(compositeHeaderUser, SWT.BORDER | SWT.READ_ONLY);
		textCreateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textCreateTime.setText(resourceManagerDao.getCreate_time());
		
		CTabFolder tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		CTabItem tabSQL = new CTabItem(tabFolder, SWT.NONE);
		tabSQL.setText("SQL");
		
		Composite compositeSQL = new Composite(tabFolder, SWT.NONE);
		tabSQL.setControl(compositeSQL);
		compositeSQL.setLayout(new GridLayout(1, false));
		
		textSQL = new TadpoleEditorWidget(compositeSQL, SWT.BORDER, EditorDefine.EXT_DEFAULT, originalResourceDB.getDataString(), "");
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tabHistory = new CTabItem(tabFolder, SWT.NONE);
		tabHistory.setText("History");
		
		SashForm sashForm = new SashForm(tabFolder, SWT.BORDER | SWT.VERTICAL);
		tabHistory.setControl(sashForm);
		sashForm.setLayout(new GridLayout(1, false));
		
		tvHistory = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tvHistory.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection sss = (StructuredSelection)tvHistory.getSelection();
				if(sss.isEmpty()) return;
				
				UserDBResourceDataDAO userDBResource = (UserDBResourceDataDAO)sss.getFirstElement();
				compareWidget.changeDiff(userDBResource.getDatas(), "");
			}
		});
		Table table = tvHistory.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tvcDate = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnDate = tvcDate.getColumn();
		tblclmnDate.setWidth(100);
		tblclmnDate.setText(Messages.get().Date);
		
		TableViewerColumn tvcUser = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnUser = tvcUser.getColumn();
		tblclmnUser.setWidth(100);
		tblclmnUser.setText(Messages.get().User);
		
		TableViewerColumn tvcSQL = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnSql = tvcSQL.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText(Messages.get().SQL);
		
		tvHistory.setContentProvider(new ArrayContentProvider());
		tvHistory.setLabelProvider(new ResourceHistoryLabelProvider());
		
		compareWidget = new TadpoleCompareWidget(sashForm, SWT.BORDER);
		compareWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnCompare = new Button(compareWidget, SWT.NONE);
		btnCompare.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCompare.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compare();
			}
		});
		btnCompare.setText(Messages.get().Compare);
		
		sashForm.setWeights(new int[] {6, 4});
		tabFolder.setSelection(0);
		
		initUIData();
		
		return container;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == MODIFY_ID) {
			if(MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().ResourceDetailDialog_delete)) {
				resourceManagerDao.setName(textTitle.getText());
				resourceManagerDao.setDescription(textDescription.getText());
				resourceManagerDao.setShared_type(comboSharedType.getText());
				resourceManagerDao.setRestapi_yesno(comboUseAPI.getText());
				resourceManagerDao.setRestapi_uri(textAPIURL.getText());
				
				if(comboUseAPI.getText().equals(PublicTadpoleDefine.YES_NO.YES.name()) && "".equals(resourceManagerDao.getRestapi_key())) { //$NON-NLS-1$
					resourceManagerDao.setRestapi_key(Utils.getUniqueID());	
				}
				
				try {
					if(!isValid(resourceManagerDao)) return;
					
					TadpoleSystem_UserDBResource.updateResourceHeader(resourceManagerDao);

					// tree refresh
					if(originalResourceDB != null) {
						originalResourceDB.setName(resourceManagerDao.getName());
						originalResourceDB.setDescription(resourceManagerDao.getDescription());
						originalResourceDB.setShared_type(resourceManagerDao.getShared_type());
						
						originalResourceDB.setRestapi_yesno(resourceManagerDao.getRestapi_yesno());
						originalResourceDB.setRestapi_uri(resourceManagerDao.getRestapi_uri());
						
						ManagerViewer mv = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ManagerViewer.ID);
						mv.refreshResource(originalResourceDB);
					}
				} catch(Exception ee) {
					logger.error("Resource title, desc saveing", ee);
					MessageDialog.openError(getShell(), Messages.get().Confirm, "Save exception." + ee.getMessage());
				}
			}
		}
		super.buttonPressed(buttonId);
	}
	
	/**
	 * is valid
	 * @return
	 */
	private boolean isValid(ResourceManagerDAO dao) {
		int len = StringUtils.trimToEmpty(textTitle.getText()).length();
		if(len < 3) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().ResourceManageEditor_27); //$NON-NLS-1$
			textTitle.setFocus();
			return false;
		}

		// sql type 
		if(dao.getResource_types().equals(RESOURCE_TYPE.SQL.name())) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(dao.getRestapi_yesno())) {
				String strAPIURI = textAPIURL.getText().trim();
				
				if(strAPIURI.equals("")) { //$NON-NLS-1$
					MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ResourceManageEditor_30);
					textAPIURL.setFocus();
					return false;
				}
				
				// check valid url. url pattern is must be /{parent}/{child}
				if(!RESTfulAPIUtils.validateURL(textAPIURL.getText())) {
					MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ResourceManageEditor_32);
					
					textAPIURL.setFocus();
					return false;
				}
			}
		}
		
		try {
			TadpoleSystem_UserDBResource.userDBResourceDupUpdate(originalResourceDB.getParent(), dao);
		} catch (Exception ee) {
			logger.error("Resource validate", ee); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.get().Error, ee.getMessage()); //$NON-NLS-1$
			return false;
		}
		
		return true;
	}
	
	/**
	 * Initialize ui data
	 */
	private void initUIData() {
		try {
			List<UserDBResourceDataDAO> listData = new ArrayList<UserDBResourceDataDAO>();
			
			long intBeforeSeq = -1;
			UserDBResourceDataDAO userDBREsourceDataDAO = null; 
			List<UserDBResourceDataDAO> listSource = TadpoleSystem_UserDBResource.getResouceDataHistory(resourceManagerDao);
			for (UserDBResourceDataDAO resourceDAO :listSource) {
				if(intBeforeSeq != resourceDAO.getGroup_seq()) {
					if(userDBREsourceDataDAO != null) {
						listData.add(userDBREsourceDataDAO);
					}
					
					userDBREsourceDataDAO = new UserDBResourceDataDAO();
					intBeforeSeq = resourceDAO.getGroup_seq();
				}
				if(resourceDAO.getCreate_time() != null) userDBREsourceDataDAO.setCreate_time(resourceDAO.getCreate_time());
				else userDBREsourceDataDAO.setSqliteCreate_time(resourceDAO.getSqliteCreate_time());
				
				userDBREsourceDataDAO.setUser_seq(resourceDAO.getUser_seq());
				userDBREsourceDataDAO.setUsernames(resourceDAO.getUsernames());
				userDBREsourceDataDAO.setDatas(userDBREsourceDataDAO.getDatas() + resourceDAO.getDatas());
			}
			
			if(!listSource.isEmpty()) listData.add(userDBREsourceDataDAO);
			tvHistory.setInput(listData);
		} catch (Exception e) {
			logger.error("finding resource data", e); //$NON-NLS-1$
		}
	}

	/**
	 * action compare 
	 */
	private void compare() {
		StructuredSelection iss = (StructuredSelection)tvHistory.getSelection();
		if(iss.isEmpty()) return;
		
		Object[] objListSel = iss.toArray();
		try {
			String source = "", target = "";
			for(int i=0; i<objListSel.length; i++) {
				if(i==2) break;
				
				UserDBResourceDataDAO dao = (UserDBResourceDataDAO)objListSel[i];
				if(i==0) {
					source = dao.getDatas();
				} else {
					target = dao.getDatas();
				}
			}
			
			compareWidget.changeDiff(source, target);
		} catch(Exception e) {
			logger.error("Get detail sql", e); //$NON-NLS-1$
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button btnModify = createButton(parent, MODIFY_ID, Messages.get().Modified, false);
		btnModify.setEnabled(SessionManager.getUserSeq() == resourceManagerDao.getUser_seq());
		
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 600);
	}
}


class ResourceHistoryLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDBResourceDataDAO resourceDAO  = (UserDBResourceDataDAO)element;
		
		switch(columnIndex) {
		case 0: return resourceDAO.getCreate_time()==null?resourceDAO.getSqliteCreate_time():resourceDAO.getCreate_time().toString();
		case 1: return ""+resourceDAO.getUsernames(); //$NON-NLS-1$
		case 2: return resourceDAO.getDatas();
		}
		return "*** not set columns ***"; //$NON-NLS-1$
	}
	
}