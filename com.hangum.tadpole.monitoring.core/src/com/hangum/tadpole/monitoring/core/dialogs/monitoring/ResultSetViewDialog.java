package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;

/**
 * ResultSet view dialog
 * 
 * @author hangum
 *
 */
public class ResultSetViewDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResultSetViewDialog.class);
	
	private int intBtnSnapshotID 	= IDialogConstants.CLIENT_ID + 1;
	private int intBtnUserConfirmID = IDialogConstants.CLIENT_ID + 2;
	
	private MonitoringResultDAO dao;
	private Text textTitle;
	private Text textDescription;
	private Text textSystemDescription;
	
	private Text textMessage;
	private Text textDate;
	private Text textDBName;
	private Text textAdvice;
//	private Text textUserConfirmMsg;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ResultSetViewDialog(Shell parentShell, MonitoringResultDAO dao) {
		super(parentShell);
		
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.dao = dao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("Error Viewer"); //$NON-NLS-1$
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
		
		Label lblDate = new Label(compositeHead, SWT.NONE);
		lblDate.setText("Date");
		
		textDate = new Text(compositeHead, SWT.BORDER);
		textDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDbName = new Label(compositeHead, SWT.NONE);
		lblDbName.setText("DB Name");
		
		textDBName = new Text(compositeHead, SWT.BORDER);
		textDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTitle = new Label(compositeHead, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeHead, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(compositeHead, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeHead, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 40;
		gd_textDescription.minimumHeight = 40;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblAdvice = new Label(compositeHead, SWT.NONE);
		lblAdvice.setText("Advice");
		
		textAdvice = new Text(compositeHead, SWT.BORDER);
		GridData gd_textAdvice = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textAdvice.heightHint = 40;
		gd_textAdvice.minimumHeight = 40;
		textAdvice.setLayoutData(gd_textAdvice);
		
		Label lblValue = new Label(compositeHead, SWT.NONE);
		lblValue.setText("System Message");
		
		textSystemDescription = new Text(compositeHead, SWT.BORDER);
		textSystemDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpResultset = new Group(container, SWT.NONE);
		grpResultset.setLayout(new GridLayout(1, false));
		grpResultset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpResultset.setText("Query Result");
		
		textMessage = new Text(grpResultset, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		Group grpUserConformMessage = new Group(container, SWT.NONE);
//		GridData gd_grpUserConformMessage = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
//		gd_grpUserConformMessage.heightHint = 50;
//		grpUserConformMessage.setLayoutData(gd_grpUserConformMessage);
//		grpUserConformMessage.setText("User Conform Message");
//		grpUserConformMessage.setLayout(new GridLayout(1, false));
//		
//		textUserConfirmMsg = new Text(grpUserConformMessage, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
//		textUserConfirmMsg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();
		
		// shell을 오른쪽 하단에 놓을수 있도록 합니다.
		Shell mainShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		int x = mainShell.getSize().x;
		int y = mainShell.getSize().y;
		
		container.getShell().setSize(450, 442);
		// 현재 shell location
		container.getShell().setLocation(x - 450, y - 442);

		return container;
	}

	/**
	 * initialize ui
	 */
	private void initUI() {
		try {
			textDate.setText(dao.getCreate_time().toString());
			textDBName.setText(dao.getUserDB().getDisplay_name());
			textTitle.setText(dao.getMonitoringIndexDAO().getTitle());
			textDescription.setText(dao.getMonitoringIndexDAO().getDescription());
			textAdvice.setText(dao.getMonitoringIndexDAO().getAdvice());
			textSystemDescription.setText(dao.getSystem_description());
			
			textMessage.setText(JSONUtil.getPretty(dao.getQuery_result()));
		} catch(Exception e) {
			logger.error("server status", e); //$NON-NLS-1$
		}
	}

	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == intBtnSnapshotID) {
			ShowSnapshotDialog dialog = new ShowSnapshotDialog(null, dao);
			dialog.open();
		} else if(buttonId == intBtnUserConfirmID) {
//			if(MessageDialog.openConfirm(null, Messages.get().Confirm, "사용자 확인으로 처리 하시겠습니까?")) {
//				try {
//					TadpoleSystem_monitoring.updateUserConfirmMsg(dao.getMonitoring_seq(), dao.getMonitoring_index_seq(), textUserConfirmMsg.getText());
//					
//					super.close();
//				} catch (Exception e) {
//					logger.error("Update user confirm", e);
//				}
//			}
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, intBtnSnapshotID, "View Snapshot", false);
//		createButton(parent, intBtnUserConfirmID, "User Confirm", false);
		createButton(parent, IDialogConstants.OK_ID, "Close", true);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 442);
	}
}
