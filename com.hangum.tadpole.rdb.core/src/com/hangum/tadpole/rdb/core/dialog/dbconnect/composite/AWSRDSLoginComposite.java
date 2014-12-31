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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.aws.rds.commons.core.utils.AmazonRDSUtsils;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.SingleAddDBDialog;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.ext.aws.rds.AWSRDSUserDBDAO;

/**
 * Amazon RDS login composite.
 * 
 * Amazon RDS Region(http://docs.aws.amazon.com/general/latest/gr/rande.html#rds_region)
 * 
 * @author hangum
 *
 */
public class AWSRDSLoginComposite extends AbstractLoginComposite {
	private static final Logger logger = Logger.getLogger(AWSRDSLoginComposite.class);
	
	private Text textAccesskey;
	private Text textSecretKey;
	private Combo comboRegionName;
	
	private TableViewer tvRDS;
	private List<AWSRDSUserDBDAO> listUserDB;

	public AWSRDSLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("AmazonRDS", DBDefine.AMAZONRDS_DEFAULT, parent, style, listGroupName, selGroupName, userDB); //$NON-NLS-1$
	}

	@Override
	public void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeRDS = new Composite(this, SWT.NONE);
		GridLayout gl_compositeRDS = new GridLayout(1, false);
		gl_compositeRDS.verticalSpacing = 2;
		gl_compositeRDS.horizontalSpacing = 2;
		gl_compositeRDS.marginHeight = 2;
		gl_compositeRDS.marginWidth = 2;
		compositeRDS.setLayout(gl_compositeRDS);
		compositeRDS.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group groupLogin = new Group(compositeRDS, SWT.NONE);
		groupLogin.setText("Amazon User Information"); //$NON-NLS-1$
		groupLogin.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		GridLayout gl_compositeLogin = new GridLayout(3, false);
		gl_compositeLogin.verticalSpacing = 1;
		gl_compositeLogin.horizontalSpacing = 1;
		gl_compositeLogin.marginHeight = 1;
		gl_compositeLogin.marginWidth = 1;
		groupLogin.setLayout(gl_compositeLogin);

		Label lblAccesskey = new Label(groupLogin, SWT.NONE);
		lblAccesskey.setText("Access Key");
		textAccesskey = new Text(groupLogin, SWT.BORDER);
		textAccesskey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblSecretKey = new Label(groupLogin, SWT.NONE);
		lblSecretKey.setText("Secret Key"); //$NON-NLS-1$
		textSecretKey = new Text(groupLogin, SWT.BORDER | SWT.PASSWORD);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblEndpoint = new Label(groupLogin, SWT.NONE);
		lblEndpoint.setSize(59, 14);
		lblEndpoint.setText("Region"); //$NON-NLS-1$
		
		comboRegionName = new Combo(groupLogin, SWT.READ_ONLY);
		comboRegionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		List<String> listRegion = AmazonRDSUtsils.getRDSRegionList();
		for (String strRegion : listRegion) {
			comboRegionName.add(strRegion);
		}
		comboRegionName.setVisibleItemCount(listRegion.size());
		comboRegionName.select(0);
		
//		textEndpoint.setData("US East (Northern Virginia) Region", "rds.us-east-1.amazonaws.com");
//		textEndpoint.setData("US West (Oregon) Region", "rds.us-west-2.amazonaws.com");
//		textEndpoint.setData("US West (Northern California) Region", "rds.us-west-1.amazonaws.com");
//		textEndpoint.setData("EU (Ireland) Region", "rds.eu-west-1.amazonaws.com");
//		textEndpoint.setData("Asia Pacific (Singapore) Region", "rds.ap-southeast-1.amazonaws.com");
//		textEndpoint.setData("Asia Pacific (Sydney) Region", "rds.ap-southeast-2.amazonaws.com");
//		textEndpoint.setData("Asia Pacific (Tokyo) Region", "rds.ap-northeast-1.amazonaws.com");
//		textEndpoint.setData("South America (Sao Paulo) Region", "rds.sa-east-1.amazonaws.com");
		
		Button btnLogin = new Button(groupLogin, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findDBList();
			}
		});
		btnLogin.setText(Messages.AWSRDSLoginComposite_0);
		
		// rds 입력 리스트..
		Group compositeBody = new Group(compositeRDS, SWT.NONE);
		compositeBody.setText(Messages.AWSRDSLoginComposite_1);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		tvRDS = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvRDS.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumns();
		
		tvRDS.setContentProvider(new ArrayContentProvider());
		tvRDS.setLabelProvider(new RDSInfoLabelProvider());
		tvRDS.setInput(listUserDB);
		
		Composite compositeTail = new Composite(compositeBody, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		compositeTail.setLayout(new GridLayout(1, false));
		
		Button btnAddDatabase = new Button(compositeTail, SWT.NONE);
		btnAddDatabase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addDatabase();
			}
		});
		btnAddDatabase.setText(Messages.AWSRDSLoginComposite_2);
		
		init();
	}
	
	/**
	 * Add RDS to Tadpole
	 */
	private void addDatabase() {
		StructuredSelection ss = (StructuredSelection)tvRDS.getSelection();
		if(ss.isEmpty()) {
			MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.AWSRDSLoginComposite_8);
		} else {
			AWSRDSUserDBDAO amazonRDSDto = (AWSRDSUserDBDAO)ss.getFirstElement();
			
			SingleAddDBDialog dialog = new SingleAddDBDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
															amazonRDSDto, getListGroupName(), getSelGroupName());
			dialog.open();
		}
		
	}
	
	/**
	 * db list
	 */
	private void findDBList() {
		String strAccesskey = textAccesskey.getText().trim();
		String strSecretkey = textSecretKey.getText().trim();
		String strRegionName = comboRegionName.getText().trim();
		
		if(!checkTextCtl(textAccesskey, "Access key")) return; //$NON-NLS-1$
		if(!checkTextCtl(textSecretKey, "Secret Key")) return; //$NON-NLS-1$
		
		try {
			listUserDB = AmazonRDSUtsils.getDBList(strAccesskey, strSecretkey, strRegionName);
			
			tvRDS.setInput(listUserDB);
			tvRDS.refresh();
			
		} catch(Exception e) {
			logger.error("Get AmazonRDS information", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", "Get AmazonRDS information", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * create columns
	 */
	private void createColumns() {
		String[] columnNames = {"Engine", "IP", "Port", "Instance", "Charset", "User", "Password"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		int[] columnSize = {50, 200, 50, 100, 80, 80, 50};
		
		for(int i=0; i<columnNames.length; i++) {
			String name = columnNames[i];
			int size = columnSize[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvRDS, SWT.NONE);
			TableColumn tblclmnEngine = tableViewerColumn.getColumn();
			tblclmnEngine.setWidth(size);
			tblclmnEngine.setText(name);
		}
		
	}

	@Override
	protected void init() {
		// no exist code
	}

	@Override
	public boolean saveDBData() {
		return true;
	}

	@Override
	public boolean testConnection(boolean isTest) {
		if(!makeUserDBDao(isTest)) return false;
		return true;
	}
	
	@Override
	public boolean isValidateInput(boolean isTest) {
		String strAccesskey = textAccesskey.getText().trim();
		String strSecretkey = textSecretKey.getText().trim();
		
		if("".equals(strAccesskey)) {
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, "Please enter the Access Key");
			textAccesskey.setFocus();
			return false;
		} else if("".equals(strSecretkey)) {
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, "Please enter the Secret Key");
			textSecretKey.setFocus();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		return true;
	}

}

/**
* login data label provider
* @author hangum
*
*/
class RDSInfoLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		AWSRDSUserDBDAO dto = (AWSRDSUserDBDAO)element;

		switch(columnIndex) {
		case 0: return dto.getDbms_type();
		case 1: return dto.getHost();
		case 2: return dto.getPort();
		case 3: return dto.getDb();
		case 4: return dto.getLocale();
		case 5: return dto.getUsers();
		case 6: return dto.getPasswd();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}
