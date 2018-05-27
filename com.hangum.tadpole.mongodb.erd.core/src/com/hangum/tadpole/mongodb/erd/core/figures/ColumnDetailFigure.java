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
package com.hangum.tadpole.mongodb.erd.core.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * column의 디테일 항목(컬러명, 타입, 키)으로 사용하는 피켜
 * 
 * @author hangum
 *
 */
public class ColumnDetailFigure extends Figure {
	
	public ColumnDetailFigure() {
		if("Asia/Seoul".equals(SessionManager.getTimezone())) {
			try{
				super.setFont(new Font(Display.getCurrent(), new FontData(CommonMessages.get().Gullim, 12, SWT.NONE)));
			} catch(Exception e) {
				//
			}
		}
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment( ToolbarLayout.ALIGN_TOPLEFT ); //ALIGN_CENTER 
		layout.setStretchMinorAxis( false );
		layout.setSpacing( 2 );
		
		setLayoutManager( layout );
	}
	
}
