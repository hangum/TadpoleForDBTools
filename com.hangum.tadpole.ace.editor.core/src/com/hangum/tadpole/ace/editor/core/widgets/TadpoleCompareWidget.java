/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.ace.editor.core.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * SQL compare widget
 * 
 * @author hangum
 *
 */
public class TadpoleCompareWidget extends Composite {
	public static final String PREFIX = "'/ace-builds/";
	public static final String strDiffHtml = 
	"<!DOCTYPE html>" +
	"<html lang='en'>" +
	"<head>" +
	"    <title>Tadpole DB Hub diff</title>" +
	"    <script src=" + PREFIX + "ace-diff/diff_match_patch.js'></script>" +
	"    <link rel='stylesheet' href=" + PREFIX + "ace-diff/styles.css'>" +
	"</head>" +
	"<body>" +
	"<div id='flex-container'>" +
	"    <div><div id='acediff-left-editor'>%s" +
	"    </div></div>" +
	"    <div id='acediff-gutter'></div>" +
	"    <div><div id='acediff-right-editor'>%s" +
	"    </div></div>" +
	"</div>" +
	"<script src=" + PREFIX + "ace-diff/ace.js' type='text/javascript' charset='utf-8'></script>" +
	"<script src=" + PREFIX + "ace-diff/mode-sql.js'></script>" +
	
	"<script src=" + PREFIX + "ace-diff/ace-diff.min.js'></script>" +
	"<script>" +
	"var aceDiffer = new AceDiff({" +
	"    mode: 'ace/mode/sql'" +
	"});" +
	"</script>" +
	"</body>" +
	"</html>";
			
	private Browser browserCompare;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TadpoleCompareWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		browserCompare = new Browser(this, SWT.NONE);
		browserCompare.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browserCompare.setText(String.format(strDiffHtml, "", ""));
	}
	
	/**
	 * change diff
	 * 
	 * @param source
	 * @param target
	 */
	public void changeDiff(String source, String target) {
		browserCompare.setText(String.format(strDiffHtml, source, target));
	}
	
	@Override
	protected void checkSubclass() {
	}

}
