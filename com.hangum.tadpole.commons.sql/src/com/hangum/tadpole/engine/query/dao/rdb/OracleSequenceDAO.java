/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     nilriri - RDBMS information for columns.
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.rdb;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * 테이블의 모든 컬럼에 대한 정보를 조회
 * 
 * <pre>
 * 		조회 권한을 갖는 모든 테이블에 대한 컬럼 목록을 표시하여 컬럼의 통계정보 생성유무나 데이터 자료형의 불일치, 코멘드나 참조무결성 정의 여부들을 한 화면에서 조회할 수 있도록 한다.
 * 
 * </pre>
 * 
 * @author nilriri
 * 
 */
public class OracleSequenceDAO extends AbstractDAO {

	String sequence_name;
	String sequence_owner;
	long min_value;
	BigDecimal max_value;
	int increment_by;
	String cycle_flag;
	String order_flag;
	int cache_size;
	long last_number;
	
	//SEQUENCE_OWNER, SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, INCREMENT_BY, CYCLE_FLAG, ORDER_FLAG, CACHE_SIZE, LAST_NUMBER

	public OracleSequenceDAO() {
		this("", "", 0, new BigDecimal(0), 0);
	}

	public OracleSequenceDAO(String sequence_name, String sequence_owner, long min_value, BigDecimal max_value, int increment_by) {
		this.sequence_name = sequence_name;
		this.sequence_owner = sequence_owner;
		this.min_value = min_value;
		this.max_value = max_value;
		this.increment_by = increment_by;
	}

	@Override
	public String getFullName() {
		if(StringUtils.isEmpty(this.schema_name)) {
			return this.getSysName();
		}else{
			return String.format("%s.%s", this.getSchema_name(), this.getSysName());
		}
	}

	@FieldNameAnnotationClass(fieldKey = "sequence_name")
	public String getSequence_name() {
		return sequence_name;
	}

	public void setSequence_name(String sequence_name) {
		this.sequence_name = sequence_name;
	}

	@FieldNameAnnotationClass(fieldKey = "sequence_owner")
	public String getSequence_owner() {
		return sequence_owner;
	}

	public void setSequence_owner(String sequence_owner) {
		this.sequence_owner = sequence_owner;
	}

	@FieldNameAnnotationClass(fieldKey = "min_value")
	public long getMin_value() {
		return min_value;
	}

	public void setMin_value(long min_value) {
		this.min_value = min_value;
	}

	@FieldNameAnnotationClass(fieldKey = "max_value")
	public BigDecimal getMax_value() {
		return max_value;
	}

	public void setMax_value(BigDecimal max_value) {
		this.max_value = max_value;
	}

	@FieldNameAnnotationClass(fieldKey = "increment_by")
	public int getIncrement_by() {
		return increment_by;
	}

	public void setIncrement_by(int increment_by) {
		this.increment_by = increment_by;
	}

	@FieldNameAnnotationClass(fieldKey = "cycle_flag")
	public String getCycle_flag() {
		return cycle_flag;
	}

	public void setCycle_flag(String cycle_flag) {
		this.cycle_flag = cycle_flag;
	}

	@FieldNameAnnotationClass(fieldKey = "order_flag")
	public String getOrder_flag() {
		return order_flag;
	}

	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}

	@FieldNameAnnotationClass(fieldKey = "cache_size")
	public int getCache_size() {
		return cache_size;
	}

	public void setCache_size(int cache_size) {
		this.cache_size = cache_size;
	}

	@FieldNameAnnotationClass(fieldKey = "last_number")
	public long getLast_number() {
		return last_number;
	}

	public void setLast_number(long last_number) {
		this.last_number = last_number;
	}


}
