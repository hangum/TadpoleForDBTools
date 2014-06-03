package com.hangum.tadpole.monitoring.core.editors.cron;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class CronMakerDialog extends Dialog {
	private Text textEveryHours;
	private Text textSQL;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CronMakerDialog(Shell parentShell) {
		super(parentShell);
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
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group groupExpress = new Group(sashForm, SWT.NONE);
		groupExpress.setText("Generate cron expression");
		groupExpress.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(groupExpress, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmHourly = new TabItem(tabFolder, SWT.NONE);
		tbtmHourly.setText("Hourly");
		
		Composite compositeHourly = new Composite(tabFolder, SWT.NONE);
		tbtmHourly.setControl(compositeHourly);
		compositeHourly.setLayout(new GridLayout(2, false));
		
		Button btnEvery = new Button(compositeHourly, SWT.RADIO);
		btnEvery.setSelection(true);
		btnEvery.setText("Every");
		
		Composite compositeHourlyEvery = new Composite(compositeHourly, SWT.NONE);
		compositeHourlyEvery.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHourlyEvery.setLayout(new GridLayout(2, false));
		
		textEveryHours = new Text(compositeHourlyEvery, SWT.BORDER);
		
		Label lblHours = new Label(compositeHourlyEvery, SWT.NONE);
		lblHours.setText("hour(s)");
		
		Button btnHourlyAt = new Button(compositeHourly, SWT.RADIO);
		btnHourlyAt.setText("At");
		
		Composite compositeHourlyAt = new Composite(compositeHourly, SWT.NONE);
		compositeHourlyAt.setLayout(new GridLayout(2, false));
		compositeHourlyAt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Combo comboHourlyTime = new Combo(compositeHourlyAt, SWT.NONE);
		GridData gd_comboHourlyTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboHourlyTime.minimumWidth = 50;
		gd_comboHourlyTime.widthHint = 50;
		comboHourlyTime.setLayoutData(gd_comboHourlyTime);
		
		Combo comboHourlyMinute = new Combo(compositeHourlyAt, SWT.NONE);
		GridData gd_comboHourlyMinute = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboHourlyMinute.minimumWidth = 50;
		gd_comboHourlyMinute.widthHint = 50;
		comboHourlyMinute.setLayoutData(gd_comboHourlyMinute);
		
		TabItem tbtmDaily = new TabItem(tabFolder, SWT.NONE);
		tbtmDaily.setText("Daily");
		
		TabItem tbtmWeekly = new TabItem(tabFolder, SWT.NONE);
		tbtmWeekly.setText("Weekly");
		
		TabItem tbtmMonthly = new TabItem(tabFolder, SWT.NONE);
		tbtmMonthly.setText("Monthly");
		
		Group grpUserQuery = new Group(sashForm, SWT.NONE);
		grpUserQuery.setText("User Query");
		grpUserQuery.setLayout(new GridLayout(1, false));
		
		textSQL = new Text(grpUserQuery, SWT.BORDER);
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setWeights(new int[] {4, 6});

		return container;
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
		return new Point(652, 600);
	}
}
