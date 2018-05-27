///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.core.runtime.Status;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.viewers.ArrayContentProvider;
//import org.eclipse.jface.viewers.ITableLabelProvider;
//import org.eclipse.jface.viewers.LabelProvider;
//import org.eclipse.jface.viewers.StructuredSelection;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.TableViewerColumn;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Combo;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Group;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.PlatformUI;
//
//import com.hangum.tadpole.aws.rds.commons.core.utils.AmazonRDSUtsils;
//import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
//import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
//import com.hangum.tadpole.engine.define.DBDefine;
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//import com.hangum.tadpole.engine.query.dao.system.ext.aws.rds.AWSRDSUserDBDAO;
//import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
//import com.hangum.tadpole.preference.get.GetAmazonPreference;
//import com.hangum.tadpole.rdb.core.Activator;
//import com.hangum.tadpole.rdb.core.Messages;
//import com.hangum.tadpole.rdb.core.dialog.dbconnect.SingleAddDBDialog;
//
///**
// * Amazon RDS login composite.
// * 
// * Amazon RDS Region(http://docs.aws.amazon.com/general/latest/gr/rande.html#rds_region)
// * 
// * @author hangum
// *
// */
//public class AWSRDSLoginComposite extends AbstractLoginComposite {
//	private static final Logger logger = Logger.getLogger(AWSRDSLoginComposite.class);
//	
//	private Text textAccesskey;
//	private Text textSecretKey;
//	private Combo comboRegionName;
//	
//	private TableViewer tvRDS;
//	private List<AWSRDSUserDBDAO> listAmazonRDS;
//
//	public AWSRDSLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
//		super("AmazonRDS", DBDefine.AMAZONRDS_DEFAULT, parent, style, listGroupName, selGroupName, userDB); //$NON-NLS-1$
//	}
//
//	@Override
//	public void crateComposite() {
//		GridLayout gridLayout = new GridLayout(1, false);
//		gridLayout.verticalSpacing = 2;
//		gridLayout.horizontalSpacing = 2;
//		gridLayout.marginHeight = 2;
//		gridLayout.marginWidth = 0;
//		setLayout(gridLayout);
//		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//
//		Composite compositeRDS = new Composite(this, SWT.NONE);
//		GridLayout gl_compositeRDS = new GridLayout(1, false);
//		gl_compositeRDS.verticalSpacing = 2;
//		gl_compositeRDS.horizontalSpacing = 2;
//		gl_compositeRDS.marginHeight = 2;
//		gl_compositeRDS.marginWidth = 2;
//		compositeRDS.setLayout(gl_compositeRDS);
//		compositeRDS.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		Group groupLogin = new Group(compositeRDS, SWT.NONE);
//		groupLogin.setText(Messages.get().AWSRDSLoginComposite_3);
//		groupLogin.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
//		GridLayout gl_compositeLogin = new GridLayout(3, false);
//		gl_compositeLogin.verticalSpacing = 3;
//		gl_compositeLogin.horizontalSpacing = 3;
//		gl_compositeLogin.marginHeight = 3;
//		gl_compositeLogin.marginWidth = 3;
//		groupLogin.setLayout(gl_compositeLogin);
//
//		Label lblAccesskey = new Label(groupLogin, SWT.NONE);
//		lblAccesskey.setText(Messages.get().AssesKey);
//		textAccesskey = new Text(groupLogin, SWT.BORDER);
//		textAccesskey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
//		
//		Label lblSecretKey = new Label(groupLogin, SWT.NONE);
//		lblSecretKey.setText(Messages.get().SecretKey);
//		textSecretKey = new Text(groupLogin, SWT.BORDER | SWT.PASSWORD);
//		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
//		
//		Label lblEndpoint = new Label(groupLogin, SWT.NONE);
//		lblEndpoint.setSize(59, 14);
//		lblEndpoint.setText(Messages.get().AWSRDSLoginComposite_6);
//		
//		comboRegionName = new Combo(groupLogin, SWT.READ_ONLY);
//		comboRegionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		List<String> listRegion = AmazonRDSUtsils.getRDSRegionList();
//		for (String strRegion : listRegion) {
//			comboRegionName.add(strRegion);
//		}
//		comboRegionName.setVisibleItemCount(listRegion.size());
//		comboRegionName.select(0);
//		
////		textEndpoint.setData("US East (Northern Virginia) Region", "rds.us-east-1.amazonaws.com");
////		textEndpoint.setData("US West (Oregon) Region", "rds.us-west-2.amazonaws.com");
////		textEndpoint.setData("US West (Northern California) Region", "rds.us-west-1.amazonaws.com");
////		textEndpoint.setData("EU (Ireland) Region", "rds.eu-west-1.amazonaws.com");
////		textEndpoint.setData("Asia Pacific (Singapore) Region", "rds.ap-southeast-1.amazonaws.com");
////		textEndpoint.setData("Asia Pacific (Sydney) Region", "rds.ap-southeast-2.amazonaws.com");
////		textEndpoint.setData("Asia Pacific (Tokyo) Region", "rds.ap-northeast-1.amazonaws.com");
////		textEndpoint.setData("South America (Sao Paulo) Region", "rds.sa-east-1.amazonaws.com");
//		
//		Button btnLogin = new Button(groupLogin, SWT.NONE);
//		btnLogin.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				findDBList();
//			}
//		});
//		btnLogin.setText(Messages.get().AWSRDSLoginComposite_0);
//		
//		// rds 입력 리스트..
//		Group compositeBody = new Group(compositeRDS, SWT.NONE);
//		compositeBody.setText(Messages.get().AWSRDSLoginComposite_1);
//		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		GridLayout gl_compositeBody = new GridLayout(1, false);
//		gl_compositeBody.verticalSpacing = 2;
//		gl_compositeBody.horizontalSpacing = 2;
//		gl_compositeBody.marginHeight = 2;
//		gl_compositeBody.marginWidth = 2;
//		compositeBody.setLayout(gl_compositeBody);
//		
//		tvRDS = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
//		Table table = tvRDS.getTable();
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		createColumns();
//		
//		tvRDS.setContentProvider(ArrayContentProvider.getInstance());
//		tvRDS.setLabelProvider(new RDSInfoLabelProvider());
//		tvRDS.setInput(listAmazonRDS);
//		
//		Composite compositeTail = new Composite(compositeBody, SWT.NONE);
//		compositeTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
//		compositeTail.setLayout(new GridLayout(1, false));
//		
//		Button btnAddDatabase = new Button(compositeTail, SWT.NONE);
//		btnAddDatabase.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				addDatabase();
//			}
//		});
//		btnAddDatabase.setText(CommonMessages.get().AddDatabase);
//		
//		init();
//	}
//	
//	/**
//	 * Add RDS to Tadpole
//	 */
//	private void addDatabase() {
//		StructuredSelection ss = (StructuredSelection)tvRDS.getSelection();
//		if(ss.isEmpty()) {
//			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().AWSRDSLoginComposite_8);
//		} else {
//			AWSRDSUserDBDAO amazonRDSDto = (AWSRDSUserDBDAO)ss.getFirstElement();
//			
//			SingleAddDBDialog dialog = new SingleAddDBDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
//															amazonRDSDto, getListGroupName(), getSelGroupName());
//			if(Dialog.OK == dialog.open()) {
//				UserDBDAO registUserDB = dialog.getDTO();
//				amazonRDSDto.setUserTadpoleDisplayName(registUserDB.getDisplay_name());
//				
//				tvRDS.refresh(amazonRDSDto, true);
//			}
//		}
//		
//	}
//	
//	/**
//	 * db list
//	 */
//	private void findDBList() {
//		String strAccesskey = textAccesskey.getText().trim();
//		String strSecretkey = textSecretKey.getText().trim();
//		String strRegionName = comboRegionName.getText().trim();
//		
//		if(!ValidChecker.checkTextCtl(textAccesskey, Messages.get().AssesKey)) return;
//		if(!ValidChecker.checkTextCtl(textSecretKey, Messages.get().SecretKey)) return;
//		
//		try {
//			Map<String, UserDBDAO> mapRegisteredDB = TadpoleSystem_UserDBQuery.getUserDBByHost();
//			
//			listAmazonRDS = AmazonRDSUtsils.getDBList(strAccesskey, strSecretkey, strRegionName);
//			for (AWSRDSUserDBDAO rdsDAO : listAmazonRDS) {
//				if(mapRegisteredDB.containsKey(rdsDAO.getHost())) {
//					UserDBDAO userDB = mapRegisteredDB.get(rdsDAO.getHost());
//					rdsDAO.setUserTadpoleDisplayName(userDB.getDisplay_name());
//				}
//			}
//			
//			tvRDS.setInput(listAmazonRDS);
//			tvRDS.refresh();
//			
//		} catch(Exception e) {
//			logger.error("Get AmazonRDS information", e); //$NON-NLS-1$
//			
//			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//			ExceptionDetailsErrorDialog.openError(getShell(),CommonMessages.get().Error, "Get AmazonRDS information", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
//		}
//	}
//	
//	/**
//	 * create columns
//	 */
//	private void createColumns() {
//		String[] columnNames = {Messages.get().DisplayName, Messages.get().AWSRDSLoginComposite_11, Messages.get().IP, Messages.get().Port, Messages.get().Instance, Messages.get().Charset, Messages.get().User};
//		int[] columnSize = {120, 50, 200, 50, 100, 80, 80};
//		
//		for(int i=0; i<columnNames.length; i++) {
//			String name = columnNames[i];
//			int size = columnSize[i];
//			
//			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvRDS, SWT.NONE);
//			TableColumn tblclmnEngine = tableViewerColumn.getColumn();
//			tblclmnEngine.setWidth(size);
//			tblclmnEngine.setText(name);
//		}
//		
//	}
//
//	@Override
//	protected void init() {
//		textAccesskey.setText(GetAmazonPreference.getAccessValue());
//		textSecretKey.setText(GetAmazonPreference.getSecretValue());
//		
//		textAccesskey.setFocus();
//	}
//
//	@Override
//	public boolean saveDBData() {
//		return true;
//	}
//
//	@Override
//	public boolean testConnection(boolean isTest) {
//		if(!makeUserDBDao(isTest)) return false;
//		return true;
//	}
//	
//	@Override
//	public boolean isValidateInput(boolean isTest) {
//		String strAccesskey = textAccesskey.getText().trim();
//		String strSecretkey = textSecretKey.getText().trim();
//		
//		if("".equals(strAccesskey)) { //$NON-NLS-1$
//			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().AWSRDSLoginComposite_7);
//			textAccesskey.setFocus();
//			return false;
//		} else if("".equals(strSecretkey)) { //$NON-NLS-1$
//			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().AWSRDSLoginComposite_20);
//			textSecretKey.setFocus();
//			return false;
//		}
//		
//		return true;
//	}
//
//	@Override
//	public boolean makeUserDBDao(boolean isTest) {
//		if(!isValidateInput(isTest)) return false;
//		
//		return true;
//	}
//
//}
//
///**
//* login data label provider
//* @author hangum
//*
//*/
//class RDSInfoLabelProvider extends LabelProvider implements ITableLabelProvider {
//
//	@Override
//	public Image getColumnImage(Object element, int columnIndex) {
//		return null;
//	}
//
//	@Override
//	public String getColumnText(Object element, int columnIndex) {
//		AWSRDSUserDBDAO dto = (AWSRDSUserDBDAO)element;
//
//		switch(columnIndex) {
//		case 0: return dto.getUserTadpoleDisplayName();
//		case 1: return dto.getDbms_type();
//		case 2: return dto.getHost();
//		case 3: return dto.getPort();
//		case 4: return dto.getDb();
//		case 5: return dto.getLocale();
//		case 6: return dto.getUsers();
////		case 7: return dto.getPasswd();
//		}
//		
//		return "*** not set column ***"; //$NON-NLS-1$
//	}
//	
//}
