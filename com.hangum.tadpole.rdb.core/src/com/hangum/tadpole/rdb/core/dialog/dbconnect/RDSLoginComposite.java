package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.aws.rds.commons.core.AmazonRDSUtsils;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.ext.aws.rds.AWSRDSUserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;

/**
 * Amazon RDS login composite.
 * 
 * @author hangum
 *
 */
public class RDSLoginComposite extends AbstractLoginComposite {
	private static final Logger logger = Logger.getLogger(RDSLoginComposite.class);
	
	private Text textAccesskey;
	private Text textSecretKey;
	private Text textEndpoint;
	
	private TableViewer tvRDS;
	private List<AWSRDSUserDBDAO> listUserDB;

	public RDSLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("AmazonRDS", DBDefine.AMAZONRDS_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
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
		
//		preDBInfo = new PreConnectionInfoGroup(compositeRDS, SWT.NONE, listGroupName);
//		preDBInfo.setText(Messages.MSSQLLoginComposite_preDBInfo_text);
//		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Group groupLogin = new Group(compositeRDS, SWT.NONE);
		groupLogin.setText("Amazon User Information");
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
		lblSecretKey.setText("Secret key");
		textSecretKey = new Text(groupLogin, SWT.BORDER | SWT.PASSWORD);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblEndpoint = new Label(groupLogin, SWT.NONE);
		lblEndpoint.setSize(59, 14);
		lblEndpoint.setText("Endpoint");
		
		textEndpoint = new Text(groupLogin, SWT.BORDER);
		textEndpoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnLogin = new Button(groupLogin, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findDBList();
			}
		});
		btnLogin.setText("Login");
		
		Group compositeBody = new Group(compositeRDS, SWT.NONE);
		compositeBody.setText("RDS List");
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
		
		othersConnectionInfo = new OthersConnectionRDBGroup(compositeRDS, SWT.NONE);
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		init();
	}
	/**
	 * db list
	 */
	private void findDBList() {
		String strAccesskey = textAccesskey.getText().trim();
		String strSecretkey = textSecretKey.getText().trim();
		String strEndpoint = textEndpoint.getText().trim();
		
		if(!checkTextCtl(textAccesskey, "Access key")) return;
		if(!checkTextCtl(textSecretKey, "Access key")) return;
		if(!checkTextCtl(textEndpoint, "Access key")) return;
		
		try {
			listUserDB = AmazonRDSUtsils.getDBList(strAccesskey, strSecretkey, strEndpoint);
			
			tvRDS.setInput(listUserDB);
			tvRDS.refresh();
			
		} catch(Exception e) {
			logger.error("Get AmazonRDS information", e);
		}
	}
	
	/**
	 * create columns
	 */
	private void createColumns() {
		String[] columnNames = {"", "운영타입", "그룹", "이름", "Engine", "Ver", "Charset", "IP", "Port", "Instance", "User"};
		int[] columnSize = {20, 52, 60, 60, 50, 50, 50, 100, 50, 80, 50};
		
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
		
	}

	@Override
	protected boolean connection() {
		// TODO Auto-generated method stub
		return false;
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
		case 0: return "";
		case 1: return dto.getOperation_type();
		case 2: return dto.getGroup_name();
		case 3: return dto.getDisplay_name();
		case 4: return dto.getExt1();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}
