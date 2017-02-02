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
package com.hangum.tadpole.rdb.core.viewers.connections;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.DBOtherDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.swtdesigner.ResourceManager;

/**
 * manager view label provider
 * 
 * @author hangum
 *
 */
public class ManagerLabelProvider extends ColumnLabelProvider {
	private static final Logger logger = Logger.getLogger(ManagerLabelProvider.class);
	
//	/** production markup start tag */
//	public static String PRODUCTION_SERVER_START_TAG = "<em style='color:rgb(255, 0, 0)'>"; //$NON-NLS-1$
//	/** development markup start tag */
//	public static String DEVELOPMENT_SERVER_START_TAG = "<em style='color:rgb(224, 224, 224)'>"; //$NON-NLS-1$
//	/** development markup start tag */
//	public static String INFO_SERVER_START_TAG = "<em style='color:rgb(145, 129, 129)'>"; //$NON-NLS-1$
//	
//	/** Markup end tag */
//	public static String END_TAG = "</em>"; //$NON-NLS-1$
//	
	@Override
	public String getToolTipText(Object element) {
		if(element instanceof UserDBDAO) {
			UserDBDAO userDB = (UserDBDAO)element;
			String retText = "";
			
			if(PublicTadpoleDefine.DBOperationType.PRODUCTION.toString().equals(userDB.getOperation_type())) {
				retText += String.format(Messages.get().DBType, userDB.getOperation_type());
			}
			
			// master, slave 표시
			if(!StringUtils.isBlank(userDB.getDuplication_type())) {
				retText += String.format(Messages.get().DBReplication, userDB.getDuplication_type());
			}
			
			if("YES".equals(userDB.getReadonly())) {
				retText += String.format(Messages.get().DBReadOnly, userDB.getReadonly());
			}
			
			return retText;
		}
		return null;
	}
	
	@Override
	public Point getToolTipShift(Object object) {
		return new Point(1, 1);
	}
	
	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 0;
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}

	/**
	 * get group image
	 * 
	 * @return
	 */
	public static Image getGroupImage() {
		return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/server_database.png"); //$NON-NLS-1$
	}
	
	/**
	 * user label text
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getDBText(UserDBDAO userDB) {
		String retText = "";
		
		if(PublicTadpoleDefine.DBOperationType.PRODUCTION.toString().equals(userDB.getOperation_type())) {
//			retText = String.format("%s [%s] %s", PRODUCTION_SERVER_START_TAG, StringUtils.substring(userDB.getOperation_type(), 0, 1), END_TAG);
			retText = String.format("[%s]", StringUtils.substring(userDB.getOperation_type(), 0, 1));
//		} else {
//			retText = String.format("%s [%s] %s", DEVELOPMENT_SERVER_START_TAG, StringUtils.substring(userDB.getOperation_type(), 0, 1), END_TAG);
		}
		
		// master, slave 표시
		if(!StringUtils.isBlank(userDB.getDuplication_type())) {
			retText += String.format("[%s]", StringUtils.substring(userDB.getDuplication_type(), 0, 1)); //$NON-NLS-3$
		}
		
		String strReadOnly = userDB.getReadonly();
		if(strReadOnly.equalsIgnoreCase("YES")) {
			retText += String.format("[R]"); //$NON-NLS-3$
		}
		
		if(PermissionChecker.isDBAdminRole(userDB)) {
			if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
				retText += String.format("%s", userDB.getDisplay_name()); //$NON-NLS-3$
			} else {
				retText += String.format("%s (%s)", userDB.getDisplay_name(), userDB.getUsers()); //$NON-NLS-3$	
			}
		} else {
			
			// 프러덕이나 백업디비이면디비 이름만보이면 됨.
			if(PermissionChecker.isProductBackup(userDB)) {
				retText += userDB.getDisplay_name();
			// 기타 디비 이면 다 보이면 됨.
			} else {
				if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
					retText += String.format("%s", userDB.getDisplay_name()); //$NON-NLS-3$
				} else {
					retText += String.format("%s (%s)", userDB.getDisplay_name(), userDB.getUsers()); //$NON-NLS-3$	
				}	
			}
		}
		
		return retText;
	}
	
	@Override
	public Image getImage(Object element) {

		if(element instanceof ManagerListDTO) {
			return getGroupImage();

		} else if(element instanceof UserDBDAO) {
			return DBIconsUtils.getDBConnectionImage((UserDBDAO)element);	
		} else if(element instanceof ResourcesDAO) {
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/managerExplorer/resources.png"); //$NON-NLS-1$
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			
			Image baseImage = null;
			if(PublicTadpoleDefine.RESOURCE_TYPE.SQL.toString().equals( dao.getResource_types())) {
				baseImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sql-query.png"); //$NON-NLS-1$
			} else if(PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals( dao.getResource_types())) {
				baseImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/erd.png"); //$NON-NLS-1$
			}
			
			// 이미지 캐쉬에 문제가 있어서 주석처리.
//			if(PublicTadpoleDefine.SHARED_TYPE.PRIVATE.name().equals(dao.getShared_type())) {
//				try {
//					baseImage = DBIconsUtils.getDecorateImage(baseImage, "resources/icons/lock_0.28.png", ResourceManager.TOP_RIGHT);
//				} catch(Exception e) {
//					logger.error("image decorate error", e);
//				}
//			}
			
			return baseImage;
		} else if(element instanceof DBOtherDAO) {
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/managerExplorer/extension.png"); //$NON-NLS-1$
		}
		
		return super.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof ManagerListDTO) {
			ManagerListDTO dto = (ManagerListDTO)element;
			return dto.getName();
			
		} else if(element instanceof UserDBDAO) {
			return getDBText((UserDBDAO)element);
		} else if(element instanceof ResourcesDAO) {
			ResourcesDAO dto = (ResourcesDAO)element;
			return dto.getName();
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			String strShareType = "[Pu] ";
			if(PublicTadpoleDefine.SHARED_TYPE.PRIVATE.name().equals(dao.getShared_type())) {
				strShareType = "[Pr] ";
			}
			
			String strSupportAPI = PublicTadpoleDefine.YES_NO.YES.name().equals(dao.getRestapi_yesno())?
//										String.format("%s [%s] %s", INFO_SERVER_START_TAG, dao.getRestapi_uri(), END_TAG):"";
					String.format("[%s]",  dao.getRestapi_uri()):"";
			String strComment = "".equals(dao.getDescription())?"":" (" + dao.getDescription() + ")";
			
			return strShareType + dao.getName() + " " + strSupportAPI + strComment;
		} else if(element instanceof DBOtherDAO) {
			DBOtherDAO dao = (DBOtherDAO)element;
			if("".equals(dao.getComment()) || "null".equals(dao.getComment()) || null == dao.getComment()) return dao.getName();
			else return String.format("%s (%s)", dao.getName(), dao.getComment());
		}
		
		return "## not set ##"; //$NON-NLS-1$
	}
	
}
