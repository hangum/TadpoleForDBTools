package com.hangum.db.browser.rap.core.dialog.dbconnect;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserDBQuery;
import com.hangum.db.util.ApplicationArgumentUtils;

/**
 * mysql login composite
 * 
 * @author hangum
 *
 */
public class MySQLLoginComposite extends AbstractLoginComposite {
	private static final Logger logger = Logger.getLogger(MySQLLoginComposite.class);
	
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	protected Combo comboLocale;
	protected Text textDisplayName;
	
	protected Button btnSavePreference;
	
	public MySQLLoginComposite(DBDefine selectDB, Composite parent, int style) {
		super(selectDB, parent, style);
		setText(selectDB.getDBToString());
	}

	@Override
	public void crateComposite() {
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblHost = new Label(compositeBody, SWT.NONE);
		lblHost.setText(Messages.DBLoginDialog_1);
		
		textHost = new Text(compositeBody, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabelPort = new Label(compositeBody, SWT.NONE);
		lblNewLabelPort.setText(Messages.DBLoginDialog_5);
		
		textPort = new Text(compositeBody, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabelDatabase = new Label(compositeBody, SWT.NONE);
		lblNewLabelDatabase.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblNewLabelDatabase.setText(Messages.DBLoginDialog_4);
		
		textDatabase = new Text(compositeBody, SWT.BORDER);
		textDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		
		
		Label lblUser = new Label(compositeBody, SWT.NONE);
		lblUser.setText(Messages.DBLoginDialog_2);
		
		textUser = new Text(compositeBody, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(compositeBody, SWT.NONE);
		lblPassword.setText(Messages.DBLoginDialog_3);
		
		textPassword = new Text(compositeBody, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLocale = new Label(compositeBody, SWT.NONE);
		lblLocale.setText(Messages.MySQLLoginComposite_lblLocale_text);
		
		comboLocale = new Combo(compositeBody, SWT.NONE);
		comboLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(selectDB == DBDefine.ORACLE_DEFAULT) {

			comboLocale.setVisibleItemCount(8);
			
			comboLocale.add("");
			
			comboLocale.add("ko");
			comboLocale.add("ja");
			comboLocale.add("zh");
			comboLocale.add("de");
			comboLocale.add("fr");
			comboLocale.add("it");
			comboLocale.add("en");
			comboLocale.select(0);
		} else if(selectDB == DBDefine.MYSQL_DEFAULT) {
			// http://dev.mysql.com/doc/refman//5.5/en/charset-charsets.html
			
			comboLocale.setVisibleItemCount(12);
			
			comboLocale.add("");
			comboLocale.add("armscii8 | ARMSCII-8 Armenian");
			comboLocale.add("ascii      | US ASCII");
			comboLocale.add("big5      | Big5 Traditional Chinese");
			comboLocale.add("binary    | Binary pseudo charset");
			comboLocale.add("cp850    | DOS West European");
			comboLocale.add("cp852    | DOS Central European");
			comboLocale.add("cp866    | DOS Russian");
			comboLocale.add("cp932    | SJIS for Windows Japanese");
			comboLocale.add("cp1250   | Windows Central European");
			comboLocale.add("cp1251   | Windows Cyrillic");
			comboLocale.add("cp1256   | Windows Arabic");
			comboLocale.add("cp1257   | Windows Baltic");
			comboLocale.add("dec8      | DEC West European");
			comboLocale.add("eucjpms  | UJIS for Windows Japanese");
			comboLocale.add("euckr     | EUC-KR Korean");
			comboLocale.add("gb2312   | GB2312 Simplified Chinese");
			comboLocale.add("gbk       | GBK Simplified Chinese");
			comboLocale.add("geostd8  | GEOSTD8 Georgian");
			comboLocale.add("greek     | ISO 8859-7 Greek");
			comboLocale.add("hebrew   | ISO 8859-8 Hebrew");
			comboLocale.add("hp8       | HP West European");
			comboLocale.add("keybcs2  | DOS Kamenicky Czech-Slovak");
			comboLocale.add("koi8r      | KOI8-R Relcom Russian");
			comboLocale.add("koi8u     | KOI8-U Ukrainian");
			comboLocale.add("latin1    | cp1252 West European");
			comboLocale.add("latin2    | ISO 8859-2 Central European");
			comboLocale.add("latin5    | ISO 8859-9 Turkish");
			comboLocale.add("latin7    | ISO 8859-13 Baltic");
			comboLocale.add("macce   | Mac Central European");
			comboLocale.add("macroman | Mac West European");
			comboLocale.add("sjis       | Shift-JIS Japanese");
			comboLocale.add("swe7    | 7bit Swedish");
			comboLocale.add("ucs2     | UCS-2 Unicode");
			comboLocale.add("tis620   | TIS620 Thai");
			comboLocale.add("ujis       | EUC-JP Japanese");
			comboLocale.add("utf8      | UTF-8 Unicode");
			comboLocale.add("utf8mb4 | UTF-8 Unicode");
			comboLocale.add("utf16     | UTF-16 Unicode");
			comboLocale.add("utf32     | UTF-32 Unicode");

			comboLocale.select(0);
		}
		
		Label lblNewLabel_1 = new Label(compositeBody, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText(Messages.DBLoginDialog_lblNewLabel_1_text);
		
		textDisplayName = new Text(compositeBody, SWT.BORDER);
		textDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnPing = new Button(compositeBody, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host 	= StringUtils.trimToEmpty(textHost.getText());
				String port 	= StringUtils.trimToEmpty(textPort.getText());
				
				if("".equals(host) || "".equals(port)) { //$NON-NLS-1$ //$NON-NLS-2$
					MessageDialog.openError(null, Messages.DBLoginDialog_10, Messages.DBLoginDialog_11);
					return;
				}
				
				try {
					if(isPing(host, port)) {
						MessageDialog.openInformation(null, Messages.DBLoginDialog_12, Messages.DBLoginDialog_13);
					} else {
						MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.DBLoginDialog_15);
					}
				} catch(NumberFormatException nfe) {
					MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
				}
			}
		});
		btnPing.setText(Messages.DBLoginDialog_btnPing_text);
		
		btnSavePreference = new Button(compositeBody, SWT.CHECK);
		btnSavePreference.setText(Messages.MySQLLoginComposite_btnSavePreference_text);
		btnSavePreference.setSelection(true);

		init();
	}
	
	@Override
	public void init() {
		if(ApplicationArgumentUtils.isTestMode()) {
			textHost.setText(Messages.DBLoginDialog_16);
			textUser.setText(Messages.DBLoginDialog_17);
			textPassword.setText(Messages.DBLoginDialog_18);
			textDatabase.setText(Messages.DBLoginDialog_19);
			textPort.setText(Messages.DBLoginDialog_20);
			
			textDisplayName.setText(Messages.DBLoginDialog_21);
		}
	}

	@Override
	public boolean connection() {
		if(!isValidate()) return false;

		String dbUrl = "";
		if(comboLocale.getText().trim().equals("")) {
			dbUrl = String.format(
					DBDefine.MYSQL_DEFAULT.getDB_URL_INFO(), 
					textHost.getText(), textPort.getText(), textDatabase.getText());
		} else {
			
			String selectLocale = StringUtils.substringBefore(comboLocale.getText(), "|");			
			
			dbUrl = String.format(
					DBDefine.MYSQL_DEFAULT.getDB_URL_INFO(), 
					textHost.getText(), textPort.getText(), textDatabase.getText() + "?Unicode=true&characterEncoding=" + selectLocale.trim());
		}
		logger.debug("[mysql dbURL]" + dbUrl);
		
		userDB = new UserDBDAO();
		userDB.setTypes(DBDefine.MYSQL_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setHost(textHost.getText());
		userDB.setPasswd(textPassword.getText());
		userDB.setPort(textPort.getText());
		userDB.setLocale(comboLocale.getText());
		userDB.setUsers(textUser.getText());
		
		// 이미 연결한 것인지 검사한다.
		if( !connectValite(userDB, textDatabase.getText()) ) return false;
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error(Messages.MySQLLoginComposite_0, e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.MySQLLoginComposite_2, errStatus); //$NON-NLS-1$
			}
		
		return true;
	}
	
	public boolean isValidate() {
		
		if(!message(textHost, "Host")) return false; //$NON-NLS-1$
		if(!message(textPort, "Port")) return false; //$NON-NLS-1$
		if(!message(textDatabase, "Database")) return false; //$NON-NLS-1$
		if(!message(textUser, "User")) return false; //$NON-NLS-1$
//		if(!message(textPassword, "Password")) return false; //$NON-NLS-1$
		if(!message(textDisplayName, "Display Name")) return false; //$NON-NLS-1$
		
		String host 	= StringUtils.trimToEmpty(textHost.getText());
		String port 	= StringUtils.trimToEmpty(textPort.getText());

		try {
			if(!isPing(host, port)) {
				MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.MySQLLoginComposite_8);
				return false;
			}
		} catch(NumberFormatException nfe) {
			MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
			return false;
		}
		
		return true;
	}
	
	protected boolean message(Text text, String msg) {
		if("".equals(StringUtils.trimToEmpty(text.getText()))) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.DBLoginDialog_10, msg + Messages.MySQLLoginComposite_10);
			text.setFocus();
			
			return false;
		}
		
		return true;
	}
	
}
