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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * table의 컬럼의 0번째 값을 소스로 설정합니다. 
 * 
 * @author hangum
 */
public class TableDragListener implements DragSourceListener {
	private UserDBDAO userDB;
	private TableViewer viewer;
	
	public TableDragListener(UserDBDAO userDB, TableViewer viewer) {
		this.userDB = userDB;
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection iss = (IStructuredSelection)viewer.getSelection();
		if(!iss.isEmpty()) {
			
			StringBuffer sbData = new StringBuffer();
			sbData.append(userDB.getSeq() + PublicTadpoleDefine.DELIMITER);
			
			for(Object objTable : iss.toList()) {
				TableDAO td = (TableDAO)objTable;//iss.getFirstElement();
				String strSchemaName = StringUtils.defaultIfEmpty(td.getSchema_name(), " ");
				String strCommnet = StringUtils.defaultIfEmpty(td.getComment(), " ");
				
				sbData.append(strSchemaName + PublicTadpoleDefine.DELIMITER + 
							  td.getName() + PublicTadpoleDefine.DELIMITER + 
							  strCommnet + PublicTadpoleDefine.DELIMITER + 
							  PublicTadpoleDefine.DELIMITER_DBL
						);
			}
			
			event.data = sbData.toString();
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

}
