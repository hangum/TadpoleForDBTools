package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringMainDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_monitoring;

/**
 * Add monitoring Dialog
 * 
 * @author hangum
 *
 */
public class UpdateMonitoringDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(UpdateMonitoringDialog.class);
	
	private MonitoringIndexDAO monitoringIndexDao;
	
	private Combo comboMonitoringType;
	private Text textTitle;
	private Text textDescription;
	private Combo comboMonitoringReadType;
	private Text textQuery;
	private Text textParameter1_name;
	private Text textParameter1Value;
	private Text textIndexName;
	private Text textConditionValue;
	
	private Combo comboConditionType;
	private Combo comboAfterProcess;
	private Text textParameter2_name;
	private Text textParameter2Value;
	private Text textReceiver;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param userDB
	 */
	public UpdateMonitoringDialog(Shell parentShell, MonitoringIndexDAO monitoringIndexDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.monitoringIndexDao = monitoringIndexDao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Update Monitoring Index"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		
		Composite compositeMoni = new Composite(container, SWT.NONE);
		compositeMoni.setLayout(new GridLayout(4, false));
		
		Label lblMonitoringType_1 = new Label(compositeMoni, SWT.NONE);
		lblMonitoringType_1.setText("Monitoring Type");
		
		comboMonitoringType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboMonitoringType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		comboMonitoringType.add("CONNECTION");
		comboMonitoringType.add("CPU");
		comboMonitoringType.add("DISK");
		comboMonitoringType.add("GENERAL_LOG");
		comboMonitoringType.add("SLOW_QUERY");
		comboMonitoringType.add("NETWORK_IN");
		comboMonitoringType.add("NETWORK_OUT");
		comboMonitoringType.add("PROCESS");
		comboMonitoringType.add("SESSION_LIST");
		comboMonitoringType.select(0);
		
		Label lblTitle = new Label(compositeMoni, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeMoni, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblDescription = new Label(compositeMoni, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeMoni, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_textDescription.heightHint = 40;
		gd_textDescription.minimumHeight = 40;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblMonitoringType = new Label(compositeMoni, SWT.NONE);
		lblMonitoringType.setText("Read Type");
		
		comboMonitoringReadType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboMonitoringReadType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		comboMonitoringReadType.add("SQL");
//		comboMonitoringReadType.add("PL/SQL");
//		comboMonitoringReadType.add("Rest-API");
		comboMonitoringReadType.select(0);
		
		Label lblQuery = new Label(compositeMoni, SWT.NONE);
		lblQuery.setText("Query");
		
		textQuery = new Text(compositeMoni, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textQuery = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_textQuery.heightHint = 80;
		gd_textQuery.minimumHeight = 80;
		textQuery.setLayoutData(gd_textQuery);
		
		Label lblParameter = new Label(compositeMoni, SWT.NONE);
		lblParameter.setText("Parameter 1 Name");
		
		textParameter1_name = new Text(compositeMoni, SWT.BORDER);
		textParameter1_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblParameterValue = new Label(compositeMoni, SWT.NONE);
		lblParameterValue.setText("Parameter 1 Value");
		
		textParameter1Value = new Text(compositeMoni, SWT.BORDER);
		textParameter1Value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblParameterName = new Label(compositeMoni, SWT.NONE);
		lblParameterName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblParameterName.setText("Parameter 2 Name");
		
		textParameter2_name = new Text(compositeMoni, SWT.BORDER);
		textParameter2_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblParameterValue_1 = new Label(compositeMoni, SWT.NONE);
		lblParameterValue_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblParameterValue_1.setText("Parameter 2 Value");
		
		textParameter2Value = new Text(compositeMoni, SWT.BORDER);
		textParameter2Value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblIndexName = new Label(compositeMoni, SWT.NONE);
		lblIndexName.setText("Index Name");
		
		textIndexName = new Text(compositeMoni, SWT.BORDER);
		textIndexName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConditionType = new Label(compositeMoni, SWT.NONE);
		lblConditionType.setText("Condition Type");
		
		comboConditionType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboConditionType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboConditionType.add("EQUALS");
		comboConditionType.add("LEAST");
		comboConditionType.add("GREATEST");
		comboConditionType.add("NOT_CHECK");
		comboConditionType.add("RISE_EXCEPTION");
		comboConditionType.select(0);

		Label lblCondition = new Label(compositeMoni, SWT.NONE);
		lblCondition.setText("Condition Value");
		
		textConditionValue = new Text(compositeMoni, SWT.BORDER);
		textConditionValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblIfError = new Label(compositeMoni, SWT.NONE);
		lblIfError.setText("If error?");
		
		comboAfterProcess = new Combo(compositeMoni, SWT.READ_ONLY);
		comboAfterProcess.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		comboAfterProcess.add("EMAIL");
		comboAfterProcess.add("KILL_AFTER_EMAIL");
		comboAfterProcess.select(0);
		
		Label lblReceiver = new Label(compositeMoni, SWT.NONE);
		lblReceiver.setText("Receiver");
		
		textReceiver = new Text(compositeMoni, SWT.BORDER);
		textReceiver.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
//		sashForm.setWeights(new int[] {3, 7});
		
		initUI();

		return container;
	}

	/**
	 * ui initialize
	 */
	private void initUI() {
//		comboMonitoringType.setText(dao.getMonitoring_type());
//		textTitle.setText(dao.getTitle());
//		textDescription.setText(dao.getDescription());
//		comboMonitoringReadType.setText("SQL");
//		textQuery.setText(dao.getQuery());
//		textParameter1_name.setText(StringUtils.trimToEmpty(dao.getParam_1_column()));
//		textParameter1Value.setText(StringUtils.trimToEmpty(dao.getParam_1_init_value()));
//		textParameter2_name.setText(StringUtils.trimToEmpty(dao.getParam_2_column()));
//		textParameter2Value.setText(StringUtils.trimToEmpty(dao.getParam_2_init_value()));
//		
//		textIndexName.setText(dao.getIndex_nm());
//		comboConditionType.setText(dao.getCondition_type());
//		textConditionValue.setText(dao.getCondition_value());
//		comboAfterProcess.setText(dao.getAfter_type());
//		textReceiver.setText(SessionManager.getEMAIL());
	}
	
	@Override
	protected void okPressed() {
//		if("".equals(textTitle.getText())) {
//			MessageDialog.openError(null, "Error", "Title은 공백이 될 수 없습니다.");
//			textTitle.setFocus();
//			return;
//		}
//		if("".equals(textQuery.getText())) {
//			MessageDialog.openError(null, "Error", "Query은 공백이 될 수 없습니다.");
//			textQuery.setFocus();
//			return;
//		}
//
//		 MonitoringMainDAO mainDao = new MonitoringMainDAO();
////		 mainDao.setUser_seq(userDB.getUser_seq());
////		 mainDao.setDb_seq(userDB.getSeq());
//		 mainDao.setRead_method(comboMonitoringReadType.getText());
//		 mainDao.setTitle(textTitle.getText());
//		 mainDao.setDescription(textDescription.getText());
//		 mainDao.setCron_exp("*/10 * * * * ?");
//		 mainDao.setQuery(textQuery.getText());
//		 mainDao.setIs_result_save(PublicTadpoleDefine.YES_NO.YES.toString());
//
//		 MonitoringIndexDAO indexDao = new MonitoringIndexDAO();
//		 indexDao.setMonitoring_seq(mainDao.getSeq());
//		 
//		 indexDao.setMonitoring_type(comboMonitoringType.getText());
//		 indexDao.setIndex_nm(textIndexName.getText());
//		 indexDao.setCondition_type(comboConditionType.getText());
//		 indexDao.setCondition_value(textConditionValue.getText());
//		 indexDao.setAfter_type(comboAfterProcess.getText());
//		 
//		 indexDao.setParam_1_column(textParameter1_name.getText());
//		 indexDao.setParam_1_init_value(textParameter1Value.getText());
//		 indexDao.setParam_2_column(textParameter2_name.getText());
//		 indexDao.setParam_2_init_value(textParameter2Value.getText());
//		 
//		 indexDao.setReceiver(textReceiver.getText());
//
//		try {
//			TadpoleSystem_monitoring.saveMonitoring(mainDao, indexDao);
//		} catch (Exception e) {
//			logger.error("save monitoring index", e);
//		}
//		 
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 670);
	}
}
