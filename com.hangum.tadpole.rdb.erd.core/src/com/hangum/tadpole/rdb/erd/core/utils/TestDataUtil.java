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
package com.hangum.tadpole.rdb.erd.core.utils;

import org.eclipse.draw2d.geometry.Rectangle;

import com.hangum.tadpole.rdb.model.Column;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbFactory;
import com.hangum.tadpole.rdb.model.Relation;
import com.hangum.tadpole.rdb.model.RelationKind;
import com.hangum.tadpole.rdb.model.Table;

public enum TestDataUtil {
	INSTANCE;
	private RdbFactory factory = RdbFactory.eINSTANCE;
	
	/**
	 * test data
	 * 
	 * @return
	 */
	public DB createModel() {
		DB db = factory.createDB();
		
//				db.setAutoLayout(true);
		db.setDbType("SQLite");
		db.setId("root");
		db.setUrl("");
		
		// add tables
		Table tableCity = factory.createTable();
		tableCity.setDb(db);
		tableCity.setName("city");
		tableCity.setConstraints(new Rectangle(10, 300, TadpoleModelUtils.END_TABLE_WIDTH, TadpoleModelUtils.END_TABLE_HIGHT));
		db.getTables().add(tableCity);
		
		// add columns
		Column columnCityId = factory.createColumn();
		columnCityId.setField("city_id");
		columnCityId.setType("String");
		columnCityId.setNull("No");
		columnCityId.setKey("PRI");
		columnCityId.setTable(tableCity);
		tableCity.getColumns().add(columnCityId);
		
		
		Column columnCity = factory.createColumn();
		columnCity.setField("city");
		columnCity.setType("String");
		columnCity.setNull("Yes");
		columnCity.setTable(tableCity);
		tableCity.getColumns().add(columnCity);
		
		
		Column columnCountryId = factory.createColumn();
		columnCountryId.setField("country_id");
		columnCountryId.setType("String");
		columnCountryId.setTable(tableCity);
		columnCountryId.setNull("YES");
		tableCity.getColumns().add(columnCountryId);
		
		// solo table
		Table tableSolo = factory.createTable();
		tableSolo.setDb(db);
		tableSolo.setName("Solo");
		tableSolo.setConstraints(new Rectangle(10, 150, TadpoleModelUtils.END_TABLE_WIDTH, TadpoleModelUtils.END_TABLE_HIGHT));
		db.getTables().add(tableSolo);
		
		Column columnSolo = factory.createColumn();
		columnSolo.setField("solo_id");
		columnSolo.setType("String");
		columnSolo.setTable(tableSolo);
		tableSolo.getColumns().add(columnSolo);
		
		// car table
		// table jong city
		Table tableCarInfo = factory.createTable();
		tableCarInfo.setDb(db);
		tableCarInfo.setName("Car Info");
		tableCarInfo.setConstraints(new Rectangle(10, 10, TadpoleModelUtils.END_TABLE_WIDTH, TadpoleModelUtils.END_TABLE_HIGHT));
		
		Column colCarName = factory.createColumn();
		colCarName.setField("name");
		colCarName.setType("String");
		colCarName.setTable(tableCarInfo);
		
		Relation relationCarToCity = factory.createRelation();
		relationCarToCity.setSource(tableCity);
		relationCarToCity.setSource_kind(RelationKind.ONE_OR_MANY);
		relationCarToCity.setTarget(tableCarInfo);
		relationCarToCity.setSource_kind(RelationKind.ONE_OR_MANY);
		
		
		// 테이블2
		Table tableAddress = factory.createTable();
		tableAddress.setDb(db);
		tableAddress.setName("Address");
		tableAddress.setConstraints(new Rectangle(50, 50, TadpoleModelUtils.END_TABLE_WIDTH, TadpoleModelUtils.END_TABLE_HIGHT));
		db.getTables().add(tableAddress);
		
		// 컬럼
		Column columnAddressId = factory.createColumn();
		columnAddressId.setField("address_id");
		columnAddressId.setType("String");
		columnAddressId.setTable(tableAddress);
		tableAddress.getColumns().add(columnAddressId);
		
		Column columnAddress = factory.createColumn();
		columnAddress.setField("address");
		columnAddress.setType("String");
		columnAddress.setTable(tableAddress);
		tableAddress.getColumns().add(columnAddress);

		Column columnCategory = factory.createColumn();
		columnCategory.setField("category");
		columnCategory.setType("String");
		columnCategory.setTable(tableAddress);
		tableAddress.getColumns().add(columnCategory);
		
		columnCityId = factory.createColumn();
		columnCityId.setField("city_id");
		columnCityId.setType("String");
		columnCityId.setTable(tableAddress);
		tableAddress.getColumns().add(columnCityId);
		
		
		// table3
		Table tablePerson = factory.createTable();
		tablePerson.setDb(db);
		tablePerson.setName("Person");
		tablePerson.setConstraints(new Rectangle(150, 150, TadpoleModelUtils.END_TABLE_WIDTH, TadpoleModelUtils.END_TABLE_HIGHT));
		db.getTables().add(tablePerson);
		
		Column columnPersonId = factory.createColumn();
		columnPersonId.setField("person_id");
		columnPersonId.setType("String");
		columnPersonId.setTable(tableAddress);
		tablePerson.getColumns().add(columnPersonId);
		
		Column columnPersonName = factory.createColumn();
		columnPersonName.setField("name");
		columnPersonName.setType("String");
		columnPersonName.setTable(tableAddress);
		tablePerson.getColumns().add(columnPersonName);
		
		

//				// relation - source
//				// 자신과 연결 모델 
		//
		//  주의)
		//  AutoLaytout에서 사용하는 (DirectedGraph 이나 DirectedGraphLayout)Layout 사용할때 self 연결은 되지 않습니다. 모델에 데이터 입력시 참고합니다
		//
		//
//				Relation relationMeToMe = factory.createRelation();
//				relationMeToMe.setSource(tableCity);
//				relationMeToMe.setTarget(tableCity);
//				relationMeToMe.getColumn_name().add("city_id");
//				relationMeToMe.getReferenced_column_name().add("city_id");
//				relationMeToMe.setSource_kind(RelationKind.ONLY_ONE);
//				relationMeToMe.setTarget_kind(RelationKind.ONE_OR_MANY);
//
//				// soruce -> target
		Relation relationMeToAnother = factory.createRelation();
		relationMeToAnother.setSource(tableCity);
		relationMeToAnother.setColumn_name("city");
		relationMeToAnother.setSource_kind(RelationKind.ONLY_ONE);
		
		relationMeToAnother.setTarget(tableAddress);
		relationMeToAnother.setTarget_kind(RelationKind.ONE_OR_MANY);
		relationMeToAnother.setReferenced_column_name("address");
		
		
//				// target -> target 
//				Relation relationMeToAnother2 = factory.createRelation();
//				relationMeToAnother2.setSource(tableAddress);
//				relationMeToAnother2.setSource_kind(RelationKind.ONE_OR_MANY);
//				relationMeToAnother2.setTarget(tableCity);
//				relationMeToAnother2.setSource_kind(RelationKind.ONLY_ONE);
		
//				
//				Relation relationMeToAnother3 = factory.createRelation();
//				relationMeToAnother3.setSource(tableAddress);
//				relationMeToAnother3.setSource_kind(RelationKind.ONE_OR_MANY);
//				relationMeToAnother3.setTarget(tableCity);
//				relationMeToAnother3.setSource_kind(RelationKind.ZERO_OR_MANY);
		
		// source -> another source
		Relation relationMeToAnother4 = factory.createRelation();
		relationMeToAnother4.setSource(tablePerson);
		relationMeToAnother4.setColumn_name("persaon");
		relationMeToAnother4.setSource_kind(RelationKind.ONE_OR_MANY);
		
		relationMeToAnother4.setTarget(tableCity);
		relationMeToAnother4.setTarget_kind(RelationKind.ZERO_OR_ONE);
		relationMeToAnother4.setReferenced_column_name("city");
		
		Relation relationMeToAnother5 = factory.createRelation();
		relationMeToAnother5.setSource(tableAddress);
		relationMeToAnother5.setSource_kind(RelationKind.ONE_OR_MANY);
		relationMeToAnother5.setTarget(tablePerson);
		relationMeToAnother5.setSource_kind(RelationKind.ONE_OR_MANY);
		
		return db;
	}
		
}
