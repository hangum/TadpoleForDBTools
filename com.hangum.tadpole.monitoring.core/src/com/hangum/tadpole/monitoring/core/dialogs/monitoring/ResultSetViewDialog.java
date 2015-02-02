package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;

/**
 * ResultSet view dialog
 * 
 * @author hangum
 *
 */
public class ResultSetViewDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResultSetViewDialog.class);
	private MonitoringResultDAO dao;
	private Text textMessage;
	
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

		newShell.setText("ResultSet Viewer"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		textMessage = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);//, EditorDefine.EXT_JSON, "", "");
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		initUI();

		return container;
	}
	
	private void initUI() {
		try {
			textMessage.setText(JSONUtil.getPretty(dao.getQuery_result() + dao.getQuery_result2()));
		} catch(Exception e) {
			logger.error("server status", e); //$NON-NLS-1$
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 442);
	}

}
