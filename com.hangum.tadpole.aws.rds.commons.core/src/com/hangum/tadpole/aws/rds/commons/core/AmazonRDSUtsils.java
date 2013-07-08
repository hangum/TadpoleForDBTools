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
package com.hangum.tadpole.aws.rds.commons.core;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.ext.aws.rds.AWSRDSUserDBDAO;

/**
 * AmazonRDS 를 사용하는 Utils
 * 
 * @author hangum
 *
 */
public class AmazonRDSUtsils {

	/**
	 * rds 자료를 가져와서 올챙이가 처리하려는 자료 형태로 만듭니다.
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @param endPoint
	 * @return 
	 * @throws Exception
	 */
	public static List<AWSRDSUserDBDAO> getDBList(String accessKey, String secretKey, String endPoint) throws Exception {
		List<AWSRDSUserDBDAO> returnDBList = new ArrayList<AWSRDSUserDBDAO>();
		
		BasicAWSCredentials awsCredential = new BasicAWSCredentials(accessKey, secretKey);
		AmazonRDSClient rdsClient = new AmazonRDSClient(awsCredential);
		rdsClient.setEndpoint(endPoint);//"rds.ap-northeast-1.amazonaws.com");
		
		DescribeDBInstancesResult describeDBInstance = rdsClient.describeDBInstances();
		List<DBInstance> listDBInstance = describeDBInstance.getDBInstances();
		for (DBInstance rdsDbInstance : listDBInstance) {
			AWSRDSUserDBDAO userDB = new AWSRDSUserDBDAO();
			
			//
			userDB.setAccessKey(accessKey);
			userDB.setSecretKey(secretKey);
			userDB.setEndPoint(endPoint);
			
			// ext information
			userDB.setExt1(rdsDbInstance.getDBInstanceClass());
			userDB.setExt2(rdsDbInstance.getAvailabilityZone());
			
			// db information
			userDB.setDbms_types(DBDefine.getDBDefine(rdsDbInstance.getEngine()).getDBToString());
			userDB.setDisplay_name(rdsDbInstance.getDBName() + "." + rdsDbInstance.getAvailabilityZone());
			userDB.setDb(rdsDbInstance.getDBName());
			userDB.setHost(rdsDbInstance.getEndpoint().getAddress());
			userDB.setPort(""+rdsDbInstance.getEndpoint().getPort());
			userDB.setLocale(rdsDbInstance.getCharacterSetName());
			userDB.setUsers(rdsDbInstance.getDBName());
			
			userDB.setDbms_types(rdsDbInstance.getEngine());
			
			returnDBList.add(userDB);
		}
		
		return returnDBList;
		
	}
}
