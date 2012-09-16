/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.util.tables;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.db.dialogs.message.dao.SQLLibraryDAO;
import com.hangum.db.dialogs.message.dao.TadpoleMessageDAO;

/**
 * history label provider
 * @author hangum
 *
 */
public class SQLLibraryLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof SQLLibraryDAO) {
			SQLLibraryDAO libraryDAO = (SQLLibraryDAO)element;
			
			switch(columnIndex) {
				case 0: return libraryDAO.getSeqNo()+"";
				case 1: return libraryDAO.getTitle();
				case 2: return libraryDAO.getSQLText();
				case 3: return libraryDAO.getDescription();
			}
		} else if(element instanceof TadpoleMessageDAO) {
			TadpoleMessageDAO messageDAO = (TadpoleMessageDAO)element;
			
			switch(columnIndex) {
				case 0: return dateToStr(messageDAO.getDateExecute());
				case 1: return "";
				case 2: return messageDAO.getStrMessage();
			}
		}
		
		return "### not set column ###"; //$NON-NLS-1$
	}

	/**
	 * 보기편하게
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
