package com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.MonitoringMainEditor;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
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
	public DBStatusComposite(Composite parent, int style, UserDBDAO userDB) {
		super(parent, style);
		
		this.userDB = userDB;
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		btnDB = new Button(composite, SWT.NONE);
		GridData gd_btnDB = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDB.heightHint = 70;
		gd_btnDB.widthHint = 70;
		btnDB.setLayoutData(gd_btnDB);
		btnDB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnDB.setBackground(SWTResourceManager.getColor(MonitoringMainEditor.STATUS_COLOR_CLEAN));
		btnDB.setText("0");
		
		Label lblDBName = new Label(composite, SWT.NONE | SWT.WRAP);
		lblDBName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblDBName.setSize(63, 24);
		lblDBName.setText(userDB.getDisplay_name());
		
	}
	
	/**
	 * 
	 * @param intBtnColor
	 * @param msg
	 */
	public void changeStatus(int intBtnColor, String msg) {
		btnDB.setBackground(SWTResourceManager.getColor(intBtnColor));
		btnDB.setText(msg);
		btnDB.getParent().redraw();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
