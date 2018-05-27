///*******************************************************************************
// * Copyright (c) 2012 - 2015 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.erd.core.dialogs;
//
//import org.eclipse.swt.widgets.Dialog;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Canvas;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Label;
//
///**
// *
// *
// *
// * @author hangum
// * @version 1.6.1
// * @since 2015. 4. 15.
// *
// */
//public class ImageExportDialog extends Dialog {
//
//	protected Object result;
//	protected Shell shell;
//	
//	private Canvas canvas ;
//
//	/**
//	 * Create the dialog.
//	 * @param parent
//	 * @param style
//	 */
//	public ImageExportDialog(Shell parent, int style) {
//		super(parent, style);
//		setText("SWT Dialog");
//	}
//
//	/**
//	 * Open the dialog.
//	 * @return the result
//	 */
//	public Object open() {
//		createContents();
//		shell.open();
//		shell.layout();
//		Display display = getParent().getDisplay();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				display.sleep();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 */
//	private void createContents() {
//		shell = new Shell(getParent(), getStyle());
//		shell.setSize(450, 300);
//		shell.setText(getText());
//		shell.setLayout(new GridLayout(1, false));
//		
//		canvas = new Canvas(shell, SWT.NONE);
//		
//		Button btnExport = new Button(shell, SWT.NONE);
//		btnExport.setText("Export");
//
//	}
//}
