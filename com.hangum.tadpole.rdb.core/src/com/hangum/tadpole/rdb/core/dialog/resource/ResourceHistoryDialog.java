package com.hangum.tadpole.rdb.core.dialog.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.rdb.core.Messages;

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
	private TadpoleEditorWidget textLeft;
	private TadpoleEditorWidget textRight;

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
		newShell.setText(Messages.ResourceHistoryDialog_0);
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
		lblTitle.setText(Messages.ResourceHistoryDialog_1);
		
		textTitle = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTitle.setText(resourceManagerDao.getName());
		
		Label lblDescription = new Label(compositeHead, SWT.NONE);
		lblDescription.setText(Messages.ResourceHistoryDialog_2);
		
		textDescription = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 40;
		textDescription.setLayoutData(gd_textDescription);
		textDescription.setText(resourceManagerDao.getDescription());
		
		Composite compositeHeaderUser = new Composite(compositeHead, SWT.NONE);
		compositeHeaderUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		compositeHeaderUser.setLayout(new GridLayout(4, false));
		
		Label lblUser = new Label(compositeHeaderUser, SWT.NONE);
		lblUser.setText(Messages.ResourceHistoryDialog_3);
		
		textUser = new Text(compositeHeaderUser, SWT.BORDER | SWT.READ_ONLY);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUser.setText(resourceManagerDao.getUser_name());
		
		Label lblCreateTime = new Label(compositeHeaderUser, SWT.NONE);
		lblCreateTime.setText(Messages.ResourceHistoryDialog_4);
		
		textCreateTime = new Text(compositeHeaderUser, SWT.BORDER | SWT.READ_ONLY);
		textCreateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textCreateTime.setText(resourceManagerDao.getCreate_time());
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayout(new GridLayout(1, false));
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tvHistory = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table = tvHistory.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tvcDate = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnDate = tvcDate.getColumn();
		tblclmnDate.setWidth(100);
		tblclmnDate.setText(Messages.ResourceHistoryDialog_5);
		
		TableViewerColumn tvcUser = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnUser = tvcUser.getColumn();
		tblclmnUser.setWidth(100);
		tblclmnUser.setText(Messages.ResourceHistoryDialog_3);
		
		TableViewerColumn tvcSQL = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnSql = tvcSQL.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText(Messages.ResourceHistoryDialog_7);
		
		tvHistory.setContentProvider(new ArrayContentProvider());
		tvHistory.setLabelProvider(new ResourceHistoryLabelProvider());

		Composite compositeCompare = new Composite(sashForm, SWT.NONE);
		compositeCompare.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeCompare.setLayout(new GridLayout(2, false));
		
		textLeft = new TadpoleEditorWidget(compositeCompare, SWT.BORDER, EditorDefine.EXT_DEFAULT, "", "");
		textLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		textRight = new TadpoleEditorWidget(compositeCompare, SWT.BORDER, EditorDefine.EXT_DEFAULT, "", "");
		textRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		sashForm.setWeights(new int[] {6, 4});
		initUIData();
		
		return container;
	}
	
	/**
	 * Initialize ui data
	 */
	private void initUIData() {
		try {
			List<UserDBResourceDataDAO> listData = new ArrayList<UserDBResourceDataDAO>();
			
			long intBeforeSeq = -1;
			UserDBResourceDataDAO tmpDAO = null; 
			List<UserDBResourceDataDAO> listSource = TadpoleSystem_UserDBResource.getResouceDataHistory(resourceManagerDao);
			for (UserDBResourceDataDAO resourceDAO :listSource) {
				if(intBeforeSeq != resourceDAO.getGroup_seq()) {
					if(tmpDAO != null) {
						listData.add(tmpDAO);
					}
					
					tmpDAO = new UserDBResourceDataDAO();
					intBeforeSeq = resourceDAO.getGroup_seq();
				}
				if(resourceDAO.getCreate_time() != null) tmpDAO.setCreate_time(resourceDAO.getCreate_time());
				else tmpDAO.setSqliteCreate_time(resourceDAO.getSqliteCreate_time());
				
				tmpDAO.setUser_seq(resourceDAO.getUser_seq());
				tmpDAO.setUsernames(resourceDAO.getUsernames());
				tmpDAO.setDatas(tmpDAO.getDatas() + resourceDAO.getDatas());
			}
			
			if(!listSource.isEmpty()) listData.add(tmpDAO);
			tvHistory.setInput(listData);
		} catch (Exception e) {
			logger.error("finding resource data", e); //$NON-NLS-1$
		}
	}
	
	@Override
	protected void okPressed() {
		StructuredSelection iss = (StructuredSelection)tvHistory.getSelection();
		if(iss.isEmpty()) return;
		
		Object[] objListSel = iss.toArray();
		try {
			for(int i=0; i<objListSel.length; i++) {
				if(i==2) break;
				
				UserDBResourceDataDAO dao = (UserDBResourceDataDAO)objListSel[i];
				if(i==0) {
					textLeft.setText(dao.getDatas());
				} else {
					textRight.setText(dao.getDatas());
				}
			}
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
		createButton(parent, IDialogConstants.OK_ID, Messages.ResourceHistoryDialog_9, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.ResourceHistoryDialog_10, false);
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