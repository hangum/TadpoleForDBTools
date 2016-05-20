/*******************************************************************************
ㅑㅜ * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBWithoutTunnelingGroup;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * sqlite login composite
 * 
 * @author hangum
 *
 */
public class SQLiteLoginComposite extends AbstractLoginComposite {
	private static final Logger logger = Logger.getLogger(SQLiteLoginComposite.class);
	private String ROOT_RESOURCE_DIR = ApplicationArgumentUtils.getResourcesDir() + SessionManager.getEMAIL() + PublicTadpoleDefine.DIR_SEPARATOR;
	private static final String INITIAL_TEXT = "No files uploaded."; //$NON-NLS-1$
	
	protected Group grpConnectionType;
	private Text fileNameLabel;
	private Text textCreationDB;
	private Text textFileLocationDB;

	private Button chkBtnFileUpload;
	private Button chkBtnCreationDb;
	private Button chkBtnFileLocationDb;
	
	private FileUpload fileUpload;
	private DiskFileUploadReceiver receiver;
	private ServerPushSession pushSession;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SQLiteLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super(Messages.get().SQLiteLoginComposite_11, DBDefine.SQLite_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
	@Override
	protected void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.get().MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(3, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.get().DatabaseInformation);
		
		chkBtnFileUpload = new Button(grpConnectionType, SWT.RADIO);
		chkBtnFileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				uiInit(false);
			}
		});
		chkBtnFileUpload.setText(Messages.get().SQLiteLoginComposite_btnFileUpload_text);
		
		fileNameLabel = new Text(grpConnectionType, SWT.BORDER);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fileNameLabel.setEditable(false);
		fileNameLabel.setText(INITIAL_TEXT);
		
		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();
		
		fileUpload = new FileUpload(grpConnectionType, SWT.NONE);
		fileUpload.setText(Messages.get().SQLiteLoginComposite_14);
		fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		fileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = fileUpload.getFileName();
				if("".equals(fileName) || null == fileName) return; //$NON-NLS-1$
				
				if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SQLiteLoginComposite_17)) return;
				
				fileNameLabel.setText(fileName == null ? "" : fileName); //$NON-NLS-1$
				
				pushSession.start();
				fileUpload.submit(url);
			}
		});
		
		chkBtnCreationDb = new Button(grpConnectionType, SWT.RADIO);
		chkBtnCreationDb.setSelection(true);
		chkBtnCreationDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				uiInit(true);
			}
		});
		chkBtnCreationDb.setText(Messages.get().SQLiteLoginComposite_btnCreationDb_text);
		
		textCreationDB = new Text(grpConnectionType, SWT.BORDER);
		textCreationDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		chkBtnFileLocationDb = new Button(grpConnectionType, SWT.RADIO);
		chkBtnFileLocationDb.setSelection(false);
		chkBtnFileLocationDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				uiInit(true);
			}
		});
		chkBtnFileLocationDb.setText(Messages.get().SQLiteLoginComposite_18);
		
		textFileLocationDB = new Text(grpConnectionType, SWT.BORDER);
		textFileLocationDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		othersConnectionInfo = new OthersConnectionRDBWithoutTunnelingGroup(this, SWT.NONE, getSelectDB());
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		init();
	}
	
	/**
	 * 저장 이벤트 
	 * 
	 * @return
	 */
	private String startUploadReceiver() {
		receiver = new DiskFileUploadReceiver();
		final FileUploadHandler uploadHandler = new FileUploadHandler(receiver);
		uploadHandler.addUploadListener(new FileUploadListener() {

			public void uploadProgress(FileUploadEvent event) {
			}

			public void uploadFailed(FileUploadEvent event) {
				addToLog( "upload failed: " + event.getException() ); //$NON-NLS-1$
			}

			public void uploadFinished(FileUploadEvent event) {
				for( FileDetails file : event.getFileDetails() ) {
					addToLog( "uploaded : " + file.getFileName() ); //$NON-NLS-1$
					if(logger.isDebugEnabled()) logger.debug("===> " + file.getFileName()); //$NON-NLS-1$
				}
			}			
		});
		
		return uploadHandler.getUploadUrl();
	}
	
	private void addToLog(final String message) {
		if (!fileNameLabel.isDisposed()) {
			fileNameLabel.getDisplay().asyncExec(new Runnable() {
				public void run() {
					String text = fileNameLabel.getText();
					if (INITIAL_TEXT.equals(text)) {
						text = ""; //$NON-NLS-1$
					}
					fileNameLabel.setText(message);

					pushSession.stop();
				}
			});
		}
	}
	
	/**
	 * 데이터 입력에 따라 수정합니다. 
	 * 
	 * @param isCreateDB
	 */
	private void uiInit(boolean isCreateDB) {
		textCreationDB.setEnabled(isCreateDB);
		fileUpload.setEnabled(!isCreateDB);
	}
	

	@Override
	protected void init() {
		// change group title
		grpConnectionType.setText(
				String.format("%s %s", selectDB.getDBToString() , Messages.get().DatabaseInformation)
		);
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText( PublicTadpoleDefine.DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
			textCreationDB.setText(oldUserDB.getDb());
			textCreationDB.setEnabled(false);
			fileUpload.setEnabled(false);
			chkBtnCreationDb.setEnabled(false);
			chkBtnFileUpload.setEnabled(false);
			
			othersConnectionInfo.setUserData(oldUserDB);
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {
			uiInit(true);
			preDBInfo.setTextDisplayName(getDisplayName());
			textCreationDB.setText(Messages.get().SQLiteLoginComposite_19);
		}
		
		Combo comboGroup = preDBInfo.getComboGroup();
		if(comboGroup.getItems().length == 0) {
			comboGroup.add(strOtherGroupName);
			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) { //$NON-NLS-1$
				comboGroup.setText(strOtherGroupName);
			} else {
				// 콤보 선택 
				for(int i=0; i<comboGroup.getItemCount(); i++) {
					if(selGroupName.equals(comboGroup.getItem(i))) comboGroup.select(i);
				}
			}
		}
		
		preDBInfo.getTextDisplayName().setFocus();
	}
	
	/**
	 * 화면에 값이 올바른지 검사합니다.
	 * 
	 * @return
	 */
	@Override
	public boolean isValidateInput(boolean isTest) {
		
		// 데이터베이스 용 디렉토리가 없으면 생성합니다.
		File fileRootResource = new File(ROOT_RESOURCE_DIR);
		if(!fileRootResource.isDirectory()) {
			fileRootResource.mkdirs();
		}
		
		if(!ValidChecker.checkTextCtl(preDBInfo.getComboGroup(), Messages.get().GroupName)) return false;
		if(!ValidChecker.checkTextCtl(preDBInfo.getTextDisplayName(), Messages.get().DisplayName)) return false;
		
		if(oldUserDB != null) return true;
		
		if(chkBtnFileUpload.getSelection()) {
			File[] arryFiles = receiver.getTargetFiles();
			if(arryFiles.length == 0) {
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SQLiteLoginComposite_23);
				return false;
			}
			File userDBFile = arryFiles[arryFiles.length-1];
			
			File targetFile = new File(ROOT_RESOURCE_DIR + userDBFile.getName());
			if(targetFile.exists()) {
				boolean isUpload = MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SQLiteLoginComposite_24);
				if(!isUpload) {
					chkBtnFileUpload.setFocus();
					return false;
				}
			}
		} else if(chkBtnFileLocationDb.getSelection()) {
			File targetFile = new File(textFileLocationDB.getText());
			if(!targetFile.exists()) {
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SQLiteLoginComposite_25);
				textFileLocationDB.setFocus();
				return false;
			}
		// 신규 디비 생성.
		} else {
			String strFile = StringUtils.trimToEmpty(textCreationDB.getText());
			
			if("".equals(strFile) ) { //$NON-NLS-1$
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SQLiteLoginComposite_7);
				textCreationDB.setFocus();
				return false;
			} 
			if(new File(ROOT_RESOURCE_DIR + textCreationDB.getText()).exists()) {
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SQLiteLoginComposite_24);
				textCreationDB.setFocus();
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		String strDBFile = "", strDBUrl = ""; //$NON-NLS-1$ //$NON-NLS-2$
		if(oldUserDB != null) {
			strDBFile = oldUserDB.getDb();
			strDBUrl = oldUserDB.getUrl();
		} else {
			if(chkBtnFileUpload.getSelection()) {
				File[] arryFiles = receiver.getTargetFiles();
				File userDBFile = arryFiles[arryFiles.length-1];
				
				strDBFile = userDBFile.getName();
				if(isTest) {
					strDBUrl = userDBFile.getAbsolutePath();
				} else {
					strDBUrl = ROOT_RESOURCE_DIR + userDBFile.getName() + Utils.getUniqueID();
					
					try {
						FileUtils.moveFile(userDBFile, new File(strDBUrl));
					} catch (IOException e) {
						logger.error("File moveing", e); //$NON-NLS-1$
						MessageDialog.openError(null, Messages.get().Error, Messages.get().SQLiteLoginComposite_29);
						
						return false;
					}
				}
			} else if(chkBtnFileLocationDb.getSelection()) {
				strDBFile = textFileLocationDB.getText();
				strDBUrl = textFileLocationDB.getText();
			// 신규 디비 생성.
			} else {
				strDBFile = textCreationDB.getText();
				strDBUrl = ROOT_RESOURCE_DIR + textCreationDB.getText();
			}
			
			strDBUrl = String.format(getSelectDB().getDB_URL_INFO(), strDBUrl);
		}
		
		userDB = new UserDBDAO();
		userDB.setDbms_type(getSelectDB().getDBToString());
		userDB.setUrl(strDBUrl);
		userDB.setDb(strDBFile);
//		userDB.setGroup_seq(SessionManager.getGroupSeq());
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));

		String dbOpType = PublicTadpoleDefine.DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).name();
		userDB.setOperation_type(dbOpType);
		if(dbOpType.equals(PublicTadpoleDefine.DBOperationType.PRODUCTION.name()) || dbOpType.equals(PublicTadpoleDefine.DBOperationType.BACKUP.name()))
		{
			userDB.setIs_lock(PublicTadpoleDefine.YES_NO.YES.name());
		}

		userDB.setUsers(""); //$NON-NLS-1$
		userDB.setPasswd(""); //$NON-NLS-1$
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();
		
		return true;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		try {
			if(pushSession != null) pushSession.stop();
			pushSession = null;
		} catch(Exception e) {
			// ignore exception
		}
	}

}
