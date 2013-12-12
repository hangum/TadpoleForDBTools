/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.notes.core.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.notes.core.Messages;
import com.hangum.tadpole.notes.core.define.NotesDefine;
import com.hangum.tadpole.notes.core.define.NotesDefine.NOTE_TYPES;
import com.hangum.tadpole.sql.dao.system.NotesDAO;
import com.hangum.tadpole.sql.system.TadpoleSystem_Notes;

/**
 * 쪽지 내용을 보고 답변을 보내는 dialog
 * 
 * @author hangum
 *
 */
public class ViewDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ViewDialog.class);

	private NotesDAO noteDAO;
	private NotesDefine.NOTE_TYPES noteType;
	
	private Label lblUser;
	private Text textUser;
	private Text textTitle;
	private Text textContent;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ViewDialog(Shell parentShell, NotesDAO noteDAO, NotesDefine.NOTE_TYPES noteType) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.noteDAO = noteDAO;
		this.noteType = noteType;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ViewDialog_5);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		lblUser = new Label(compositeHead, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText(Messages.ViewDialog_0);
		
		textUser = new Text(compositeHead, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(2, false));
		
		Label lblTitle = new Label(compositeBody, SWT.NONE);
		lblTitle.setText(Messages.ViewDialog_1);
		
		textTitle = new Text(compositeBody, SWT.BORDER);
		textTitle.setEditable(false);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblContent = new Label(compositeBody, SWT.NONE);
		lblContent.setText(Messages.ViewDialog_2);
		
		textContent = new Text(compositeBody, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textContent.setEditable(false);
		textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initData();
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		NewNoteDialog dialog = new NewNoteDialog(null, noteDAO, textUser.getText());
		dialog.open();
		
		super.okPressed();
	}
	
	/**
	 * 데이터를 초기화 한다.
	 */
	private void initData() {
		try {
			// note를 읽음 상태 처리 합니다.
			TadpoleSystem_Notes.readNote(noteDAO.getSeq());

			// note의 디테일 정보를 가져오고.
			noteDAO.setContents(TadpoleSystem_Notes.getNoteData(noteDAO));
			
			// 나머지 노트 내용을 체우고.
			if(noteType == NOTE_TYPES.SEND) {
				lblUser.setText(Messages.ViewDialog_3);
				textUser.setText(noteDAO.getReceiveUserId());
			} else {
				lblUser.setText(Messages.ViewDialog_4);
				textUser.setText(noteDAO.getSendUserId());
			}
			
			textTitle.setText(noteDAO.getTitle());
			textContent.setText(noteDAO.getContents());
		} catch(Exception e) {
			logger.error("get note data", e); //$NON-NLS-1$
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.ViewDialog_6, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.ViewDialog_7, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 500);
	}

}
