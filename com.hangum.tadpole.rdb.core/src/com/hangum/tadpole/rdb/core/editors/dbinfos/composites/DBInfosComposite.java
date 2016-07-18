/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.eclipse.swt.widgets.Composite;

/**
 * DBInfos composite
 * 
 * @author hangum
 *
 */
public abstract class DBInfosComposite extends Composite {

	public DBInfosComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * initialize UI 
	 * 
	 * @param isRefresh
	 */
	public abstract void initUI(boolean isRefresh);
	
	@Override
	protected void checkSubclass() {
	}
}
