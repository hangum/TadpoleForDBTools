package com.hangum.tadpole.rdb.core.dialog.table;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

/**
 * Table 수정 다이얼로그.
 * 
 * @author hangum
 *
 */
public class AlterTableDialog extends Dialog {
	private UserDBDAO userDB;
	private TableDAO tableDao;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public AlterTableDialog(Shell parentShell, final UserDBDAO userDB) {
		super(parentShell);
		
		this.userDB = userDB;
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AlterTableDialog(Shell parentShell, final UserDBDAO userDB, TableDAO tableDao) {
		super(parentShell);
		
		this.userDB = userDB;
		this.tableDao = tableDao;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Table : " + tableDao.getName());

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
