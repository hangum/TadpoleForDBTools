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
package com.hangum.tadpole.mongodb.core.ext.editors.javascript.dialog;

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

import com.hangum.tadpole.mongodb.core.Messages;

/**
 * javascript eval input dialog
 * 
 * @author hangum
 *
 */
public class EvalInputDialog extends Dialog {
	int intArgumentCount;
	private Text[] textInputValue;
	private Object[] inputObject;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EvalInputDialog(Shell parentShell, int intArgumentCount) {
		super(parentShell);
		
		this.intArgumentCount = intArgumentCount;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Java Script Input Dialog");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		textInputValue = new Text[intArgumentCount];
		for(int i=0; i<intArgumentCount; i++) {
			Label lblNewLabel = new Label(container, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblNewLabel.setText("input value : " + i);
			
			textInputValue[i] = new Text(container, SWT.BORDER);
			textInputValue[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		
//		// set default focus
		textInputValue[0].setFocus();

		return container;
	}
	
	@Override
	protected void okPressed() {
	
		inputObject = new Object[intArgumentCount];
		for (int i=0; i<intArgumentCount; i++) {
			inputObject[i] = convertInputVal(textInputValue[i].getText());			
		}
		
		super.okPressed();
	}
	
	private Object convertInputVal(String val) {
		if("".equals(val)) return "";
		
		try {
			return Double.parseDouble(val);
		} catch(Exception e) {
			return val;
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,  Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 422);
	}
	
	public Object[] getInputObject() {
		return inputObject;
	}

}
