package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
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

import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine.MONITORING_STATUS;
import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.widgets.Group;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * monitoring status dialog
 * 
 * @author hangum
 *
 */
public class MonitoringStatusDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MonitoringStatusDialog.class);
	
	private UserDBDAO userDB;
	private TableViewer tvDetail ;
	private Text textDBName;
	private Text textDate;
	private Text textQueryResult;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MonitoringStatusDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(userDB.getDisplay_name() + " last monitoring data dialog"); //$NON-NLS-1$
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
		
		Composite compositeTitle = new Composite(container, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(2, false));
		
		Label lblDbName = new Label(compositeTitle, SWT.NONE);
		lblDbName.setText("DB Name");
		
		textDBName = new Text(compositeTitle, SWT.BORDER);
		textDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDate = new Label(compositeTitle, SWT.NONE);
		lblDate.setText("Date");
		
		textDate = new Text(compositeTitle, SWT.BORDER);
		textDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		tvDetail = new TableViewer(compositeHead, SWT.BORDER | SWT.FULL_SELECTION);
		tvDetail.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection iss = (IStructuredSelection)event.getSelection();
				if(!iss.isEmpty()) {
					MonitoringResultDAO dao = (MonitoringResultDAO)iss.getFirstElement();
					textQueryResult.setText(JSONUtil.getPretty(dao.getQuery_result()));
				}
			}
		});
		Table table = tvDetail.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQueryResult = new Group(container, SWT.NONE);
		grpQueryResult.setLayout(new GridLayout(1, false));
		grpQueryResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpQueryResult.setText("Query Result");
		
		textQueryResult = new Text(grpQueryResult, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQueryResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumns();
		
		tvDetail.setContentProvider(ArrayContentProvider.getInstance());
		tvDetail.setLabelProvider(new MonitoringStatusLabelProvider());
		
		initUI();

		return container;
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		try {
			List<MonitoringResultDAO> listMonitoringResult = TadpoleSystem_monitoring.getMonitoringStatus(userDB.getSeq());
			
			textDBName.setText(userDB.getDisplay_name());
			if(!listMonitoringResult.isEmpty()) {
				MonitoringResultDAO dao = listMonitoringResult.get(0);
				textDate.setText(dao.getCreate_time().toString());	
			}
			
			tvDetail.setInput(listMonitoringResult);
			tvDetail.refresh();
		} catch(Exception e) {
			logger.error("Get last monitoring status", e);
		}
	}
	
	/**
	 * crate table column
	 */
	private void createColumns() {
		String[] names = {"Type", "KPI", "Result", "Title", "Description", "Index Value", "System Description", "Advice", "User Description"};
		int[] intWidth = {100, 95, 80, 100, 80, 200,
						100, 100, 100};
		
		for(int i=0; i<names.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvDetail, SWT.NONE);
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
		createButton(parent, IDialogConstants.OK_ID, "Close", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 650);
	}
}

/**
 * monitoring status label provider
 * @author hangum
 *
 */
class MonitoringStatusLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {

	@Override
	public Color getForeground(Object element) {
		MonitoringResultDAO dao = (MonitoringResultDAO)element;
		if(MONITORING_STATUS.WARRING.toString().equals(dao.getResult())) {
			return SWTResourceManager.getColor(MONITORING_STATUS.WARRING.getColor());
		} else if(MONITORING_STATUS.CRITICAL.toString().equals(dao.getResult())) {
			return SWTResourceManager.getColor(MONITORING_STATUS.CRITICAL.getColor());
		}
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MonitoringResultDAO dao = (MonitoringResultDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getMonitoring_type();
		case 1: return dao.getKpi_type();
		case 2: return dao.getResult();
		case 3: return dao.getTitle();
		case 4: return dao.getDescription();
		case 5: return dao.getIndex_value();
		case 6: return dao.getSystem_description();
		case 7: return dao.getAdvice();
		case 8: return dao.getUser_description();
		}
		
		return null;
	}
}
