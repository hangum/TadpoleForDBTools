package com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.MonitoringStatusDialog;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine;
import com.swtdesigner.SWTResourceManager;

/**
 * monitoring status composite
 * 
 * @author hangum
 *
 */
public class DBStatusComposite extends Composite {

	private UserDBDAO userDB;
	private Button btnDB;
			
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DBStatusComposite(final Composite parent, int style, final UserDBDAO userDB) {
		super(parent, style);
		
		this.userDB = userDB;
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.widthHint = 85;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(1, false));
		
		btnDB = new Button(composite, SWT.NONE);
		GridData gd_btnDB = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDB.heightHint = 70;
		gd_btnDB.widthHint = 85;
		btnDB.setLayoutData(gd_btnDB);
		btnDB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MonitoringStatusDialog dialog = new MonitoringStatusDialog(parent.getShell(), userDB);
				dialog.open();
			}
		});
		btnDB.setBackground(SWTResourceManager.getColor(MonitoringDefine.MONITORING_STATUS.CLEAN.getColor()));
		
		Label lblDBName = new Label(composite, SWT.NONE | SWT.WRAP);
		lblDBName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblDBName.setSize(63, 24);
		lblDBName.setText(userDB.getDisplay_name());
	}
	
	/**
	 * change status
	 *  
	 * @param intBtnColor
	 * @param msg
	 */
	public void changeStatus(int intBtnColor, String msg) {
		btnDB.setBackground(SWTResourceManager.getColor(intBtnColor));
		btnDB.setText(msg);
		btnDB.getParent().redraw();
	}

	/**
	 * change status 
	 *  
	 * @param intBtnColor
	 * @param msg
	 */
	public void changeStatusAdd(int intBtnColor, String msg) {
		btnDB.setBackground(SWTResourceManager.getColor(intBtnColor));
		btnDB.setText(btnDB.getText() + ", " + msg);
		btnDB.getParent().redraw();
	}
	
	@Override
	protected void checkSubclass() {
	}
}
