/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.dialogs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.admin.core.Activator;
import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.define.GetAdminPreference;

/**
 * 모든 사용자에게 메시지를 보냅니다.
 * 
 * @author hangum
 *
 */
public class SendMessageDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SendMessageDialog.class);
	
	private Combo comboType;
	private Text textMessage;
	private Text textTitle;
	private Text textSengingHistory;
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SendMessageDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().SendMessageDialog_0);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblType = new Label(compositeHead, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText(Messages.get().SendMessageDialog_1);
		
		comboType = new Combo(compositeHead, SWT.NONE);
		comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboType.add(Messages.get().SendMessageDialog_2);
		comboType.select(0);
		
		Composite compositeBody = new Composite(container, SWT.BORDER);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblTitle = new Label(compositeBody, SWT.NONE);
		lblTitle.setText(Messages.get().SendMessageDialog_3);
		
		textTitle = new Text(compositeBody, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMessage = new Label(compositeBody, SWT.NONE);
		lblMessage.setText(Messages.get().SendMessageDialog_4);
		
		textMessage = new Text(compositeBody, SWT.BORDER | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textMessage.setText(Messages.get().SendMessageDialog_5);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 100;
		composite.setLayoutData(gd_composite);
		
		Label lblSeingHistory = new Label(composite, SWT.NONE);
		lblSeingHistory.setText(Messages.get().SendMessageDialog_6);
		
		textSengingHistory = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		textSengingHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	@Override
	protected void okPressed() {

		if(StringUtils.isEmpty(textTitle.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SendMessageDialog_8);
			textTitle.setFocus();
			return;
		}
		if(StringUtils.isEmpty(textMessage.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SendMessageDialog_10);
			textMessage.setFocus();
			return;
		}
		
		// 모든 사용자에게 이메일을 보냅니다.
		final Shell shell = getShell();
		final String strTitle = textTitle.getText();
		final String strMessage = textMessage.getText();
		
		Job job = new Job("AdminSendingmail") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				
				try {
					List<UserDAO> listUser = TadpoleSystem_UserQuery.getLiveAllUser();
					SendEmails email = new SendEmails(GetAdminPreference.getSessionSMTPINFO());
					
					monitor.beginTask("Start send mail", listUser.size());
					
					for (int i=0; i<listUser.size(); i++) {
						UserDAO userDAO = listUser.get(i);
						
						monitor.setTaskName(String.format("%d/%d, user %s ", i, listUser.size(), userDAO.getEmail()));
						logger.info("admin user sender " + userDAO.getEmail()); //$NON-NLS-1$
						try {
							EmailDTO emailDto = new EmailDTO();
							emailDto.setSubject(strTitle);
							emailDto.setContent(strMessage);
							emailDto.setTo(userDAO.getEmail());
							
							email.sendMail(emailDto);
							resultMessage(userDAO.getEmail() + " senging."); //$NON-NLS-1$
						} catch(Exception e) {
							logger.error("user sender", e); //$NON-NLS-1$
							resultMessage(userDAO.getEmail() + " is fail. " + e.getMessage()); //$NON-NLS-1$
						} 
						
						if(monitor.isCanceled()) {
							logger.info(Messages.get().SendMessageDialog_18);
							break;
						}
					}
				} catch(Exception e) {
					logger.error("sending mail", e);
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage(), e);
				} finally {
					resultMessage("done."); //$NON-NLS-1$
					monitor.done();
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
			
			private void resultMessage(final String msg) {
				shell.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						String strFullMsg = "".equals(textSengingHistory.getText())?msg:textSengingHistory.getText() + PublicTadpoleDefine.LINE_SEPARATOR + msg; //$NON-NLS-1$
						textSengingHistory.setText(strFullMsg);
						
					}
				});
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							MessageDialog.openInformation(shell, Messages.get().Confirm, Messages.get().SendMessageDialog_23);
						} else {
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(shell, Messages.get().Error, Messages.get().SendMessageDialog_11, errStatus); //$NON-NLS-1$
						}
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(Messages.get().SendMessageDialog_11);
		job.setUser(true);
		job.schedule();
		
//		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Confirm, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancle, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(441, 480);
	}
}
