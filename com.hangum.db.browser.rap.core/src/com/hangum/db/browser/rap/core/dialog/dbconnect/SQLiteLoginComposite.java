package com.hangum.db.browser.rap.core.dialog.dbconnect;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.system.TadpoleSystem_UserDBQuery;
import com.hangum.db.util.ApplicationArgumentUtils;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

/**
 * sqlite login composite
 * 
 * @author hangum
 *
 */
public class SQLiteLoginComposite extends AbstractLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -444340316081961365L;

	private static final Logger logger = Logger.getLogger(SQLiteLoginComposite.class);
	
	protected Text textFile;
	protected Text textDisplayName;
	
	protected Button btnSavePreference;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SQLiteLoginComposite(Composite parent, int style) {
		super(DBDefine.SQLite_DEFAULT, parent, style);
		setText(Messages.SQLiteLoginComposite_0);
	}
	
	@Override
	protected void crateComposite() {
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite container = new Composite(this, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(3, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblDbFile = new Label(compositeBody, SWT.NONE);
		lblDbFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDbFile.setText(Messages.SQLiteLoginComposite_1);
		
		textFile = new Text(compositeBody, SWT.BORDER);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		
		Label lblDisplayName = new Label(compositeBody, SWT.NONE);
		lblDisplayName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDisplayName.setText(Messages.SQLiteLoginComposite_2);
		
		textDisplayName = new Text(compositeBody, SWT.BORDER);
		textDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		new Label(compositeBody, SWT.NONE);
		
		btnSavePreference = new Button(compositeBody, SWT.CHECK);
		btnSavePreference.setText(Messages.SQLiteLoginComposite_btnSavePreference_text);
		btnSavePreference.setSelection(true);
		new Label(compositeBody, SWT.NONE);
		
		init();
	}

	@Override
	protected void init() {
		if(ApplicationArgumentUtils.isTestMode()) {
//			textFile.setText("C:/dev/eclipse-rcp-indigo-SR2-win32/workspace/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/configuration/tadpole/db/tadpole-system.db");//Messages.SQLiteLoginComposite_3); //$NON-NLS-1$
			textFile.setText("C:/tadpole-test.db");//Messages.SQLiteLoginComposite_3); //$NON-NLS-1$
			textDisplayName.setText(Messages.SQLiteLoginComposite_4);
		}
	}

	@Override
	protected boolean connection() {
		String strFile = StringUtils.trimToEmpty(textFile.getText());
		
		if("".equals( strFile ) ) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_7);
			return false;
		} else if("".equals(StringUtils.trimToEmpty(textDisplayName.getText()))) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_12 );
			return false;
		}
		
		if( !new File(strFile).exists() ) {
			if( !MessageDialog.openConfirm(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_9) ) return false; 
		}
		
		final String dbUrl = String.format(DBDefine.SQLite_DEFAULT.getDB_URL_INFO(), textFile.getText());
		
		userDB = new UserDBDAO();
		userDB.setType(DBDefine.SQLite_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDatabase(textFile.getText());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setPasswd(""); //$NON-NLS-1$
		userDB.setUser(""); //$NON-NLS-1$
		
		// 이미 연결한 것인지 검사한다.
		if( !connectValite(userDB, textFile.getText()) ) return false;
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB);
			} catch (Exception e) {
				logger.error(Messages.SQLiteLoginComposite_8, e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
			}
		
		return true;
		
	}

}
