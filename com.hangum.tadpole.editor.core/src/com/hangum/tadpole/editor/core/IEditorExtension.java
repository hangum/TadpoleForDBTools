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
package com.hangum.tadpole.editor.core;

/**
 * editor의 표준 인터페이스를 정의합니다.
 * 
 * @author hangum
 *
 */
public interface IEditorExtension {
	/** 개발디비 에디터 정의 */
	public static final String DEV_DB_URL = "orion/tadpole/editor/RDBEmbeddededitor.html"; //$NON-NLS-1$
	
	/** 운영디비 에디터 정의 */
	public static final String REAL_DB_URL = "orion/tadpole/editor/REAL_RDBEmbeddededitor.html"; //$NON-NLS-1$
}
