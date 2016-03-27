package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import org.apache.commons.lang.StringUtils;
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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringMainDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.Messages;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine;

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
	private Combo comboKPIType;
	
	private Text textTitle;
	private Text textDescription;
	private Text textAdvice;
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
	
	private Combo comboExceptionConditionType;
	private Text textExceptionIndexNM;
	private Text textExceptionConditionValue;
	
	private Text textReceiver;
	
	private Combo comboIsResultSave;
	private Combo comboIsSnapshotSave;

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
		compositeMoni.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeMoni.setLayout(new GridLayout(6, false));
		
		Label lblMonitoringType_1 = new Label(compositeMoni, SWT.NONE);
		lblMonitoringType_1.setText("Monitoring Type");
		
		comboMonitoringType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboMonitoringType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		for (MonitoringDefine.MONITORING_TYPE type : MonitoringDefine.MONITORING_TYPE.values()) {
			comboMonitoringType.add(type.toString());
		}
		comboMonitoringType.setVisibleItemCount(MonitoringDefine.MONITORING_TYPE.values().length);
		comboMonitoringType.select(0);
		
		Label lblKpi = new Label(compositeMoni, SWT.NONE);
		lblKpi.setText("KPI");
		
		comboKPIType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboKPIType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		for (MonitoringDefine.KPI_TYPE type : MonitoringDefine.KPI_TYPE.values()) {
			comboKPIType.add(type.toString());
		}
		comboKPIType.setVisibleItemCount(MonitoringDefine.MONITORING_TYPE.values().length);
		comboKPIType.select(0);
		
		Label lblTitle = new Label(compositeMoni, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeMoni, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblDescription = new Label(compositeMoni, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeMoni, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gd_textDescription.heightHint = 40;
		gd_textDescription.minimumHeight = 40;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblAdvice = new Label(compositeMoni, SWT.NONE);
		lblAdvice.setText("Advice");
		
		textAdvice = new Text(compositeMoni, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textAdvice = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gd_textAdvice.minimumHeight = 40;
		gd_textAdvice.heightHint = 40;
		textAdvice.setLayoutData(gd_textAdvice);
		
		Label lblMonitoringType = new Label(compositeMoni, SWT.NONE);
		lblMonitoringType.setText("Read Type");
		
		comboMonitoringReadType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboMonitoringReadType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		for (MonitoringDefine.ACCESS_TYPE type : MonitoringDefine.ACCESS_TYPE.values()) {
			comboMonitoringReadType.add(type.toString());
		}
		comboMonitoringReadType.setVisibleItemCount(MonitoringDefine.ACCESS_TYPE.values().length);
		comboMonitoringReadType.select(0);
		
		Label lblQuery = new Label(compositeMoni, SWT.NONE);
		lblQuery.setText("Query");
		
		textQuery = new Text(compositeMoni, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textQuery = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gd_textQuery.heightHint = 80;
		gd_textQuery.minimumHeight = 80;
		textQuery.setLayoutData(gd_textQuery);
		
		Label lblParameter = new Label(compositeMoni, SWT.NONE);
		lblParameter.setText("Parameter 1 Name");
		
		textParameter1_name = new Text(compositeMoni, SWT.BORDER);
		textParameter1_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblParameterValue = new Label(compositeMoni, SWT.NONE);
		lblParameterValue.setText("Parameter 1 Value");
		
		textParameter1Value = new Text(compositeMoni, SWT.BORDER);
		textParameter1Value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblParameterName = new Label(compositeMoni, SWT.NONE);
		lblParameterName.setText("Parameter 2 Name");
		
		textParameter2_name = new Text(compositeMoni, SWT.BORDER);
		textParameter2_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblParameterValue_1 = new Label(compositeMoni, SWT.NONE);
		lblParameterValue_1.setText("Parameter 2 Value");
		
		textParameter2Value = new Text(compositeMoni, SWT.BORDER);
		textParameter2Value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblIndexName = new Label(compositeMoni, SWT.NONE);
		lblIndexName.setText("Index Name");
		
		textIndexName = new Text(compositeMoni, SWT.BORDER);
		textIndexName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConditionType = new Label(compositeMoni, SWT.NONE);
		lblConditionType.setText("Type");
		
		comboConditionType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboConditionType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (MonitoringDefine.CONDITION_TYPE type : MonitoringDefine.CONDITION_TYPE.values()) {
			comboConditionType.add(type.toString());
		}
		comboConditionType.setVisibleItemCount(MonitoringDefine.CONDITION_TYPE.values().length);
		comboConditionType.select(0);

		Label lblCondition = new Label(compositeMoni, SWT.NONE);
		lblCondition.setText("Value");
		
		textConditionValue = new Text(compositeMoni, SWT.BORDER);
		textConditionValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblExceptionIndex = new Label(compositeMoni, SWT.NONE);
		lblExceptionIndex.setText("Exception Index");
		
		textExceptionIndexNM = new Text(compositeMoni, SWT.BORDER);
		textExceptionIndexNM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(compositeMoni, SWT.NONE);
		lblType.setText("Type");
		
		comboExceptionConditionType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboExceptionConditionType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (MonitoringDefine.CONDITION_TYPE type : MonitoringDefine.CONDITION_TYPE.values()) {
			comboExceptionConditionType.add(type.toString());
		}
		comboExceptionConditionType.setVisibleItemCount(MonitoringDefine.CONDITION_TYPE.values().length);
		comboExceptionConditionType.select(0);
		
		Label lblNewLabel = new Label(compositeMoni, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Value");
		
		textExceptionConditionValue = new Text(compositeMoni, SWT.BORDER);
		textExceptionConditionValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblIfError = new Label(compositeMoni, SWT.NONE);
		lblIfError.setText("If error?");
		
		comboAfterProcess = new Combo(compositeMoni, SWT.READ_ONLY);
		comboAfterProcess.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		for (MonitoringDefine.AFTER_PROCESS_TYPE type : MonitoringDefine.AFTER_PROCESS_TYPE.values()) {
			comboAfterProcess.add(type.toString());
		}
		comboAfterProcess.setVisibleItemCount(MonitoringDefine.AFTER_PROCESS_TYPE.values().length);
		comboAfterProcess.select(0);
		
		Label lblReceiver = new Label(compositeMoni, SWT.NONE);
		lblReceiver.setText("Receiver");
		
		textReceiver = new Text(compositeMoni, SWT.BORDER);
		textReceiver.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblSaveResultData = new Label(compositeMoni, SWT.NONE);
		lblSaveResultData.setText("Is save result data?");
		
		comboIsResultSave = new Combo(compositeMoni, SWT.READ_ONLY);
		comboIsResultSave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		for (PublicTadpoleDefine.YES_NO type : PublicTadpoleDefine.YES_NO.values()) {
			comboIsResultSave.add(type.toString());
		}
		comboIsResultSave.setVisibleItemCount(MonitoringDefine.AFTER_PROCESS_TYPE.values().length);
		comboIsResultSave.select(0);
		
		Label lblSaveSanpshotData = new Label(compositeMoni, SWT.NONE);
		lblSaveSanpshotData.setText("is Save sanpshot data?");
		
		comboIsSnapshotSave = new Combo(compositeMoni, SWT.READ_ONLY);
		comboIsSnapshotSave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		for (PublicTadpoleDefine.YES_NO type : PublicTadpoleDefine.YES_NO.values()) {
			comboIsSnapshotSave.add(type.toString());
		}
		comboIsSnapshotSave.setVisibleItemCount(MonitoringDefine.AFTER_PROCESS_TYPE.values().length);
		comboIsSnapshotSave.select(0);
		
		initUI();

		return container;
	}

	/**
	 * ui initialize
	 */
	private void initUI() {
		comboMonitoringType.setText(monitoringIndexDao.getMonitoring_type());
		comboKPIType.setText(monitoringIndexDao.getKpi_type());
		
		textTitle.setText(monitoringIndexDao.getTitle());
		textDescription.setText(StringUtils.trimToEmpty(monitoringIndexDao.getDescription()));
		textAdvice.setText(monitoringIndexDao.getAdvice());
		comboMonitoringReadType.setText("SQL");
		textQuery.setText(StringUtils.trimToEmpty(monitoringIndexDao.getQuery()));
		textParameter1_name.setText(StringUtils.trimToEmpty(monitoringIndexDao.getParam_1_column()));
		textParameter1Value.setText(StringUtils.trimToEmpty(monitoringIndexDao.getParam_1_init_value()));
		textParameter2_name.setText(StringUtils.trimToEmpty(monitoringIndexDao.getParam_2_column()));
		textParameter2Value.setText(StringUtils.trimToEmpty(monitoringIndexDao.getParam_2_init_value()));
		
		textIndexName.setText(monitoringIndexDao.getIndex_nm());
		comboConditionType.setText(monitoringIndexDao.getCondition_type());
		textConditionValue.setText(monitoringIndexDao.getCondition_value());
		
		textExceptionIndexNM.setText(monitoringIndexDao.getException_index_nm());
		comboExceptionConditionType.setText(monitoringIndexDao.getException_condition_type());
		textExceptionConditionValue.setText(monitoringIndexDao.getException_condition_value());
		
		comboAfterProcess.setText(monitoringIndexDao.getAfter_type());
		textReceiver.setText(StringUtils.trimToEmpty(monitoringIndexDao.getReceiver()));
		
		comboIsResultSave.setText(monitoringIndexDao.getIs_result_save());
		comboIsSnapshotSave.setText(monitoringIndexDao.getIs_snapshot_save());
	}
	
	@Override
	protected void okPressed() {
		if("".equals(textTitle.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "Please input the Title");//Title은 공백이 될 수 없습니다.");
			textTitle.setFocus();
			return;
		}
		if("".equals(textQuery.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "Please input the Query");//Query은 공백이 될 수 없습니다.");
			textQuery.setFocus();
			return;
		}
		
		if(!MessageDialog.openConfirm(null, Messages.get().Confirm, "Do you want to modify?")) return;

		 MonitoringMainDAO mainDao = new MonitoringMainDAO();
		 mainDao.setSeq(monitoringIndexDao.getMonitoring_seq());
		 mainDao.setUser_seq(monitoringIndexDao.getUser_seq());
		 mainDao.setDb_seq(monitoringIndexDao.getSeq());
		 mainDao.setRead_method(comboMonitoringReadType.getText());
		 mainDao.setTitle(textTitle.getText());
		 mainDao.setDescription(textDescription.getText());
		 mainDao.setAdvice(textAdvice.getText());
		 
		 mainDao.setCron_exp("*/10 * * * * ?");
		 mainDao.setQuery(textQuery.getText());
		 mainDao.setIs_result_save(PublicTadpoleDefine.YES_NO.YES.name());
		 mainDao.setReceiver(textReceiver.getText());
		 
		 mainDao.setParam_1_column(textParameter1_name.getText());
		 mainDao.setParam_1_init_value(textParameter1Value.getText());
		 mainDao.setParam_2_column(textParameter2_name.getText());
		 mainDao.setParam_2_init_value(textParameter2Value.getText());
		 
		 mainDao.setIs_result_save(comboIsResultSave.getText());
		 mainDao.setIs_snapshot_save(comboIsSnapshotSave.getText());

		 monitoringIndexDao.setMonitoring_type(comboMonitoringType.getText());
		 monitoringIndexDao.setKpi_type(comboKPIType.getText());
		 monitoringIndexDao.setAfter_type(comboAfterProcess.getText());
		 monitoringIndexDao.setIndex_nm(textIndexName.getText());
		 monitoringIndexDao.setCondition_type(comboConditionType.getText());
		 monitoringIndexDao.setCondition_value(textConditionValue.getText());
		 
		 monitoringIndexDao.setException_index_nm(textExceptionIndexNM.getText());
		 monitoringIndexDao.setException_condition_type(comboExceptionConditionType.getText());
		 monitoringIndexDao.setException_condition_value(textExceptionConditionValue.getText());
		 
		 monitoringIndexDao.setReceiver(textReceiver.getText());		 

		try {
			TadpoleSystem_monitoring.updateMonitoring(mainDao, monitoringIndexDao);

			
		} catch (Exception e) {
			logger.error("update monitoring index", e);
		}
		 
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Update", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 600);
	}
}
