package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringMainDAO;
import com.hangum.tadpole.engine.query.dao.system.sql.template.TeadpoleMonitoringTemplateDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_Template;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.Messages;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Add monitoring Dialog
 * 
 * @author hangum
 *
 */
@SuppressWarnings("serial")
public class AddMonitoringDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AddMonitoringDialog.class);
	
	private UserDBDAO userDB;
	
	private TableViewer tvTemplate;
	
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
	public AddMonitoringDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add Monitoring Index"); //$NON-NLS-1$
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
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpTemplate = new Group(sashForm, SWT.NONE);
		grpTemplate.setText("Template");
		grpTemplate.setLayout(new GridLayout(1, false));
		
		tvTemplate = new TableViewer(grpTemplate, SWT.BORDER | SWT.FULL_SELECTION);
		tvTemplate.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection select = (IStructuredSelection)event.getSelection();
				if(!select.isEmpty()) {
					TeadpoleMonitoringTemplateDAO dao = (TeadpoleMonitoringTemplateDAO)select.getFirstElement();
					selectTemplateData(dao);	
				}
				
			}
		});
		Table table = tvTemplate.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumns();
		
		tvTemplate.setContentProvider(new ArrayContentProvider());
		tvTemplate.setLabelProvider(new MonitoringLabelProvider());
		
		Composite compositeMoni = new Composite(sashForm, SWT.NONE);
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
		
		sashForm.setWeights(new int[] {3, 7});
		
		initUI();

		return container;
	}

	/**
	 * ui initialize
	 */
	private void initUI() {
		try {
			List<TeadpoleMonitoringTemplateDAO> listTemplateDao = TadpoleSystem_Template.getMonitoringTemplate(userDB.getDBDefine());
			tvTemplate.setInput(listTemplateDao);
			tvTemplate.refresh();
		} catch(Exception e) {
			logger.error("Get template list", e);
		}
	}
	
	/**
	 * select template data
	 * 
	 * @param dao
	 */
	private void selectTemplateData(TeadpoleMonitoringTemplateDAO dao) {
		
		comboMonitoringType.setText(dao.getMonitoring_type());
		comboKPIType.setText(dao.getKpi_type());
		
		textTitle.setText(dao.getTitle());
		textDescription.setText(dao.getDescription());
		textAdvice.setText(dao.getAdvice());
		comboMonitoringReadType.setText("SQL");
		textQuery.setText(dao.getQuery());
		
		textParameter1_name.setText(StringUtils.trimToEmpty(dao.getParam_1_column()));
		textParameter1Value.setText(StringUtils.trimToEmpty(dao.getParam_1_init_value()));
		textParameter2_name.setText(StringUtils.trimToEmpty(dao.getParam_2_column()));
		textParameter2Value.setText(StringUtils.trimToEmpty(dao.getParam_2_init_value()));
		
		textIndexName.setText(dao.getIndex_nm());
		comboConditionType.setText(dao.getCondition_type());
		textConditionValue.setText(dao.getCondition_value());
		
		textExceptionIndexNM.setText(dao.getException_index_nm());
		comboExceptionConditionType.setText(dao.getException_condition_type());
		textExceptionConditionValue.setText(dao.getException_condition_value());
		
		comboAfterProcess.setText(dao.getAfter_type());
		textReceiver.setText(SessionManager.getEMAIL());
		
		comboIsResultSave.setText(dao.getIs_result_save());
		comboIsSnapshotSave.setText(dao.getIs_snapshot_save());
	}
	
	@Override
	protected void okPressed() {
		if("".equals(textTitle.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "Title column is not empay.");
			textTitle.setFocus();
			return;
		}
		if("".equals(textQuery.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "Query column is not empty");
			textQuery.setFocus();
			return;
		}

		 MonitoringMainDAO mainDao = new MonitoringMainDAO();
		 mainDao.setUser_seq(userDB.getUser_seq());
		 mainDao.setDb_seq(userDB.getSeq());
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

		 MonitoringIndexDAO indexDao = new MonitoringIndexDAO();
		 indexDao.setMonitoring_seq(mainDao.getSeq());
		 
		 indexDao.setMonitoring_type(comboMonitoringType.getText());
		 indexDao.setKpi_type(comboKPIType.getText());
		 indexDao.setAfter_type(comboAfterProcess.getText());
		 indexDao.setIndex_nm(textIndexName.getText());
		 indexDao.setCondition_type(comboConditionType.getText());
		 indexDao.setCondition_value(textConditionValue.getText());
		 
		 indexDao.setException_index_nm(textExceptionIndexNM.getText());
		 indexDao.setException_condition_type(comboExceptionConditionType.getText());
		 indexDao.setException_condition_value(textExceptionConditionValue.getText());
		 
		 indexDao.setReceiver(textReceiver.getText());

		try {
			TadpoleSystem_monitoring.saveMonitoring(mainDao, indexDao);
			MessageDialog.openInformation(null, Messages.get().Confirm, "Save data.");
		} catch (Exception e) {
			logger.error("save monitoring index", e);
			
			MessageDialog.openError(null, Messages.get().Error, e.getMessage());
		}
	}
	
	/**
	 * crate table column
	 */
	private void createColumns() {
		String[] names = {"Type", "KPI", "Title", "Description", "Query", 
					"param 1 column", "param 1 value", "param 2 column", "param 2 value", 
					"Index Name", "Condition Type", "Condition Value"};
		int[] intWidth = {120, 90, 100, 150, 200,
						100, 100, 100, 100, 
						100, 100, 100};
		
		for(int i=0; i<names.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvTemplate, SWT.NONE);
			TableColumn tblclmnDbName = tableViewerColumn.getColumn();
			tblclmnDbName.setText(names[i]);
			tblclmnDbName.setWidth(intWidth[i]);
		}
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
		return new Point(700, 805);
	}
}
