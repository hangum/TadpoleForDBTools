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
package com.hangum.tadpole.mongodb.erd.core.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ReferencedTableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.erd.core.utils.MongodbUtils;
import com.hangum.tadpole.mongodb.model.Column;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.MongodbFactory;
import com.hangum.tadpole.mongodb.model.Relation;
import com.hangum.tadpole.mongodb.model.RelationKind;
import com.hangum.tadpole.mongodb.model.Table;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ERD 출력시 Table relation 관련 코드
 * 
 * <pre>   	
     컬럼 설명
	constraint_name		:	 인덱스 이름
	table_name				:	 소스 테이블 이름
	column_name			:	 소스 테이블 컬럼
	
	referenced_table_name	:	타켓 테이블 이름
	referenced_column_name	: 	타켓 테이블 컬럼
 * </pre>
 * 
 * @author hangum
 *
 */
public class RelationUtil {
	private static final Logger logger = Logger.getLogger(RelationUtil.class);
	
	/**
	 * <pre>
	 * 특정 테이블 관계를 조회합니다.
	 * 
	 * object type이 org.bson.types.ObjectId 이거나 field이름이 _id로 시작하지 않고 _id로 끝나야 레퍼런스 컬럼으로 인식합니다.
	 * </pre>
	 * 
	 * @param userDB
	 * @param mapDBTables
	 * @param db
	 * @param refTableNames
	 * @throws Exception
	 */
	public static void calRelation(UserDBDAO userDB, Map<String, Table> mapDBTables, DB db, String refTableNames) throws Exception {
		List<ReferencedTableDAO> listRealRefTableDAO = new ArrayList<ReferencedTableDAO>();
		
		Set<String> keySet = mapDBTables.keySet();
		for (String keyTable : keySet) {
			Table table = mapDBTables.get(keyTable);
			EList<Column> listColumn = table.getColumns();
			for (Column column : listColumn) {
				String strField = column.getField();
				
				if(MongodbUtils.isReferenceKey(column.getType(), strField)) {
					String strRefName = StringUtils.remove(strField, "_id");
					
					ReferencedTableDAO refTableDao = new ReferencedTableDAO();
					refTableDao.setTable_name(table.getName());
					refTableDao.setColumn_name(strRefName);
					refTableDao.setReferenced_table_name(strRefName);
					refTableDao.setReferenced_column_name(table.getName());
					refTableDao.setConstraint_name(strRefName);
					
					listRealRefTableDAO.add(refTableDao);
				}
			}
		}
		
		calRelation(userDB, mapDBTables, db, listRealRefTableDAO);
	}
	
	/**
	 * 모든 테이블을 조회합니다.
	 * 
	 * @param userDB
	 * @param mapDBTables
	 * @param db
	 * @throws Exception
	 */
	public static void calRelation(UserDBDAO userDB, Map<String, Table> mapDBTables, DB db)  throws Exception {
		List<ReferencedTableDAO> listRealRefTableDAO = new ArrayList<ReferencedTableDAO>();
		
		Set<String> keySet = mapDBTables.keySet();
		for (String keyTable : keySet) {
			Table table = mapDBTables.get(keyTable);
			EList<Column> listColumn = table.getColumns();
			for (Column column : listColumn) {
				String strField = column.getField();
				
				if(MongodbUtils.isReferenceKey(column.getType(), strField)) {					
					ReferencedTableDAO refTableDao = new ReferencedTableDAO();
					refTableDao.setTable_name(table.getName());
					refTableDao.setColumn_name(strField);
					refTableDao.setReferenced_table_name(StringUtils.remove(strField, "_id"));
					refTableDao.setReferenced_column_name(strField);
					refTableDao.setConstraint_name(strField);
					
					listRealRefTableDAO.add(refTableDao);
				}
			}
		}
		
		calRelation(userDB, mapDBTables, db, listRealRefTableDAO);
	}

	/**
	 * 테이블 관계를 구성합니다.
	 * 
	 * @param userDB
	 * @param mapDBTables
	 * @param db
	 * @throws Exception
	 */
	public static void calRelation(UserDBDAO userDB, Map<String, Table> mapDBTables, DB db, List<ReferencedTableDAO> referenceTableList) throws Exception {
		
		MongodbFactory tadpoleFactory = MongodbFactory.eINSTANCE;
		
		// 디비에서 관계 정보를 찾아서 넣어준다.
		for (ReferencedTableDAO refTabDAO : referenceTableList) {
			Table soTabMod = mapDBTables.get( refTabDAO.getTable_name() );
			Table tarTabMod = mapDBTables.get( refTabDAO.getReferenced_table_name() );
			
			// 소스테이블에 인덱스가 없고, 타겟 테이블이 있으면 추가한다. 
			if(soTabMod != null && tarTabMod != null) {
				
				// 이미 추가된 relation인지 검사합니다.
				boolean isAlrealyAppend = false;
				for(Relation relation : soTabMod.getOutgoingLinks()) {
					
					if( relation.getConstraint_name() != null && refTabDAO.getConstraint_name() != null ) {
						if (relation.getConstraint_name().equals(refTabDAO.getConstraint_name())) {
							isAlrealyAppend = true;
							break;
						}
					}
				}
				for (Relation relation : soTabMod.getIncomingLinks()) {
					
					if( relation.getConstraint_name() != null && refTabDAO.getConstraint_name() != null ) {
						if (relation.getConstraint_name().equals(refTabDAO.getConstraint_name())) {
							isAlrealyAppend = true;
							break;						
						}
					}
				}
				
				// TODO 현재 자신의 테이블을 키로 가자고 있는 항목은 다음과 같은 이유로 제거 합니다. 
				// java.lang.RuntimeException: Cycle detected in graph
				if(refTabDAO.getTable_name().equals(refTabDAO.getReferenced_table_name())) continue;
				
				// 이미 추가 되어 있는가?
				if(isAlrealyAppend) continue;

				// 새롭게 추가될 요소 이면.
				Relation relation = tadpoleFactory.createRelation();
				/* 저장시 아래와 같은 오류가 발생하여 추가한 코드
				 * 여유를 가지고 디버깅을 해봐야 하는코드
				 *  
				 * org.eclipse.emf.ecore.resource.Resource$IOWrappedException: 
				 * The object 'com.hangum.tadpole.model.impl.RelationImpl@5e44efa0 (source_kind: ONLY_ONE, target_kind: ONE_MANY, column_name: country_id, referenced_column_name: country_id, bendpoint: [], constraint_name: null)' 
				 * is not contained in a resource.
				 */
				relation.setDb(db);
				
				relation.setConstraint_name(refTabDAO.getConstraint_name());
				
				relation.setColumn_name(refTabDAO.getColumn_name());
				relation.setReferenced_column_name(refTabDAO.getReferenced_column_name());
			
				relation.setSource_kind( RelationKind.ONLY_ONE );
				relation.setTarget_kind( RelationKind.ONLY_ONE );
					
				// 관계형성
				soTabMod.getIncomingLinks().add(relation);
				tarTabMod.getOutgoingLinks().add(relation);
				
				relation.setSource(soTabMod);
				relation.setTarget(tarTabMod);

			}// if(souceModel != null && targetModel != null
			
		}	// for
	}

	/**
	 * 테이블의 참조 목록 정보를 리턴합니다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<ReferencedTableDAO> getReferenceTable(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("referencedTableListALL");
	}

}
