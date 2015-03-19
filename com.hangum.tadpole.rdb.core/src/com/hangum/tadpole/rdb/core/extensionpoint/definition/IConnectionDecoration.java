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
package com.hangum.tadpole.rdb.core.extensionpoint.definition;

import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 커넥션 뷰 파트의 데코레이션입니다.
 * 
 * @author hangum
 *
 */
public interface IConnectionDecoration {
	
	/**
	 * 화면을 초기화 합니다.
	 * 1. 초기 화면이 보여야 하는지 설정합니다. 
	 */
	public Image getImage(UserDBDAO userDB);

}
