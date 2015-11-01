package com.hangum.tadpole.manager.core.dialogs.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;

/**
 * Resource history dialog
 * 
 * @author hangum
 *
 */
public class ResourceHistoryDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResourceHistoryDialog.class);
	
	private ResourceManagerDAO resourceManagerDao;
	private Text textUser;
	private Text textTitle;
	private Text textDescription;
	private Text textCreateTime;
	private TableViewer tvHistory;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param resourceManagerDao 
	 */
	public ResourceHistoryDialog(Shell parentShell, ResourceManagerDAO resourceManagerDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.resourceManagerDao = resourceManagerDao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Resource History");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblTitle = new Label(compositeHead, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTitle.setText(resourceManagerDao.getName());
		
		Label lblDescription = new Label(compositeHead, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 40;
		textDescription.setLayoutData(gd_textDescription);
		textDescription.setText(resourceManagerDao.getDescription());
		
		Composite compositeHeaderUser = new Composite(compositeHead, SWT.NONE);
		compositeHeaderUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		compositeHeaderUser.setLayout(new GridLayout(4, false));
		
		Label lblUser = new Label(compositeHeaderUser, SWT.NONE);
		lblUser.setText("User");
		
		textUser = new Text(compositeHeaderUser, SWT.BORDER | SWT.READ_ONLY);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUser.setText(resourceManagerDao.getUser_name());
		
		Label lblCreateTime = new Label(compositeHeaderUser, SWT.NONE);
		lblCreateTime.setText("Create time");
		
		textCreateTime = new Text(compositeHeaderUser, SWT.BORDER | SWT.READ_ONLY);
		textCreateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textCreateTime.setText(resourceManagerDao.getCreate_time());
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tvHistory = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table = tvHistory.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tvcDate = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnDate = tvcDate.getColumn();
		tblclmnDate.setWidth(100);
		tblclmnDate.setText("Date");
		
		TableViewerColumn tvcUser = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnUser = tvcUser.getColumn();
		tblclmnUser.setWidth(100);
		tblclmnUser.setText("User");
		
		TableViewerColumn tvcSQL = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnSql = tvcSQL.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText("SQL");
		
		tvHistory.setContentProvider(new ArrayContentProvider());
		tvHistory.setLabelProvider(new ResourceHistoryLabelProvider());
		
		initUIData();
		
		return container;
	}
	
	/**
	 * Initialize ui data
	 */
	private void initUIData() {
		try {
			List<UserDBResourceDataDAO> listData = new ArrayList<UserDBResourceDataDAO>();
			
			int intBeforeSeq = -1;
			UserDBResourceDataDAO tmpDAO = null; 
			List<UserDBResourceDataDAO> listSource = TadpoleSystem_UserDBResource.getResouceDataHistory(resourceManagerDao);
			for (UserDBResourceDataDAO resourceDAO :listSource) {
				if(intBeforeSeq != resourceDAO.getGroup_seq()) {
					if(tmpDAO != null) {
						listData.add(tmpDAO);
					}
					
					tmpDAO = new UserDBResourceDataDAO();
					intBeforeSeq = resourceDAO.getSeq();
				}
				tmpDAO.setCreate_time(resourceDAO.getCreate_time());
				tmpDAO.setUser_seq(resourceDAO.getUser_seq());
				tmpDAO.setUsernames(resourceDAO.getUsernames());
				tmpDAO.setDatas(tmpDAO.getDatas() + resourceDAO.getDatas());
			}
			
			if(!listSource.isEmpty()) listData.add(tmpDAO);
			tvHistory.setInput(listData);
		} catch (Exception e) {
			logger.error("finding resource data", e);
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
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
		case 0: return resourceDAO.getCreate_time().toString();
		case 1: return ""+resourceDAO.getUsernames();
		case 2: return resourceDAO.getDatas();
		}
		return "*** not set columns ***";
	}
	
}