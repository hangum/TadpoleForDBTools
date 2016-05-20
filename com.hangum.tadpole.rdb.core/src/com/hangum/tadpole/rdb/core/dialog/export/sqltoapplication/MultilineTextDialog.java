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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text; 

/**
 * Multilin text dialog
 * 
 * @author nilriri
 *
 */
public class MultilineTextDialog extends Dialog { 
    private Composite composite; 
    private String originalValue; 
    private String value; 
    private String title; 
    private Text textControl; 
 
    public MultilineTextDialog(Shell parentShell, final String originalValue, String title) { 
        super(parentShell); 
        setShellStyle(SWT.RESIZE | SWT.TOOL | SWT.TITLE); 
 
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
 
        Label label = new Label(composite, SWT.WRAP); 
        label.setText("Specify " + title + " value"); 
 
        GridDataFactory.fillDefaults().grab(true, true).applyTo(composite); 
 
        textControl = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL ); 
 
        textControl.setText(originalValue); 
        GridDataFactory.fillDefaults().grab(true, true).hint(300, 200).applyTo(textControl); 
 
        return parent; 
    } 
 
    @Override 
    protected void okPressed() { 
        value = textControl.getText(); 
        super.okPressed(); 
    } 
 
    public String getValue() { 
        return value; 
    } 
 
}
