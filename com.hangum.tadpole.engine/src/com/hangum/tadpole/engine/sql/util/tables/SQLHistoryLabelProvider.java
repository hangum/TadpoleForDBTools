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
package com.hangum.tadpole.engine.sql.util.tables;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.swtdesigner.SWTResourceManager;

/**
 * history label provider
 * @author hangum
 *
 */
@SuppressWarnings("serial")
public class SQLHistoryLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof RequestResultDAO) {
			RequestResultDAO historyDAO = (RequestResultDAO)element;
			
			switch(columnIndex) {
				case 0: return dateToStr(historyDAO.getStartDateExecute());
				// 쿼리에 개행 문자가 있으면 테이블에 개행 문자 이후 쿼리가 보이지 않으므로 보여줄 때는 개행 문자를 제거합니다.
				case 1: 
					return historyDAO.getSql_text();//StringUtils.replace(historyDAO.getStrSQLText(), "\n", "<br/>");
//					return StringEscapeUtils.escapeXml(StringUtils.replaceChars(historyDAO.getStrSQLText(), "\n", "<br> "));
				case 2: 
					return ""+( (historyDAO.getEndDateExecute().getTime() - historyDAO.getStartDateExecute().getTime()) / 1000f);
				case 3: return ""+historyDAO.getRows();
				case 4: return historyDAO.getResult();
				case 5: return historyDAO.getMesssage();
				case 6: return historyDAO.getUserName();
				case 7: return historyDAO.getDbName();
				case 8: return historyDAO.getIpAddress();
			}
		} else if(element instanceof TadpoleMessageDAO) {
			TadpoleMessageDAO dao = (TadpoleMessageDAO)element;
			switch(columnIndex) {
			case 0: return dateToStr(dao.getDateExecute());
			case 1: return dao.getStrMessage();
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

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
	 */
	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
	 */
	@Override
	public Color getBackground(Object element, int columnIndex) {
		if(element instanceof RequestResultDAO) {
			RequestResultDAO historyDAO = (RequestResultDAO)element;
			String strResult = historyDAO.getResult();
			if("F".equals(strResult)) {
				return SWTResourceManager.getColor(240, 180, 167);
			}
		}
		
		return SWTResourceManager.getColor(SWT.COLOR_GRAY);		
	}
}
