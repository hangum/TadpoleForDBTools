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
package com.hangum.tadpole.mongodb.core.test;

import java.io.File;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * db.stats();
 * 
 * @author hangum
 * 
 */
public class MongoTestGridFS  {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
//		saveImage(db);
//		getImage(db);
		allList(db);
		
		mongo.close();
	}
	
	private static void allList(DB db) throws Exception {
		System.out.println("##[all GridFs list] [start]######################");
		
		GridFS gridFs = new GridFS(db);
		DBCursor dbCursor = gridFs.getFileList();
		for (DBObject dbObject : dbCursor) {
			System.out.println(dbObject);
		}
		
		System.out.println("##[all GridFs list] [end]######################");
	}
	
	private static void getImage(DB db) throws Exception {
		String newFileName = "mkyong-java-image";
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
		System.out.println(imageForOutput);
		
	}

	private static void saveImage(DB db) throws Exception{

		String newFileName = "currentop";
		File imageFile = new File("c:/temp/currentop.png");
		
		GridFS gfsPhoto = new GridFS(db);
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
		gfsFile.setFilename(newFileName);
		gfsFile.save();
		
	}

}
