package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import java.text.ParseException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.tadpole.monitoring.core.Messages;
import com.hangum.tadpole.monitoring.core.utils.Utils;

/**
 * Add monitoring Dialog
 * 
 * @author hangum
 *
 */
public class AddMonitoringDialog extends Dialog {
	private Text textTitle;
	private Text textDescription;
	private Combo comboMonitoringReadType;
	private Text textCronExp;
	private Text textViewScheule;
	private Table table;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddMonitoringDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add monitoring schedule"); //$NON-NLS-1$
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
		compositeMoni.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeMoni.setLayout(new GridLayout(3, false));
		
		Label lblTitle = new Label(compositeMoni, SWT.NONE);
		lblTitle.setBounds(0, 0, 60, 14);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeMoni, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblDescription = new Label(compositeMoni, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeMoni, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_textDescription.heightHint = 50;
		gd_textDescription.minimumHeight = 60;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblMonitoringType = new Label(compositeMoni, SWT.NONE);
		lblMonitoringType.setText("Read Type");
		
		comboMonitoringReadType = new Combo(compositeMoni, SWT.READ_ONLY);
		comboMonitoringReadType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboMonitoringReadType.add("SQL");
//		comboMonitoringReadType.add("PL/SQL");
//		comboMonitoringReadType.add("Rest API");
		comboMonitoringReadType.select(0);
		
		Label lblCropExp = new Label(compositeMoni, SWT.NONE);
//		lblCropExp.setText("<a href='http://www.cronmaker.com/' target='_blank'>Cron Expression</a>"); //$NON-NLS-1$
		lblCropExp.setText("Cron Expression</a>"); //$NON-NLS-1$
		lblCropExp.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		textCronExp = new Text(compositeMoni, SWT.BORDER);
		textCronExp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnViewSchedule = new Button(compositeMoni, SWT.NONE);
		btnViewSchedule.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					textViewScheule.setText( Utils.showExp(textCronExp.getText()) );
				} catch (ParseException ee) {
					MessageDialog.openError(null, Messages.AddScheduleDialog_20, Messages.AddScheduleDialog_12);
					textCronExp.setFocus();
				}
			}
		});
		btnViewSchedule.setText("View Schedule");
		new Label(compositeMoni, SWT.NONE);
		
		textViewScheule = new Text(compositeMoni, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textViewScheule.setEditable(true);
		GridData gd_textViewScheule = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_textViewScheule.heightHint = 60;
		textViewScheule.setLayoutData(gd_textViewScheule);
		
		Group grpAddCheckPoint = new Group(container, SWT.NONE);
		grpAddCheckPoint.setLayout(new GridLayout(1, false));
		grpAddCheckPoint.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpAddCheckPoint.setText("Add Check point");
		
		ToolBar toolBar = new ToolBar(grpAddCheckPoint, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setText("Add");
		
		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setEnabled(false);
		tltmDelete.setText("Delete");
		
		TableViewer tableViewer = new TableViewer(grpAddCheckPoint, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn.getColumn();
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText("Title");
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnType = tableViewerColumn_5.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnVariable = tableViewerColumn_2.getColumn();
		tblclmnVariable.setWidth(100);
		tblclmnVariable.setText("variable");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnConditionType = tableViewerColumn_3.getColumn();
		tblclmnConditionType.setWidth(100);
		tblclmnConditionType.setText("Condition Type");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnReferenceValue = tableViewerColumn_4.getColumn();
		tblclmnReferenceValue.setText("Reference Value");
		tblclmnReferenceValue.setWidth(100);

		return container;
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
		return new Point(533, 506);
	}
}
