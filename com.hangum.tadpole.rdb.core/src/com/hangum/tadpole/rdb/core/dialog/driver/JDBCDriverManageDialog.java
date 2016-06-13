/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.driver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rap.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.JDBCDriverLoader;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * JDBC Driver Manager
 * 
 * @author hangum
 *
 */
public class JDBCDriverManageDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(JDBCDriverManageDialog.class);
	
	private Button btnDelete;
	private FileUpload fileUpload;
	private DiskFileUploadReceiver receiver;
	private ServerPushSession pushSession;

	private ListViewer lvDB;
	private Text lblRealFullPath;
	private ListViewer lvDriverFile;
	
	private String jdbc_dir = "";
	
	/** Is JAR upload? */
	private boolean isUploaded = false;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public JDBCDriverManageDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().JDBCDriverSetting);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		
		SashForm sashForm = new SashForm(container, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeLeft = new Composite(sashForm, SWT.NONE);
		compositeLeft.setLayout(new GridLayout(1, false));
		
		Label lblDriverList = new Label(compositeLeft, SWT.NONE);
		lblDriverList.setText(Messages.get().JDBCDriverSetting_DriverList);
		
		lvDB = new ListViewer(compositeLeft, SWT.BORDER | SWT.V_SCROLL);
		lvDB.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection ss = (StructuredSelection)lvDB.getSelection();
				if(ss.isEmpty()) return;
				
				DBDefine dbDefine = (DBDefine)ss.getFirstElement();
				jdbc_dir = ApplicationArgumentUtils.JDBC_RESOURCE_DIR + dbDefine.getExt() + PublicTadpoleDefine.DIR_SEPARATOR;
				lblRealFullPath.setText(jdbc_dir);
				initDBFileList();
			}
		});
		List list = lvDB.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		lvDB.setContentProvider(new ArrayContentProvider());
		lvDB.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				DBDefine dbDefine = (DBDefine)element;
				return dbDefine.getDBToString();
			}
		});
		lvDB.setInput(DBDefine.getDriver());
		
		Composite compositeBody = new Composite(sashForm, SWT.NONE);
		compositeBody.setLayout(new GridLayout(3, false));
		
		Label lblDumy = new Label(compositeBody, SWT.NONE);
		lblDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblPath = new Label(compositeBody, SWT.NONE);
		lblPath.setText(Messages.get().JDBCDriverSetting_Path);
		
		lblRealFullPath = new Text(compositeBody, SWT.NONE | SWT.READ_ONLY);
		lblRealFullPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label txtFileList = new Label(compositeBody, SWT.NONE);
		txtFileList.setText(Messages.get().JDBCDriverSetting_FileList);
		new Label(compositeBody, SWT.NONE);
		new Label(compositeBody, SWT.NONE);
		
		lvDriverFile = new ListViewer(compositeBody, SWT.BORDER | SWT.V_SCROLL);
		lvDriverFile.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				btnDelete.setEnabled(true);
			}
		});
		lvDriverFile.setContentProvider(new ArrayContentProvider());
		lvDriverFile.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String str = element.toString();
				return str;
			}
		});
		
		List listDriver = lvDriverFile.getList();
		listDriver.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Composite compositeBodyBtn = new Composite(compositeBody, SWT.NONE);
		compositeBodyBtn.setLayout(new GridLayout(1, false));
		compositeBodyBtn.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		
		btnDelete = new Button(compositeBodyBtn, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)lvDriverFile.getSelection();
				if(ss.isEmpty()) return;
				
				String strFile = (String)ss.getFirstElement();
				if(!MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().DeleteDriver)) return;
				if(logger.isDebugEnabled()) logger.debug("File delete : " + jdbc_dir + strFile);
				
				try {
					FileUtils.forceDelete(new File(jdbc_dir + strFile));
					
					initDBFileList();
				} catch (IOException e1) {
					logger.error("File delete", e1);
					MessageDialog.openError(getShell(), Messages.get().Error, "File deleteing: " + e1.getMessage());
				}
				
			}
		});
		btnDelete.setText(Messages.get().Delete);
		
		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();
		
		fileUpload = new FileUpload(compositeBodyBtn, SWT.NONE);
		fileUpload.setText(Messages.get().JDBCDriverSetting_JARUpload);
		fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		fileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = fileUpload.getFileName();
				if("".equals(fileName) || null == fileName) return; //$NON-NLS-1$
				if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SQLiteLoginComposite_17)) return;
				
				if(logger.isDebugEnabled()) logger.debug("=[file name]==> " + fileName);
				
				pushSession.start();
				fileUpload.submit(url);
			}
		});
		
		Button btnRefresh = new Button(compositeBodyBtn, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initDBFileList();
			}
		});
		btnRefresh.setText(Messages.get().Refresh);
		
		sashForm.setWeights(new int[] {3, 7});
		initManager();

		return container;
	}
	
	/**
	 * init db file list
	 */
	private void initDBFileList() {
		if(logger.isDebugEnabled()) logger.debug("=[jdbc url]==> " + jdbc_dir);
		
		File f = new File(jdbc_dir);
		String[] listFiles = f.list();
		lvDriverFile.setInput(listFiles);
		lvDriverFile.refresh();
		
		btnDelete.setEnabled(false);
		fileUpload.setEnabled(true);
	}
	
	/**
	 * data initialize
	 */
	private void initManager() {
		btnDelete.setEnabled(false);
		fileUpload.setEnabled(false);
		
		AnalyticCaller.track(this.getClass().getName());
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
				logger.error("file upload ", event.getException());
//				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Upload failed", "File upload fail.  " + event.getException().getMessage());
			}
			public void uploadFinished(FileUploadEvent event) {
				for( FileDetails file : event.getFileDetails() ) {
					if(logger.isDebugEnabled()) logger.debug("===> " + file.getFileName()); //$NON-NLS-1$
				}
				
				String strFile = jdbc_dir;
				File[] arryFiles = receiver.getTargetFiles();
				for (File file : arryFiles) {
					try {
						FileUtils.moveFileToDirectory(file, new File(jdbc_dir), true);
						
						strFile += file.getName();
					} catch (Exception e) {
						logger.error("driver move", e);
//						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Upload failed", "File upload fail.  " + e.getMessage());
					}
				}

				final String strTmpPaht = strFile;
				lvDriverFile.getList().getDisplay().asyncExec(new Runnable() {
					public void run() {
						initDBFileList();
						
						try {
							JDBCDriverLoader.addJarFile(strTmpPaht);
							setUploaded(true);
						} catch (Exception e) {
							logger.error("jar loading", e);
						}
					}
				});
			}			
		});
		
		return uploadHandler.getUploadUrl();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 560);
	}
	
	@Override
	public boolean close() {
		try {
			if(pushSession != null) pushSession.stop();
			pushSession = null;
		} catch(Exception e) {
			// ignore exception
		}
		
		return super.close();
	}
	
	/**
	 * is upload
	 * @return
	 */
	public boolean isUploaded() {
		return isUploaded;
	}
	
	/**
	 * Set jar upload
	 * 
	 * @param isUploaded
	 */
	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}

}
