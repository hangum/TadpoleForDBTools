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
package com.hangum.tadpole.aws.rds.commons.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.ext.aws.rds.AWSRDSUserDBDAO;
import com.hangum.tadpole.define.DBOperationType;

/**
 * AmazonRDS Utils
 * 
 * @author hangum
 *
 */
public class AmazonRDSUtsils {
	
	/**
	 * list region name
	 * 
	 * @return
	 */
	public static List<String> getRDSRegionList() {
		List<String> listRegion = new ArrayList<String>();
		
		for (Region region : RegionUtils.getRegionsForService(ServiceAbbreviations.RDS)) {
			listRegion.add(region.getName());
		}

		return listRegion;
	}

	/**
	 * Get RDS to Tadpole UserDB data.
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @param regionName
	 * @return 
	 * @throws Exception
	 */
	public static List<AWSRDSUserDBDAO> getDBList(String accessKey, String secretKey, String regionName) throws Exception {
		List<AWSRDSUserDBDAO> returnDBList = new ArrayList<AWSRDSUserDBDAO>();
		
		try {
			BasicAWSCredentials awsCredential = new BasicAWSCredentials(accessKey, secretKey);
			AmazonRDSClient rdsClient = new AmazonRDSClient(awsCredential);
			rdsClient.setRegion(RegionUtils.getRegion(regionName));
			
			DescribeDBInstancesResult describeDBInstance = rdsClient.describeDBInstances();
			List<DBInstance> listDBInstance = describeDBInstance.getDBInstances();
			for (DBInstance rdsDbInstance : listDBInstance) {
				AWSRDSUserDBDAO rdsUserDB = new AWSRDSUserDBDAO();
				
				// rds information
				rdsUserDB.setAccessKey(accessKey);
				rdsUserDB.setSecretKey(secretKey);
				rdsUserDB.setEndPoint(regionName);
				
				// ext information
				rdsUserDB.setExt1(rdsDbInstance.getDBInstanceClass());
				rdsUserDB.setExt2(rdsDbInstance.getAvailabilityZone());
				
				// db information
				String strDBMStype = rdsDbInstance.getEngine();
				if(strDBMStype.startsWith("sqlserver")) {
					String strEngVer = rdsDbInstance.getEngineVersion();
//					if(strEngVer.startsWith("11")) 
//					else strDBMStype = "MSSQL_8_LE";
					
					strDBMStype = DBDefine.MSSQL_DEFAULT.getDBToString();
				} else if(strDBMStype.startsWith("oracle")) {
					strDBMStype = DBDefine.ORACLE_DEFAULT.getDBToString();
				}
				
				rdsUserDB.setDbms_types(DBDefine.getDBDefine(strDBMStype).getDBToString());
				rdsUserDB.setDisplay_name(rdsDbInstance.getDBInstanceIdentifier() + "." + rdsDbInstance.getAvailabilityZone());
				rdsUserDB.setOperation_type(DBOperationType.DEVELOP.toString());
				rdsUserDB.setDb(rdsDbInstance.getDBInstanceIdentifier());//getDBName());
				rdsUserDB.setHost(rdsDbInstance.getEndpoint().getAddress());
				rdsUserDB.setPort(""+rdsDbInstance.getEndpoint().getPort());
				rdsUserDB.setLocale(rdsDbInstance.getCharacterSetName()==null?"":rdsDbInstance.getCharacterSetName());
				rdsUserDB.setUsers(rdsDbInstance.getMasterUsername());
				rdsUserDB.setPasswd("");
				
				returnDBList.add(rdsUserDB);
			}
		} catch(Exception e) {
			throw e;
		}
		
		return returnDBList;
	}
}
