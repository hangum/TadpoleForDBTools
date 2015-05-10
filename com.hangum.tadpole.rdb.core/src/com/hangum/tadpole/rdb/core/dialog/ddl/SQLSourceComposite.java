///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.dialog.ddl;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Label;
//
//import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
//import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
//import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
//
///**
// * Table summary composite
// * 
// * @author hangum
// *
// */
//public class SQLSourceComposite extends Composite {
//	
//	private Label labelTable;
//	private TadpoleEditorWidget ddlSource;
//
//	/**
//	 * Create the composite.
//	 * @param parent
//	 * @param style
//	 */
//	public SQLSourceComposite(Composite parent, int style, String tableName, String initDDL) {
//		super(parent, style);
//		GridLayout gridLayout = new GridLayout(1, false);
//		gridLayout.verticalSpacing = 0;
//		gridLayout.horizontalSpacing = 0;
//		gridLayout.marginHeight = 0;
//		gridLayout.marginWidth = 0;
//		setLayout(gridLayout);
//		
//		Composite compositeBasic = new Composite(this, SWT.NONE);
//		compositeBasic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		compositeBasic.setLayout(new GridLayout(1, false));
//		
//		labelTable = new Label(compositeBasic, SWT.NONE);
//		labelTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		labelTable.setText(tableName);
//		
//		ddlSource = new TadpoleEditorWidget(compositeBasic, SWT.BORDER, EditorDefine.EXT_DEFAULT, initDDL, "");
//		ddlSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
//		
//		// google analytic
//		AnalyticCaller.track(this.getClass().getName());
//	}
//	
//	/**
//	 * set ddl
//	 * 
//	 * @param ddlSource
//	 * @deprecated
//	 */
//	public void setDdlSource(String strDdl) {
//		ddlSource.setText(strDdl);
//	}
//	
//	/**
//	 * get ddl
//	 * 
//	 * @return
//	 */
//	public String getDdlSource() {
//		return ddlSource.getText();
//	}
//
//	@Override
//	protected void checkSubclass() {
//	}
//
//}
