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
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.DBOtherDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * manager view label provider
 * 
 * @author hangum
 *
 */
public class ManagerLabelProvider extends LabelProvider {
	private static final Logger logger = Logger.getLogger(ManagerLabelProvider.class);
	
	/** production markup start tag */
	public static String PRODUCTION_SERVER_START_TAG = "<em style='color:rgb(255, 0, 0)'>"; //$NON-NLS-1$
//	/** development markup start tag */
//	public static String DEVELOPMENT_SERVER_START_TAG = "<em style='color:rgb(224, 224, 224)'>"; //$NON-NLS-1$
	/** development markup start tag */
	public static String INFO_SERVER_START_TAG = "<em style='color:rgb(145, 129, 129)'>"; //$NON-NLS-1$
	
	/** Markup end tag */
	public static String END_TAG = "</em>"; //$NON-NLS-1$
	
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
			retText = String.format("%s [%s] %s", PRODUCTION_SERVER_START_TAG, StringUtils.substring(userDB.getOperation_type(), 0, 1), END_TAG);
//		} else {
//			retText = String.format("%s [%s] %s", DEVELOPMENT_SERVER_START_TAG, StringUtils.substring(userDB.getOperation_type(), 0, 1), END_TAG);
		}
		
		if(PermissionChecker.isDBAdminRole(userDB)) {
			retText += String.format("%s (%s@%s)", userDB.getDisplay_name(), userDB.getUsers(), userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			
			// 프러덕이나 백업디비이면디비 이름만보이면 됨.
			if(PermissionChecker.isProductBackup(userDB)) {
				retText += userDB.getDisplay_name();
			// 기타 디비 이면 다 보이면 됨.
			} else {
				retText += String.format("%s (%s@%s)", userDB.getDisplay_name(), userDB.getUsers(), userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$				
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
			
			Image baseImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sql-query.png"); //$NON-NLS-1$
			if(PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals( dao.getResource_types())) {
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
										String.format("%s [%s] %s", INFO_SERVER_START_TAG, dao.getRestapi_uri(), END_TAG):"";
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
