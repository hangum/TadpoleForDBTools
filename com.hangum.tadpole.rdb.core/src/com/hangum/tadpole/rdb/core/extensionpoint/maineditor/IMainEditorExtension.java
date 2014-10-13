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
package com.hangum.tadpole.rdb.core.extensionpoint.maineditor;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * MainEditor extension
 * 
 * @author hangum
 *
 */
public interface IMainEditorExtension {
	/**
	 * 메인 에디터에서 UI가 위치할 위치 지정.
	 * 
	 *  SWT.LEFT
	 *	SWT.RIGHT
	 *	SWT.TOP
	 *	SWT.BOTTOM
	 */
	public int extensionLocation = SWT.RIGHT;
		
	/**
	 * user create part control
	 * 
	 * @param parent
	 */
	public void createPartControl(Composite parent);
	
	/**
	 * resultSetDoubleClick
	 * 
	 * @param selectIndex  select index
	 * @param mapColumns column <index, value>
	 */
	public void resultSetDoubleClick(int selectIndex, Map<Integer, Object> mapColumns);

}
