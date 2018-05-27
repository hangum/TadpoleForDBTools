package com.hangum.tadpole.mongodb.core.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;

/**
 * mongodb collection 다루는 utils.
 * 
 * @author hangum
 *
 */
public class CollectionUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CollectionUtils.class);

	/**
	 * get assist filter
	 * 
	 * @param userDB
	 * @param strCollectionName
	 * 
	 * @return
	 */
	public static String getAssistList(final UserDBDAO userDB, final String strCollectionName) {
		String strAssistList = "";
		
		try {
			List<CollectionFieldDAO> showTableColumns = MongoDBQuery.collectionColumn(userDB, strCollectionName);
			for (CollectionFieldDAO collectionFieldDAO : showTableColumns) {
				strAssistList += collectionFieldDAO.getField() + ",";
				
				if(collectionFieldDAO.getChildren().size() > 0) {
					strAssistList = getChildFild(strAssistList, collectionFieldDAO.getField(), collectionFieldDAO.getChildren());
				}
			}

			strAssistList = StringUtils.removeEnd(strAssistList, ",");
		} catch(Exception e) {
			logger.error("MongoDB groupeditor get the table list", e);
		}
		
		return strAssistList;
	}
	
	/**
	 * child field
	 * 
	 * @param strAssistList
	 * @param parentCollectionName
	 * @param listCollection
	 * @return
	 */
	private static String getChildFild(String strAssistList, final String parentCollectionName, final List<CollectionFieldDAO> listCollection) {
		for (CollectionFieldDAO collectionFieldDAO : listCollection) {
			strAssistList += parentCollectionName + "." + collectionFieldDAO.getField() + ",";
			
			if(collectionFieldDAO.getChildren().size() > 0) {
				strAssistList = getChildFild(strAssistList, collectionFieldDAO.getField(), collectionFieldDAO.getChildren());
			}
		}
		strAssistList = StringUtils.removeEnd(strAssistList, ",");
		
		return strAssistList;
	}
}
