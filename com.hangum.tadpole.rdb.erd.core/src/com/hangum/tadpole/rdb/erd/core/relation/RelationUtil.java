/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.ReferencedTableDAO;
import com.hangum.tadpole.dao.sqlite.SQLiteRefTableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.erd.core.dnd.TableTransferDropTargetListener;
import com.hangum.tadpole.rdb.model.Column;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbFactory;
import com.hangum.tadpole.rdb.model.Relation;
import com.hangum.tadpole.rdb.model.RelationKind;
import com.hangum.tadpole.rdb.model.Table;
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
	 * 특정 테이블 관계를 조회합니다.
	 * 
	 * @param userDB
	 * @param mapDBTables
	 * @param db
	 * @param refTableNames
	 * @throws Exception
	 */
	public static void calRelation(UserDBDAO userDB, Map<String, Table> mapDBTables, DB db, String refTableNames) throws Exception {
			
		// 현재 sqlite는 관계 정의를 못하겠는바 막습니다.
		if(DBDefine.SQLite_DEFAULT.getDBToString().equals( userDB.getTypes() )) {
			
			calRelation(userDB, mapDBTables, db, makeSQLiteRelation(userDB));
			
		} else if(DBDefine.CUBRID_DEFAULT.getDBToString().equals( userDB.getTypes() )) {
			
			calRelation(userDB, mapDBTables, db, CubridTableRelation.makeCubridRelation(userDB, refTableNames));
			
		} else {
		
			calRelation(userDB, mapDBTables, db, getReferenceTable(userDB, refTableNames));
		}
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
		
		// 현재 sqlite는 관계 정의를 못하겠는바 막습니다.
		if(DBDefine.SQLite_DEFAULT.getDBToString().equals( userDB.getTypes() )) {
			 
			calRelation(userDB, mapDBTables, db, makeSQLiteRelation(userDB));
			
		} else if(DBDefine.CUBRID_DEFAULT.getDBToString().equals( userDB.getTypes() )) {
			
			calRelation(userDB, mapDBTables, db, CubridTableRelation.makeCubridRelation(userDB));
			
		} else {

			calRelation(userDB, mapDBTables, db, getReferenceTable(userDB));
		}
	}
	
	/**
	 * sqlite의 relation을 만든다.
	 * 
	 * @param userDB
	 * @return
	 */
	private static List<ReferencedTableDAO> makeSQLiteRelation(UserDBDAO userDB) {
		List<ReferencedTableDAO> listRealRefTableDAO = new ArrayList<ReferencedTableDAO>();
		
		try {
			// 실제 레퍼런스를 생성합니다.
			for (SQLiteRefTableDAO sqliteRefTableDAO : getSQLiteRefTbl(userDB)) {
	
				int indexKey = StringUtils.indexOf(sqliteRefTableDAO.getSql(), "FOREIGN KEY");
				String forKey = sqliteRefTableDAO.getSql().substring(indexKey);
				if(logger.isDebugEnabled()) logger.debug("\t full text:" + sqliteRefTableDAO.getSql());
				if(logger.isDebugEnabled()) logger.debug("\t=================>[forKeys]\n" + forKey);
				String[] foreignInfo =forKey.split("FOREIGN KEY");
				
				
				for(int i=1; i<foreignInfo.length; i++) {
					try {
						String strForeign = foreignInfo[i];
						if(logger.isDebugEnabled()) logger.debug("\t ==========================> sub[\n" + strForeign + "]");
						ReferencedTableDAO ref = new ReferencedTableDAO();
						
						// 테이블 명
						ref.setTable_name(sqliteRefTableDAO.getTbl_name());
						
						// 컬럼명, 첫번째 ( 시작 부터 ) 끝날때까지...
						String colName = StringUtils.substringBetween(strForeign, "(", ")");
						
						// 참조 테이블,  REFERENCES 로 시작 하는 다음 부터 (까지
						String refTbName = StringUtils.substringBetween(strForeign, "REFERENCES", "(");
						
						// 참조 컬럼,  refTbName의 끝나는 것부터 ) 까지...
						String refCol	 = StringUtils.substringBetween(strForeign, refTbName+"(" , ")");
						
						ref.setColumn_name(moveSpec(colName));
						ref.setReferenced_table_name(moveSpec(refTbName));
						ref.setReferenced_column_name(moveSpec(refCol));
						
						// sqlite는 인덱스 네임이 없으므로.... 생성합니다.
						ref.setConstraint_name(ref.toString());

						listRealRefTableDAO.add(ref);
					
					} catch(Exception e) {
						logger.error("SQLLite Relation making", e);
					}
				} 	// inner if
	
			}	// last for
		} catch(Exception e) {
			logger.error("SQLite Relation check 2", e);
		}
		
		return listRealRefTableDAO;
	}
	
	private static String moveSpec(String val) {
		return StringUtils.trimToEmpty( val.replaceAll("\\[", "").replaceAll("\\]", "") );
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
		
		RdbFactory tadpoleFactory = RdbFactory.eINSTANCE;
		
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
			
				/*
				 * 위의 경우의 수를 이용하여 릴레이션을 생성합니다.
				 */
				Map<String, Column> sourceColumnsMap = new HashMap<String, Column>();
				Map<String, Column> targetColumnMap = new HashMap<String, Column>();
				for (Column column : soTabMod.getColumns()) sourceColumnsMap.put(column.getField(), column);
				for (Column column : tarTabMod.getColumns()) targetColumnMap.put(column.getField(), column);
				
				// source 컬럼 정보
				Column col = sourceColumnsMap.get(refTabDAO.getColumn_name());
				
				
				// target 컬럼 정보
				Column colR = targetColumnMap.get(refTabDAO.getReferenced_column_name());
				if(logger.isDebugEnabled()) {
					if(col == null || colR == null) {
						logger.debug("###[table index]###############################################################################");
						logger.debug(db.getUrl() + ":" + refTabDAO.toString());
						logger.debug("###[table index]###############################################################################");
					}
				}
				if(col == null || colR == null) continue;
				
//				logger.debug("\t [source ]" + col.getField() + " [key]" + col.getKey());
//				logger.debug("\t [target ]" + colR.getField() + " [key]" + colR.getKey());
				relation.setSource_kind( calcRelationCol(col, colR) );
				relation.setTarget_kind( calcRelationCol(colR, col) );
					
				// 관계형성
				soTabMod.getIncomingLinks().add(relation);
				tarTabMod.getOutgoingLinks().add(relation);
				
				relation.setSource(soTabMod);
				relation.setTarget(tarTabMod);

			}// if(souceModel != null && targetModel != null
			
		}	// for
	}


	/**
	 * 키 컬럼의의 조건을 relational type을 분석합니다.
	 * 
	 * 상대방 컬럼이 NULL을 허용하면 자신은 없을수도 있음.
	 * 
	 * @param soCol source table
	 * @rapap taCol target table
	 * @return
	 */
	public static RelationKind calcRelationCol(Column soCol, Column taCol) {
		if("YES".equals( taCol.getNull() ) || "YES".equals( soCol.getNull() )) {
			
			if( Define.isPK( soCol.getKey() )) return RelationKind.ZERO_OR_ONE;
			else return RelationKind.ZERO_OR_MANY;
		
		} else {
		
			if( Define.isPK( soCol.getKey() )) return RelationKind.ONLY_ONE;
			else return RelationKind.ONE_OR_MANY;
			
		}
	}
	
	/**
	 * sqlite 참조용 테이블
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<SQLiteRefTableDAO> getSQLiteRefTbl(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("referencedTableListALL");
	}
	
	/**
	 * sqlite 참조용 테이블
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<SQLiteRefTableDAO> getSQLiteRefTbl(UserDBDAO userDB, String tableName) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("referencedTableList", tableName);
	}
	
	/**
	 * 테이블의 참조 목록 정보를 리턴합니다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<ReferencedTableDAO> getReferenceTable(UserDBDAO userDB, String tableName) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("referencedTableList", tableName);
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
