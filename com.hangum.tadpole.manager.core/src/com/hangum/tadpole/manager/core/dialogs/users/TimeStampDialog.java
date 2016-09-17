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
package com.hangum.tadpole.manager.core.dialogs.users;

import java.sql.Timestamp;
import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

/**
 * TimeStamp dialog
 * 
 * @author nilriri
 *
 */
public class TimeStampDialog extends Dialog { 
    private Composite composite; 
    private Timestamp originalValue; 
    private Timestamp value;
    private String title; 
    
    private DateTime serviceDate;
    private DateTime serviceTime;
 
    public TimeStampDialog(Shell parentShell, final Timestamp originalValue, String title) { 
        super(parentShell);
        setShellStyle(SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL);
 
        this.originalValue = originalValue; 
        this.title = title; 
    } 
 
    @Override 
    protected void configureShell(Shell shell) { 
        super.configureShell(shell); 
        shell.setText(title); 
    } 
 
    @Override 
    protected Control createDialogArea(Composite parent) { 
        GridLayoutFactory.swtDefaults().applyTo(parent); 
 
        composite = new Composite(parent, SWT.NONE); 
        GridLayoutFactory.swtDefaults().applyTo(composite); 
        composite.setLayout(new GridLayout(1, false));
 
        GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);
        
        serviceDate = new DateTime(composite, SWT.DROP_DOWN | SWT.CALENDAR | SWT.LONG);
        serviceDate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        serviceDate.setDate(originalValue.getYear(), originalValue.getMonth(), originalValue.getDay());
        
        serviceTime = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.TIME | SWT.LONG);
        serviceTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        serviceTime.setTime(originalValue.getHours(),  originalValue.getMinutes(),  originalValue.getSeconds());

        return parent; 
    } 
 
    @Override 
    protected void okPressed() {
    	Calendar cal = Calendar.getInstance();
    	cal.set(serviceDate.getYear(), serviceDate.getMonth(), serviceDate.getDay(), serviceTime.getHours(), serviceTime.getMinutes(), serviceTime.getSeconds());
        value = new Timestamp( cal.getTimeInMillis()); 
        super.okPressed(); 
    } 
 
    public Timestamp getValue() { 
        return value; 
    } 

    @Override
	protected Point getInitialSize() {
		return new Point(277, 318);
	}
}
