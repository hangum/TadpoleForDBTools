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

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.libs.core.define.HTMLDefine;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

/**
 * SQL compare widget
 * 
 * @author hangum
 *
 */
public class TadpoleCompare2Widget extends Composite {
	private static final Logger logger = Logger.getLogger(TadpoleCompare2Widget.class);
	private static diff_match_patch.Operation DELETE = diff_match_patch.Operation.DELETE;
	private static diff_match_patch.Operation EQUAL = diff_match_patch.Operation.EQUAL;
	private static diff_match_patch.Operation INSERT = diff_match_patch.Operation.INSERT;
	
	public static String strDiffHtml = HTMLDefine.HTML_STYLE + "<table class='tg'><tr><td class='tg-yw4l'>%s</td></tr></table>";

			
	private Browser browserCompare;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TadpoleCompare2Widget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		browserCompare = new Browser(this, SWT.NONE);
		browserCompare.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browserCompare.setText(String.format(strDiffHtml, ""));
	}
	
	/**
	 * change diff
	 * 
	 * @param source
	 * @param target
	 */
	public void changeDiff(String source, String target) {
		try {
			diff_match_patch diffMatch = new diff_match_patch();
			LinkedList<Diff> diffResult = diffMatch.diff_main(target, source);
			browserCompare.setText(String.format(strDiffHtml, diffMatch.diff_prettyHtml(diffResult)));
		} catch(Exception e) {
			logger.error("error diff");
		}
	}
	
	/**
	 * diffs
	 * @param diffs
	 * @return
	 */
	private static LinkedList<Diff> diffList(Diff... diffs) {
		LinkedList<Diff> myDiffList = new LinkedList<Diff>();
		for (Diff myDiff : diffs) {
			myDiffList.add(myDiff);
		}
		return myDiffList;
	}
	
	@Override
	protected void checkSubclass() {
	}

}
