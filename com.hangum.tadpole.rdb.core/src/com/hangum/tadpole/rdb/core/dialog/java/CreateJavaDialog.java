/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJavaDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDDLCommand;
import com.hangum.tadpole.engine.utils.RequestQueryUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Java object 실행 다이얼로그.
 * 
 * @author nilriri
 * 
 */
public class CreateJavaDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(CreateJavaDialog.class);
	
	protected int ID_CREATE_JAVA = IDialogConstants.CLIENT_ID + 1;
	protected int ID_CHANGE_JAVA = IDialogConstants.CLIENT_ID + 2;
	protected int ID_DROP_JAVA = IDialogConstants.CLIENT_ID + 3;

	private UserDBDAO userDB;
	private OracleJavaDAO javaDao;

	private Text textScript;
	private Group grpTables;
	private Label lblJavaName;
	private Text textJavaName;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param userDB
	 * @param javaDao
	 */
	public CreateJavaDialog(Shell parentShell, UserDBDAO userDB, OracleJavaDAO javaDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

		this.userDB = userDB;
		this.javaDao = javaDao;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Java Object Manager");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite containerInput = (Composite) super.createDialogArea(parent);
		GridLayout gl_containerInput = (GridLayout) containerInput.getLayout();
		gl_containerInput.verticalSpacing = 1;
		gl_containerInput.horizontalSpacing = 1;
		gl_containerInput.marginHeight = 1;
		gl_containerInput.marginWidth = 1;

		Composite compositeHead = new Composite(containerInput, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));

		lblJavaName = new Label(compositeHead, SWT.NONE);
		lblJavaName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJavaName.setText("Java Name");

		textJavaName = new Text(compositeHead, SWT.BORDER);
		textJavaName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textJavaName.setText(this.javaDao.getObjectName());

		SashForm sashForm = new SashForm(containerInput, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		grpTables = new Group(sashForm, SWT.NONE);
		GridLayout gl_grpTables = new GridLayout(1, false);
		gl_grpTables.horizontalSpacing = 2;
		gl_grpTables.verticalSpacing = 2;
		gl_grpTables.marginHeight = 2;
		gl_grpTables.marginWidth = 2;
		grpTables.setLayout(gl_grpTables);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTables.setText("Java Source");

		textScript = new Text(grpTables, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		textScript.setText(getJavaSource());

		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return containerInput;
	}

	private String getJavaSource() {

		SqlMapClient sqlClient;
		StringBuffer result = new StringBuffer();
		try {
			sqlClient = TadpoleSQLManager.getInstance(userDB);
			Map<String, String> param = new HashMap<String, String>();
			param.put("schema_name", javaDao.getSchema_name());
			param.put("object_name", javaDao.getObjectName());

			List<String> source = sqlClient.queryForList("getJavaSource", param); //$NON-NLS-1$
			for (String line : source) {
				if (StringUtils.isBlank(line)) {
					result.append("\n");
				} else {
					result.append(line + "\n");
				}
			}
			return result.toString();
		} catch (Exception e) {
			logger.error("java object", e);
			return "";
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (StringUtils.isBlank(javaDao.getObjectName())) {
			createButton(parent, ID_CREATE_JAVA, Messages.get().CreateJava, false);
		} else {
			createButton(parent, ID_CREATE_JAVA, Messages.get().ChangeJava, false);
			createButton(parent, ID_DROP_JAVA, Messages.get().DropJava, false);
		}
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Close, false);
	}

	private String getCreateScript() {
		StringBuffer script = new StringBuffer();
		StringBuffer source = new StringBuffer();

		String object_name = (StringUtils.isBlank(this.javaDao.getSchema_name()) ? userDB.getSchema() : this.javaDao.getSchema_name());

		object_name += ".\"" + this.textJavaName.getText().trim() + "\"";

		source.append("CREATE OR REPLACE AND RESOLVE JAVA SOURCE NAMED " + object_name + " as\n");
		source.append(this.textScript.getText());

		script.append("begin\n");
		script.append(String.format("EXECUTE IMMEDIATE '%s';\n", source));
		script.append("dbms_output.put_line(sqlerrm);");
		script.append("end;\n");

		return script.toString();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == ID_CREATE_JAVA) {
			
			try {
				ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, getCreateScript())); //$NON-NLS-1$
				
				MessageDialog.openInformation(this.getShell(), CommonMessages.get().Information, Messages.get().CreateOrChangedJavaObject);
				this.okPressed();
			} catch (Exception e) {
				logger.error(e);
				
				MessageDialog.openError(this.getShell(),CommonMessages.get().Error, Messages.get().CreateOrChangedErrorJavaObject + e.getMessage());
			}
		} else if (buttonId == ID_DROP_JAVA) {
			try {
				ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, "DROP JAVA SOURCE " + javaDao.getFullName() + " ")); //$NON-NLS-1$
				MessageDialog.openInformation(this.getShell(), CommonMessages.get().Information, Messages.get().DeletedJavaObject);
				this.okPressed();
			} catch (Exception e) {
				logger.error(e);
				
				MessageDialog.openError(this.getShell(),CommonMessages.get().Error, Messages.get().DeletedErrorJavaObject + e.getMessage());
			}
		} else {
			okPressed();
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 700);
	}
}
