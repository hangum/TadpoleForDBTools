package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringDashboardDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.Messages;

/**
 * show monitoring stauts dialog
 * 
 * @author hangum
 *
 */
public class MonitoringDetailStatusDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MonitoringDetailStatusDialog.class);
	
	private int ALL_CONFORM_BTN_ID = IDialogConstants.CLIENT_ID +1;
	
	private MonitoringDashboardDAO dao;
	private Text textDBName;
	private Text textTitle;
	private Text textDescription;
	private Text textAdvice;
	private Text textWarningCnt;
	private Text textCriticalCnt;

	private TableViewer tvList;
	private Text textSnapshot;
	private Text textQueryResult;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MonitoringDetailStatusDialog(Shell parentShell, MonitoringDashboardDAO dao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.dao = dao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(dao.getTitle() + " monitoring detail dialog"); //$NON-NLS-1$
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
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		compositeBody.setLayout(new GridLayout(4, false));
		
		Label lblDbName = new Label(compositeBody, SWT.NONE);
		lblDbName.setText("DB Name");
		
		textDBName = new Text(compositeBody, SWT.BORDER);
		textDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblTitle = new Label(compositeBody, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeBody, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblDescription = new Label(compositeBody, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_textDescription.heightHint = 40;
		gd_textDescription.minimumHeight = 40;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblAdvice = new Label(compositeBody, SWT.NONE);
		lblAdvice.setText("Advice");
		
		textAdvice = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textAdvice = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_textAdvice.minimumHeight = 40;
		gd_textAdvice.heightHint = 40;
		textAdvice.setLayoutData(gd_textAdvice);
		
		Label lblWarringCount = new Label(compositeBody, SWT.NONE);
		lblWarringCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWarringCount.setText("Warring Count");
		
		textWarningCnt = new Text(compositeBody, SWT.BORDER);
		textWarningCnt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(compositeBody, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblNewLabel.setText("Critical Count");
		
		textCriticalCnt = new Text(compositeBody, SWT.BORDER);
		textCriticalCnt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpErrorList = new Group(sashForm, SWT.NONE);
		grpErrorList.setLayout(new GridLayout(1, false));
		grpErrorList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpErrorList.setText("Error List");
		
		tvList = new TableViewer(grpErrorList, SWT.BORDER | SWT.FULL_SELECTION);
		tvList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection iss = (IStructuredSelection)tvList.getSelection();
				if(!iss.isEmpty()) {
					MonitoringResultDAO dao = (MonitoringResultDAO)iss.getFirstElement();
					
					textQueryResult.setText(dao.getQuery_result());
					try {
						MonitoringResultDAO resultDao = TadpoleSystem_monitoring.getMonitoringResult(dao);
						textSnapshot.setText(resultDao.getSnapshot());
					} catch (Exception e) {
						logger.error("get monitoring lsit", e);
					}
				}
				
			}
		});
		Table table = tvList.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQueryResult = new Group(sashForm, SWT.NONE);
		grpQueryResult.setText("Query Result");
		grpQueryResult.setLayout(new GridLayout(1, false));
		
		textQueryResult = new Text(grpQueryResult, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQueryResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpSystemBackdata = new Group(sashForm, SWT.NONE);
		grpSystemBackdata.setText("Snapshot");
		grpSystemBackdata.setLayout(new GridLayout(1, false));
		
		textSnapshot = new Text(grpSystemBackdata, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textSnapshot.setText("");
		textSnapshot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setWeights(new int[] {3, 3, 4});
		
		createTableColumn();
		
		tvList.setContentProvider(ArrayContentProvider.getInstance());
		tvList.setLabelProvider(new MonitoringResultLabelprovider());
		
		initUI();
		
		return container;
	}
	
	private void initUI() {
		
		textDBName.setText(dao.getDisplay_name());
		textTitle.setText(dao.getTitle());
		textDescription.setText(dao.getDescription());
		textAdvice.setText(dao.getAdvice());
		textWarningCnt.setText(""+dao.getWarring_cnt());
		textCriticalCnt.setText(""+dao.getCritical_cnt());
		
		try {
			List<MonitoringResultDAO> listMonitoringResult = 
					TadpoleSystem_monitoring.getMonitoringResultStatus(dao.getMonitoring_seq(), dao.getMonitoring_index_seq(), "NO", "'WARRING', 'CRITICAL'");
			
			tvList.setInput(listMonitoringResult);
			tvList.refresh();
		} catch (Exception e) {
			logger.error("get monitoring lsit", e);
		}
		
	}

	/**
	 * create result column
	 */
	private void createTableColumn() {
		String[] arryTable 	= {"Rise Date", "Index", "Result Type", "System Description"};//, "Query Result"};
		int[] arryWidth 	= {120, 		80,  			90,		200, 				};//300};
	
		for(int i=0; i<arryTable.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvList, SWT.NONE);
			TableColumn tblclmnDbName = tableViewerColumn.getColumn();
			tblclmnDbName.setWidth(arryWidth[i]);
			tblclmnDbName.setText(arryTable[i]);
		}
	}
	
	@Override
	protected void okPressed() {
		IStructuredSelection iss = (IStructuredSelection)tvList.getSelection();
		if(!iss.isEmpty()) {
			MonitoringResultDAO dao = (MonitoringResultDAO)iss.getFirstElement();
			
			try {
				InputDialog inputDialog=new InputDialog(getShell(), "User Confirm Message", "User Confirm Message", "", null);
				if(Window.OK == inputDialog.open()) {
					if(MessageDialog.openConfirm(null, CommonMessages.get().Confirm, "Do you want to checked the data?")) { //"사용자 확인으로 처리 하시겠습니까?"
						String inputMsg = inputDialog.getValue();
						
						TadpoleSystem_monitoring.updateUserConfirmMsg(dao.getSeq(), inputMsg);
						
						MessageDialog.openInformation(null, CommonMessages.get().Confirm, "Data saved");// "처리되었습니다.");
					}
				}
				
			} catch (Exception e) {
				logger.error("Update user confirm", e);
			}
		} else {
			MessageDialog.openWarning(null, CommonMessages.get().Warning, "Please selected data.");
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == ALL_CONFORM_BTN_ID) {
			try {
				InputDialog inputDialog=new InputDialog(getShell(), "User Confirm Message", "User Confirm Message", "", null);
				if(Window.OK == inputDialog.open()) {
					if(MessageDialog.openConfirm(null, CommonMessages.get().Confirm, "Do you want to checked the data?")) { //"사용자 확인으로 처리 하시겠습니까?"
						String inputMsg = inputDialog.getValue();
						
						TadpoleSystem_monitoring.updateUserConfirmMsg(dao.getMonitoring_seq(), dao.getMonitoring_index_seq(), inputMsg);
						MessageDialog.openInformation(null, CommonMessages.get().Confirm, "Data saved");// "처리되었습니다.");
					}
				}
				
			} catch (Exception e) {
				logger.error("Update user confirm", e);
			}
		}

		super.buttonPressed(buttonId);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, ALL_CONFORM_BTN_ID, 	"All Read Confirm", true);
		createButton(parent, IDialogConstants.OK_ID, "ADD User Confirm", true);
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

/**
 * monitoring result lable provider
 * 
 * @author hangum
 *
 */
class MonitoringResultLabelprovider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MonitoringResultDAO dao = (MonitoringResultDAO)element;
			
		switch(columnIndex) {
			case 0: return dao.getCreate_time().toString();
			case 1: return dao.getIndex_value();
			case 2: return dao.getResult();
			case 3: return dao.getSystem_description();
//			case 4: return dao.getQuery_result();
		}
	
		return null;
	}
}

