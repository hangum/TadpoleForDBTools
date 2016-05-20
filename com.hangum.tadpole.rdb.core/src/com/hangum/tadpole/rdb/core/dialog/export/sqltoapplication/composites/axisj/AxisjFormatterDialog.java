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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.axisj;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent; 
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;

/**
 * Multilin text dialog
 * 
 * @author nilriri
 *
 */
public class AxisjFormatterDialog extends Dialog { 
    private Composite composite; 
    private String originalValue; 
    private String value; 
    private String title; 
    private Text textControl; 
 
    public AxisjFormatterDialog(Shell parentShell, final String originalValue, String title) { 
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
        composite.setLayout(new GridLayout(1, false));
 
        Label label = new Label(composite, SWT.WRAP); 
        label.setText("Specify " + title); 
 
        GridDataFactory.fillDefaults().grab(true, true).applyTo(composite); 
        
        Composite composite_1 = new Composite(composite, SWT.NONE);
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        composite_1.setLayout(new GridLayout(5, false));
        
        Label lblNewLabel = new Label(composite_1, SWT.NONE);
        lblNewLabel.setText("Type");
        
        Button btnNormal = new Button(composite_1, SWT.RADIO);
        btnNormal.setText("Normal");
        btnNormal.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(false);
        		textControl.setText("\"\"");
        	}
        });
        
        Button btnMoney = new Button(composite_1, SWT.RADIO);
        btnMoney.setText("Money");
        btnMoney.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(false);
        		textControl.setText("\"money\"");
        	}
        });
        
        Button btnDec = new Button(composite_1, SWT.RADIO);
        btnDec.setText("Dec");
        btnDec.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(false);
        		textControl.setText("\"dec\"");
        	}
        });
        
        Button btnHtml = new Button(composite_1, SWT.RADIO);
        btnHtml.setText("HTML");
        btnHtml.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(false);
        		textControl.setText("\"html\"");
        	}
        });
        new Label(composite_1, SWT.NONE);
        
        Button btnCheckbox = new Button(composite_1, SWT.RADIO);
        btnCheckbox.setText("CheckBox");
        btnCheckbox.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(false);
        		textControl.setText("\"checkbox\"");
        	}
        });
        
        Button btnRadio = new Button(composite_1, SWT.RADIO);
        btnRadio.setText("Radio");
        btnRadio.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(false);
        		textControl.setText("\"radio\"");
        	}
        });
        
        Button btnFunction = new Button(composite_1, SWT.RADIO);
        btnFunction.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		textControl.setEnabled(true);
        		if (StringUtils.isEmpty(originalValue) | StringUtils.equals(originalValue, "\"\"")){
        			textControl.setText( "function(){return this.value.number();}" );
        		}else{
        			textControl.setText(originalValue);
        		}
        		textControl.setFocus();
        		textControl.setSelection(11);
        	}
        });
        btnFunction.setText("Function");
        new Label(composite_1, SWT.NONE);
        
        Label label_1 = new Label(composite, SWT.NONE);
        label_1.setText("<< Function >>");
 
        textControl = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI ); 
        textControl.setEnabled(false);
 
        GridDataFactory.fillDefaults().grab(true, true).hint(350, 300).applyTo(textControl); 

        if (StringUtils.startsWithIgnoreCase(originalValue, "\"\"")) {
        	btnNormal.setSelection(true);
        }else if (StringUtils.startsWithIgnoreCase(originalValue, "\"money")) {
        	btnMoney.setSelection(true);
        }else if (StringUtils.startsWithIgnoreCase(originalValue, "\"dec")) {
        	btnDec.setSelection(true);
        }else if (StringUtils.startsWithIgnoreCase(originalValue, "\"html")) {
        	btnHtml.setSelection(true);
        }else if (StringUtils.startsWithIgnoreCase(originalValue, "\"checkbox")) {
        	btnHtml.setSelection(true);
        }else if (StringUtils.startsWithIgnoreCase(originalValue, "\"radio")) {
        	btnHtml.setSelection(true);
        }else if (StringUtils.startsWithIgnoreCase(originalValue, "function")) {
        	btnFunction.setSelection(true);
        	textControl.setEnabled(true);
        }
        textControl.setText(originalValue);
        
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

    @Override
	protected Point getInitialSize() {
		return new Point(400, 400);
	}
}
