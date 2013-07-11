package com.hangum.tadpole.mongodb.core.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
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
	 * 
	 * @return
	 */
	public static String getAssistList(final UserDBDAO userDB) {
		String strTablelist = "";
		
		try {
			List<TableDAO> listCollFields = MongoDBQuery.listCollection(userDB);
			for (TableDAO tableDao : listCollFields) {
				strTablelist += tableDao.getName() + ",";
			}
			strTablelist = StringUtils.removeEnd(strTablelist, ",");
		} catch(Exception e) {
			logger.error("MongoDB groupeditor get the table list", e);
		}
		
		return strTablelist;
	}
}
